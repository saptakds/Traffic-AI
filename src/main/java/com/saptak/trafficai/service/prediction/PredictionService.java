package com.saptak.trafficai.service.prediction;

import com.fasterxml.jackson.databind.JsonNode;
import com.saptak.trafficai.config.CustomVisionConfig;
import com.saptak.trafficai.enums.Camera;
import com.saptak.trafficai.enums.Road;
import com.saptak.trafficai.mapper.CameraRoadMapper;
import com.saptak.trafficai.state.SignalControlStateManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionService {

    public static final Logger logger = LoggerFactory.getLogger(PredictionService.class);

    private final CustomVisionConfig customVisionConfig;
    private final SignalControlStateManager signalControlStateManager;

    public void checkForAmbulance(List<File> frames, Camera camera) {
        Road road = CameraRoadMapper.getRoadForCamera(camera);
        boolean ambulanceDetected = false;

        // ACTUAL DETECTION - Using Azure Custom Vision Model
        for (File frame : frames) {
            try {
                JsonNode response = predictSingleFrame(frame);
                logger.info("Prediction for {}: {}", frame.getName(), response);

                for (JsonNode prediction : response.get("predictions")) {
                    String tagName = prediction.get("tagName").asText();
                    double probability = prediction.get("probability").asDouble();

                    if ("ambulance".equalsIgnoreCase(tagName) &&
                            probability > customVisionConfig.getPredictionThreshold()) {

                        logger.info("🚨 Ambulance detected in {} on {} with confidence {}%",
                                frame.getName(), road, Math.round(probability * 100));
                        ambulanceDetected = true;
                        break; // no need to process more predictions in this frame
                    }
                }

                if (ambulanceDetected) break; // break early if found in any frame

            } catch (Exception e) {
                logger.error("Failed to predict frame: {}", frame.getName(), e);
            }
        }

        // DETECTION DECOY ONLY - To avoid Custom Vision API Limits
//        ambulanceDetected = frames.size() > 5;

        if (ambulanceDetected) {
            signalControlStateManager.enterPriorityMode(road);
        } else {
            signalControlStateManager.exitPriorityModeIfSafe(road);
        }
    }

    private JsonNode predictSingleFrame(File frame) {
        String uri = String.format("%s/customvision/v3.0/Prediction/%s/classify/iterations/%s/image",
                customVisionConfig.getEndpoint(),
                customVisionConfig.getProjectId(),
                customVisionConfig.getPublishedName()
        );

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image", new FileSystemResource(frame))
                .header("Content-Disposition", "form-data; name=image; filename=" + frame.getName());

        return customVisionConfig
                .getCustomVisionClient()
                .post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Prediction-Key", customVisionConfig.getKey())
                .bodyValue(bodyBuilder.build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}

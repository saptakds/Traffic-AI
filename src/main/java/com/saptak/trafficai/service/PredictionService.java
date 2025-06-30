package com.saptak.trafficai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.saptak.trafficai.config.CustomVisionConfig;
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

    public void checkForAmbulance(List<File> frameFiles) {
        for (File frame : frameFiles) {
            try {
                JsonNode response = predictSingleFrame(frame);
                logger.info("Prediction for {}: {}", frame.getName(), response.toPrettyString());
                response.get("predictions").forEach(prediction -> {
                    String tagName = prediction.get("tagName").asText();
                    double probability = prediction.get("probability").asDouble();
                    if ("ambulance".equalsIgnoreCase(tagName) && probability > customVisionConfig.getPredictionThreshold()) {
                        logger.info("🚨 Ambulance detected in {} with confidence {}%", frame.getName(), Math.round(probability * 100));
                    }
                });
            } catch (Exception e) {
                logger.error("Failed to predict frame: {}", frame.getName(), e);
            }
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

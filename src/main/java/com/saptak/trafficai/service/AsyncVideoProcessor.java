package com.saptak.trafficai.service;

import com.saptak.trafficai.config.FrameExtractionConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncVideoProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AsyncVideoProcessor.class);

    private final FrameExtractorService frameExtractorService;
    private final FrameExtractionConfig config;
    private final PredictionService predictionService;

    @Async
    public void analyseVideo(MultipartFile videoFile) {
        File tempFile = null;
        try {
            logger.info("Started async video analysis for: {}", videoFile.getOriginalFilename());

            tempFile = File.createTempFile("upload_", ".mp4");
            videoFile.transferTo(tempFile);

            List<File> frames = frameExtractorService.extractFrames(tempFile, config.getIntervalSeconds());
            logger.info("Extracted {} frames from video: {}", frames.size(), videoFile.getOriginalFilename());

            predictionService.checkForAmbulance(frames);

        } catch (IOException e) {
            logger.error("I/O error while processing video: {}", videoFile.getOriginalFilename(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during async video analysis", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) logger.warn("Could not delete temp video file: {}", tempFile.getAbsolutePath());
            }
        }
    }
}

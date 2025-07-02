package com.saptak.trafficai.service.video;

import com.saptak.trafficai.enums.Camera;
import com.saptak.trafficai.model.video.VideoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    public static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final AsyncVideoProcessor asyncVideoProcessor;

    public VideoResponse getVideoUploadResult(MultipartFile videoFile, Camera camera) {
        long timestamp = System.currentTimeMillis();
        asyncVideoProcessor.analyseVideo(videoFile, camera);
        return VideoResponse.builder()
                .fileName(videoFile.getOriginalFilename())
                .camera(camera)
                .sizeInBytes(videoFile.getSize())
                .timestamp(timestamp)
                .message("Video received successfully")
                .build();
    }
}

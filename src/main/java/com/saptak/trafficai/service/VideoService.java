package com.saptak.trafficai.service;

import com.saptak.trafficai.enums.Camera;
import com.saptak.trafficai.model.video.VideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    public VideoResponse getVideoUploadResult(MultipartFile videoFile, Camera camera) {
        long timestamp = System.currentTimeMillis();
        return VideoResponse.builder()
                .fileName(videoFile.getOriginalFilename())
                .camera(camera)
                .sizeInBytes(videoFile.getSize())
                .timestamp(timestamp)
                .message("Video received successfully")
                .build();
    }
}

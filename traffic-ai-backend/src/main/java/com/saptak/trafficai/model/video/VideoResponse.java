package com.saptak.trafficai.model.video;

import com.saptak.trafficai.enums.Camera;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoResponse {
    private String fileName;
    private Camera camera;
    private long sizeInBytes;
    private long timestamp;
    private String message;
}

package com.saptak.trafficai.mapper;

import com.saptak.trafficai.enums.Camera;
import com.saptak.trafficai.enums.Road;

public class CameraRoadMapper {
    public static Road getRoadForCamera(Camera camera) {
        return switch (camera) {
            case CAM_A -> Road.ROAD_A;
            case CAM_B -> Road.ROAD_B;
            case CAM_C -> Road.ROAD_C;
            case CAM_D -> Road.ROAD_D;
        };
    }
}

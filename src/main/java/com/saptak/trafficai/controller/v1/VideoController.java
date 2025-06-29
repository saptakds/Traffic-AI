package com.saptak.trafficai.controller.v1;

import com.saptak.trafficai.enums.Camera;
import com.saptak.trafficai.model.video.VideoResponse;
import com.saptak.trafficai.service.VideoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.saptak.trafficai.common.Strings.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/video")
@AllArgsConstructor
public class VideoController {

    public static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = @ApiResponse(responseCode = "200",
            content = @Content(
                    schema = @Schema(implementation = VideoResponse.class))))
    public ResponseEntity<Object> uploadVideo(@RequestParam MultipartFile videoFile, @RequestParam Camera camera
    ) {
        logger.info("Received video from camera {} : {}", camera, videoFile.getOriginalFilename());
        return ResponseEntity.ok(videoService.getVideoUploadResult(videoFile, camera));
    }
}

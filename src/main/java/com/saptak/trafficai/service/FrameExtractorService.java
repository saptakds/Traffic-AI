package com.saptak.trafficai.service;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrameExtractorService {

    public static final Logger logger = LoggerFactory.getLogger(FrameExtractorService.class);

    public List<File> extractFrames(File videoFile, int intervalSeconds) {
        List<File> extractedFrames = new ArrayList<>();

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
            grabber.start();
            double fps = grabber.getFrameRate();
            int frameInterval = (int) (fps * intervalSeconds);

            Java2DFrameConverter converter = new Java2DFrameConverter();
            Frame frame;
            int frameCount = 0;

            while ((frame = grabber.grabImage()) != null) {
                if (frameCount % frameInterval == 0) {
                    BufferedImage img = converter.convert(frame);
                    if (img != null) {
                        File output = File.createTempFile("frame-" + frameCount, ".jpg");
                        ImageIO.write(img, "jpg", output);
                        logger.info("Saved frame: {}", output.getAbsolutePath());
                        extractedFrames.add(output);
                    }
                }
                frameCount++;
            }

            grabber.stop();

        } catch (Exception e) {
            logger.error("Error extracting frames", e);
            throw new RuntimeException("Failed to extract frames from video", e);
        } finally {
            videoFile.deleteOnExit();
        }

        return extractedFrames;
    }
}

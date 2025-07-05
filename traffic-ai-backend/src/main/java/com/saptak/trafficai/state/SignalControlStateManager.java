package com.saptak.trafficai.state;

import com.saptak.trafficai.config.SignalConfig;
import com.saptak.trafficai.enums.Road;
import com.saptak.trafficai.enums.Signal;
import com.saptak.trafficai.model.signal.SignalState;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.saptak.trafficai.common.Strings.WEBSOCKET_VERSION_1;

@Component
@RequiredArgsConstructor
public class SignalControlStateManager {
    private static final Logger logger = LoggerFactory.getLogger(SignalControlStateManager.class);
    private final SignalConfig signalConfig;
    private final SimpMessagingTemplate messagingTemplate;

    private final Map<Road, Signal> signalMap = new ConcurrentHashMap<>();
    private final List<Road> roads = List.of(Road.values());

    private volatile boolean priorityMode = false;
    private volatile Road priorityRoad = null;
    private int currentIndex = 0;
    private long priorityStartTime = 0;

    @PostConstruct
    public void init() {
        // Starts with ROAD_A green, rest red
        for (Road road : roads) {
            signalMap.put(road, Signal.RED);
        }
        signalMap.put(roads.getFirst(), Signal.GREEN);
        logger.info("Initialized signal state: {}", getCurrentState());

        broadcastStateChange();
        logger.info("Signal interval: {} ms", signalConfig.getRoundRobinIntervalMs());
    }

    @Scheduled(
            fixedRateString = "${traffic-ai.signal.round-robin-interval-ms}",
            initialDelayString = "${traffic-ai.signal.round-robin-interval-ms}"
    )
    public synchronized void rotateSignalsIfNotInPriorityMode() {
        if (priorityMode) return;

        Road currentGreen = roads.get(currentIndex);
        signalMap.put(currentGreen, Signal.RED);

        currentIndex = (currentIndex + 1) % roads.size();
        Road nextGreen = roads.get(currentIndex);
        signalMap.put(nextGreen, Signal.GREEN);

        logger.info("Signal rotated: {}", getCurrentState());
        broadcastStateChange();
    }

    public synchronized void enterPriorityMode(Road road) {
        if (!priorityMode || !road.equals(priorityRoad)) {
            logger.info("🚨 Entering priority mode for {}", road);
            priorityMode = true;
            priorityRoad = road;
            priorityStartTime = System.currentTimeMillis();

            for (Road r : roads) {
                signalMap.put(r, r == road ? Signal.GREEN : Signal.RED);
            }

            logger.info("Priority signal state: {}", getCurrentState());
            broadcastStateChange();
        }
    }


    public synchronized void exitPriorityModeIfSafe(Road road) {
        if (priorityMode && road.equals(priorityRoad)) {
            logger.info("✅ Exiting priority mode for {}", road);
            priorityMode = false;
            priorityRoad = null;

            for (Road r : roads) {
                signalMap.put(r, r == roads.get(currentIndex) ? Signal.GREEN : Signal.RED);
            }

            logger.info("Resumed round-robin state: {}", getCurrentState());
            broadcastStateChange();
        }
    }

    @Scheduled(fixedRate = 1000)
    public synchronized void checkPriorityTimeout() {
        if (priorityMode && System.currentTimeMillis() - priorityStartTime >= signalConfig.getPriorityTimeoutMs()) {
            logger.info("⏱️ Priority mode timed out for {}. Reverting to round-robin.", priorityRoad);
            exitPriorityModeIfSafe(priorityRoad);
        }
    }

    private void broadcastStateChange() {
        messagingTemplate.convertAndSend(WEBSOCKET_VERSION_1 + "/traffic-signal/state", getCurrentState());
    }

    public synchronized SignalState getCurrentState() {
        return SignalState.builder()
                .signalMap(Map.copyOf(signalMap))
                .isPriorityMode(priorityMode)
                .priorityRoad(priorityRoad)
                .build();
    }
}

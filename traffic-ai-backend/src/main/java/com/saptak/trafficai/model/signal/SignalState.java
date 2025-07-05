package com.saptak.trafficai.model.signal;

import com.saptak.trafficai.enums.Road;
import com.saptak.trafficai.enums.Signal;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SignalState {
    private Map<Road, Signal> signalMap;
    private boolean isPriorityMode;
    private Road priorityRoad;
}

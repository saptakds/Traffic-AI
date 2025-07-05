export type Road = 'ROAD_A' | 'ROAD_B' | 'ROAD_C' | 'ROAD_D';

export type SignalColor = 'RED' | 'GREEN';

export interface SignalState {
  signalMap: Record<Road, SignalColor>;
  isPriorityMode: boolean;
  priorityRoad: Road | null;
}

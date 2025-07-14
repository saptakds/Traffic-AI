import React from "react";
import { observer } from "mobx-react-lite";
import { useStores } from "../stores/StoreContext";
import RoadSignal from "./RoadSignal";

const IntersectionGrid: React.FC = observer(() => {
  const { signalStore } = useStores();
  const signalMap = signalStore.signalState?.signalMap;

  if (!signalMap) {
  return (
    <div className="pt-24 text-gray-400 italic text-lg">
      Waiting for signal state...
    </div>
  );
}

  return (
    <div className="grid grid-cols-[1fr_150px_1fr] grid-rows-[1fr_150px_1fr] w-screen h-screen">
      {/* Top (Road A) */}
      <div className="col-start-2 row-start-1 h-full w-full">
        <RoadSignal
          road="ROAD_A"
          color={signalMap.ROAD_A}
          isPriority={signalStore.signalState?.priorityRoad === "ROAD_A"}
          orientation="vertical"
          stretch="vertical"
        />
      </div>

      {/* Left (Road D) */}
      <div className="col-start-1 row-start-2 h-full w-full">
        <RoadSignal
          road="ROAD_D"
          color={signalMap.ROAD_D}
          isPriority={signalStore.signalState?.priorityRoad === "ROAD_D"}
          orientation="horizontal"
          stretch="horizontal"
        />
      </div>

      {/* Center */}
      <div className="col-start-2 row-start-2 flex items-center justify-center bg-zinc-700 rounded">
        {signalStore.signalState?.isPriorityMode ? (
          <div className="text-black font-bold animate-pulse">
            🚨 {signalStore.signalState?.priorityRoad?.replace("ROAD_", "")}
          </div>
        ) : (
          <span className="text-3xl">👮</span>
        )}
      </div>

      {/* Right (Road B) */}
      <div className="col-start-3 row-start-2 h-full w-full">
        <RoadSignal
          road="ROAD_B"
          color={signalMap.ROAD_B}
          isPriority={signalStore.signalState?.priorityRoad === "ROAD_B"}
          orientation="horizontal"
          stretch="horizontal"
        />
      </div>

      {/* Bottom (Road C) */}
      <div className="col-start-2 row-start-3 h-full w-full">
        <RoadSignal
          road="ROAD_C"
          color={signalMap.ROAD_C}
          isPriority={signalStore.signalState?.priorityRoad === "ROAD_C"}
          orientation="vertical"
          stretch="vertical"
        />
      </div>
    </div>
  );
});

export default IntersectionGrid;

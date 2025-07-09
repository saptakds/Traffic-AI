import React from "react";

interface RoadSignalProps {
  road: string;
  color: "RED" | "GREEN";
  isPriority?: boolean;
  orientation?: "vertical" | "horizontal";
  stretch?: "vertical" | "horizontal";
}

const getLabelAlignment = (road: string) => {
  switch (road) {
    case "ROAD_A":
      return "items-center justify-end pr-4";
    case "ROAD_B":
      return "items-end justify-center pb-4";
    case "ROAD_C":
      return "items-center justify-start pl-4";
    case "ROAD_D":
      return "items-start justify-center pt-4";
    default:
      return "items-center justify-center";
  }
};

const RoadSignal: React.FC<RoadSignalProps> = ({
  road,
  color,
  isPriority = false,
  orientation = "vertical",
  stretch = "vertical",
}) => {
  const label = road.replace("ROAD_", "");
  const isGreen = color === "GREEN";
  const isAmbulanceDetected = isPriority && isGreen;

  return (
    <div
      className={`
        relative flex ${getLabelAlignment(road)} overflow-hidden
        ${stretch === "vertical" ? "w-full h-full" : "h-full w-full"}
        bg-zinc-800 border-4
        ${isAmbulanceDetected ? "animate-siren-border" : isGreen ? "border-green-500" : "border-red-500"}
      `}
    >
      {/* Tint Layer */}
      <div
        className={`absolute inset-0 z-0 transition-all duration-500 ease-in-out ${
          isAmbulanceDetected
            ? "animate-siren-tint"
            : isGreen
              ? "bg-green-500/10"
              : "bg-red-500/10"
        }`}
      />

      {/* Dotted Divider */}
      <div
        className={`
          absolute z-10 bg-white opacity-70
          ${
            orientation === "vertical"
              ? "w-1 h-full left-1/2 -translate-x-1/2"
              : "h-1 w-full top-1/2 -translate-y-1/2"
          }
          ${
            orientation === "vertical"
              ? "[mask-image:repeating-linear-gradient(black_0_6px,transparent_6px_12px)]"
              : "[mask-image:repeating-linear-gradient(to_right,black_0_6px,transparent_6px_12px)]"
          }
          [mask-size:100%]
        `}
      />

      {/* Label & Icon */}
      <div className="z-20 flex items-center gap-2 text-white text-6xl font-extrabold opacity-40 select-none">
        <span>{label}</span>
        {isAmbulanceDetected && <span className="text-5xl">🚑</span>}
      </div>
    </div>
  );
};

export default RoadSignal;

import React from 'react';

interface RoadSignalProps {
  road: string;
  color: 'RED' | 'GREEN';
}

const RoadSignal: React.FC<RoadSignalProps> = ({ road, color }) => {
  const emoji = color === 'GREEN' ? '🟢' : '🔴';
  const label = road.replace('ROAD_', 'Road ');

  return (
    <div className="flex flex-col items-center justify-center w-full h-full bg-gray-700 rounded text-sm font-medium">
      <span className="text-xl">{emoji}</span>
      <span>{label}</span>
    </div>
  );
};



export default RoadSignal;

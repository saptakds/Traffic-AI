import React from 'react';
import { observer } from 'mobx-react-lite';
import { useStores } from '../stores/StoreContext';
import RoadSignal from './RoadSignal';

const Placeholder = () => (
  <div className="w-full h-full bg-transparent" />
);

const IntersectionGrid: React.FC = observer(() => {
  const { signalStore } = useStores();
  const signalMap = signalStore.signalState?.signalMap;

  if (!signalMap) {
    return <p className="text-gray-400 italic">Waiting for signal state...</p>;
  }

  return (
    <div className="grid grid-cols-3 grid-rows-3 w-[300px] h-[300px] gap-2 bg-gray-800 text-white rounded-md shadow-lg p-2">
      {/* Row 1 */}
      <Placeholder />
      <RoadSignal road="ROAD_A" color={signalMap.ROAD_A} />
      <Placeholder />

      {/* Row 2 */}
      <RoadSignal road="ROAD_D" color={signalMap.ROAD_D} />
      <div className="flex flex-col items-center justify-center w-full h-full rounded text-7xl font-medium">👮</div>
      <RoadSignal road="ROAD_B" color={signalMap.ROAD_B} />

      {/* Row 3 */}
      <Placeholder />
      <RoadSignal road="ROAD_C" color={signalMap.ROAD_C} />
      <Placeholder />
    </div>
  );
});

export default IntersectionGrid;

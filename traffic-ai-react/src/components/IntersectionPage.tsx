import React from 'react';
import { observer } from 'mobx-react-lite';
import { useSignalState } from '../hooks/useSignalState';
import IntersectionGrid from './IntersectionGrid';

const IntersectionPage: React.FC = observer(() => {
  useSignalState(); // WebSocket subscription

  return (
    <div className="min-h-screen w-full flex flex-col items-center justify-center space-y-6 bg-white dark:bg-zinc-900 text-zinc-900 dark:text-white transition-colors duration-300">
      <h1 className="text-3xl font-bold">🚦 Traffic AI Simulation</h1>
      <IntersectionGrid />
    </div>
  );
});

export default IntersectionPage;

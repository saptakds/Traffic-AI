import React from 'react';
import { useSignalState } from '../hooks/useSignalState';

const IntersectionPage: React.FC = () => {
  const signalState = useSignalState();

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">🚦 Traffic AI Simulation</h1>

      {signalState ? (
        <div className="bg-gray-800 p-4 rounded text-left text-sm font-mono text-green-300 whitespace-pre">
          <strong>📡 Real-Time Signal State:</strong>
          <br />
          {JSON.stringify(signalState, null, 2)}
        </div>
      ) : (
        <p className="text-gray-400 italic">Waiting for signal updates...</p>
      )}
    </div>
  );
};

export default IntersectionPage;

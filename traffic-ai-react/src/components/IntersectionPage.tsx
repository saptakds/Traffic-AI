import React, { useState, useEffect } from "react";
import { observer } from "mobx-react-lite";
import { useSignalState } from "../hooks/useSignalState";
import IntersectionGrid from "./IntersectionGrid";
import SignalTimer from "./SignalTimer";

const IntersectionPage: React.FC = observer(() => {
  const { signalState } = useSignalState();
  const [hasSignalArrived, setHasSignalArrived] = useState(false);

  useEffect(() => {
    if (signalState && !hasSignalArrived) {
      setHasSignalArrived(true);
    }
  }, [signalState, hasSignalArrived]);

  return (
    <div className="min-h-screen w-full flex flex-col bg-zinc-900 text-white relative overflow-hidden">
      {/* Title - animates from center to top-left */}
      <div
        className={`fixed z-50 transition-all duration-700 ease-in-out text-xl font-bold flex items-center gap-2
          ${hasSignalArrived ? "top-4 left-4" : "top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-3xl"}`}
      >
        <span>🚦</span> <span>Traffic AI Simulation</span>
      </div>

      {hasSignalArrived && <SignalTimer />}

      {/* Grid */}
      <div className="flex flex-1 items-center justify-center">
        <IntersectionGrid />
      </div>
    </div>
  );
});

export default IntersectionPage;

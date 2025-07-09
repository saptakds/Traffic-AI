import React from "react";
import { observer } from "mobx-react-lite";
import { useSignalState } from "../hooks/useSignalState";
import IntersectionGrid from "./IntersectionGrid";

const IntersectionPage: React.FC = observer(() => {
  useSignalState();

  return (
    <div className="min-h-screen w-full flex flex-col bg-zinc-900 text-white relative">
      {/* Title pinned to top-left */}
      <div className="absolute top-4 left-4 text-xl font-bold flex items-center gap-2">
        <span>🚦</span> <span>Traffic AI Simulation</span>
      </div>

      {/* Centered intersection */}
      <div className="flex flex-1 items-center justify-center">
        <IntersectionGrid />
      </div>
    </div>
  );
});

export default IntersectionPage;

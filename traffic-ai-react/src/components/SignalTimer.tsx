import React, { useEffect, useState, useRef } from "react";
import { observer } from "mobx-react-lite";
import { useStores } from "../stores/StoreContext";
import { reaction } from "mobx";

const SignalTimer: React.FC = observer(() => {
  const { signalStore } = useStores();
  const [secondsLeft, setSecondsLeft] = useState<number>(0);
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);
  const disposerRef = useRef<(() => void) | null>(null);

  const resetTimer = (intervalSec: number) => {
    setSecondsLeft(intervalSec);
    if (intervalRef.current) clearInterval(intervalRef.current);
    intervalRef.current = setInterval(() => {
      setSecondsLeft((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
  };

  useEffect(() => {
    const initial = signalStore.signalState;
    if (initial) resetTimer(initial.intervalSec);

    disposerRef.current = reaction(
      () => {
        const state = signalStore.signalState;
        if (!state) return null;
        const activeRoad = Object.entries(state.signalMap).find(
          ([, color]) => color === "GREEN"
        )?.[0];
        return `${state.isPriorityMode ? "P" : "R"}:${activeRoad}`;
      },
      () => {
        const intervalSec = signalStore.signalState?.intervalSec ?? 0;
        resetTimer(intervalSec);
      }
    );

    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
      if (disposerRef.current) disposerRef.current();
    };
  }, [signalStore]);

  const formatTime = (totalSeconds: number) => {
    const mins = Math.floor(totalSeconds / 60);
    const secs = totalSeconds % 60;
    return `${String(mins).padStart(2, "0")}:${String(secs).padStart(2, "0")}`;
  };

  const isCritical = secondsLeft <= 10;

  return (
    <div
      className={`absolute top-4 right-4 px-6 py-3 rounded-full shadow-md text-3xl font-mono tracking-wider font-bold
        backdrop-blur-md border transition-all duration-300 ease-in-out
        ${isCritical ? "text-red-500 border-yellow-500" : "text-green-400 border-green-500"}
      `}
    >
      ⏱️{" "}
      <span className={isCritical ? "animate-blink" : ""}>
        {formatTime(secondsLeft)}
      </span>
    </div>
  );
});

export default SignalTimer;
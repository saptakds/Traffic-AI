import { makeAutoObservable } from 'mobx';
import type { SignalState } from '../types/SignalState';

export class SignalStore {
  signalState: SignalState | null = null;

  constructor() {
    makeAutoObservable(this);
  }

  updateSignalState(state: SignalState) {
    this.signalState = state;
  }
}

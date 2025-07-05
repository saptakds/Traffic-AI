import { ConfigStore } from './ConfigStore';
import { SignalStore } from './SignalStore';

export class RootStore {
  configStore: ConfigStore;
  signalStore: SignalStore;

  constructor() {
    this.configStore = new ConfigStore();
    this.signalStore = new SignalStore();
  }
}

export const rootStore = new RootStore();

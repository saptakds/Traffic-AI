import { makeAutoObservable } from 'mobx';

interface Config {
  webSocketBaseUrl: string;
  backEndBaseUrl: string;
  topics: {
    baseUrl: string;
    signal: string;
  };
  featureFlags: {
    roads: boolean;
    signal: boolean;
  };
}

export class ConfigStore {
  config: Config | null = null;
  isLoading = true;
  isInitialized = false;

  constructor() {
    makeAutoObservable(this);
  }

  async loadConfig() {
    try {
      const res = await fetch('/config.json');
      if (!res.ok) throw new Error('Failed to load config.json');

      const data = await res.json();
      this.setConfig(data);
      this.setInitialized(true);
      this.loaded();
      console.log('✅ Config loaded:', data);
    } catch (error) {
      console.error('❌ Error loading config:', error);
      this.unload();
    }
  }

  setConfig(config: Config) {
    this.config = config;
  }

  loaded() {
    this.isLoading = false;
  }

  unload() {
    this.isLoading = true;
  }

  setInitialized(value: boolean) {
    this.isInitialized = value;
  }
}

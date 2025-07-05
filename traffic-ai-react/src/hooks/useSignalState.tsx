import { useEffect, useRef } from 'react';
import type { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { Client as StompClient } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

import type { SignalState } from '../types/SignalState';
import { useStores } from '../stores/StoreContext';

export function useSignalState() {
  const { configStore, signalStore } = useStores();
  const clientRef = useRef<Client | null>(null);
  const subscriptionRef = useRef<StompSubscription | null>(null);

  useEffect(() => {
    if (!configStore.isInitialized || !configStore.config) {
      console.warn('Config not ready yet. Skipping WebSocket init.');
      return;
    }

    const wsUrl = configStore.config.webSocketBaseUrl;
    const topic = configStore.config.topics.baseUrl + configStore.config.topics.signal;

    const client: Client = new StompClient({
      webSocketFactory: () => new SockJS(wsUrl),
      reconnectDelay: 5000,
      onConnect: () => {
        subscriptionRef.current = client.subscribe(topic, (message: IMessage) => {
          try {
            console.log('📨 WebSocket message received:', message.body);
            const body: SignalState = JSON.parse(message.body);
            signalStore.updateSignalState(body);
          } catch (error) {
            console.error('Failed to parse signal state:', error);
          }
        });
      },
      onStompError: (frame) => {
        console.error('Broker error:', frame.headers['message']);
        console.error('Details:', frame.body);
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      subscriptionRef.current?.unsubscribe();
      client.deactivate();
    };
  }, [configStore.isInitialized, configStore.config, signalStore]);

  return signalStore.signalState;
}

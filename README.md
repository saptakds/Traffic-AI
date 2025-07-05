# 🚦 TrafficAI: AI-Based Ambulance Prioritization at Traffic Signals

> **Smart cities need smarter signals.**
> TrafficAI is a prototype system that uses video footage from traffic signal cameras to detect ambulances in real-time, enabling automated traffic signal prioritization at intersections.

---

## 🔍 The Problem

Emergency vehicles like ambulances often lose critical time waiting in traffic.
Traditional traffic signal systems aren’t equipped to detect such vehicles or dynamically reprioritize flow during emergencies — especially in urban congestion.

---

## 💡 The Vision

TrafficAI proposes an AI-first, automation-friendly solution:

* Leverage existing CCTV infrastructure at intersections
* Detect ambulances using cloud-based computer vision
* Dynamically alter signal flow to prioritize emergency vehicles

---

## ✅ Current Capabilities (as of Day 5)

### 🔼 Video Upload & Ingestion

* REST API to upload traffic footage with camera ID
* Enum-based camera identifiers (`CAM_A` to `CAM_D`)
* Swagger UI for quick testing
* Multipart support with 100MB limit

### 🎥 Frame Extraction & Processing

* Frames extracted every 5 seconds using JavaCV
* Async processing ensures non-blocking operations
* Temporary files auto-cleaned post-processing

### 🧠 Ambulance Detection (AI)

* Integrated with Azure Custom Vision
* Supports **decoy mode** for safe testing
* Logs ambulance detection results with confidence
* Maps each camera to a road (`ROAD_A` to `ROAD_D`)

### 🚦 Traffic Signal Control Logic

* Round-robin switching every 10 seconds (configurable)
* **Priority mode** for ambulance-detected roads
* Priority persists for 30 seconds (configurable), then reverts
* Logs state transitions with clarity and timestamps

### 📡 Real-Time WebSocket Integration

* Signal state updates published to: `/topic/v1/traffic-signal/state`
* WebSocket endpoint: `/ws`, powered by STOMP over SockJS
* Confirmed working with a plain HTML + SockJS client

### 🧩 New in Day 5 — Frontend Visualization with React

TrafficAI now includes a **React 19** frontend to **visually simulate the 4-road intersection** and its signal states in real time.

* 💻 **Built with Vite + React + TypeScript**
* 🧠 State managed using **MobX** with root store pattern
* ⚙️ Config-driven architecture via `public/config.json`
* 🔄 Connects to backend WebSocket and reflects live signal state updates
* ✅ Built using modular, production-grade best practices
* 📜 Displays real-time signal state logs in a clean UI panel
* 🚧 Road-based signal light visualizations coming soon in Day 6

---

## 🖥️ Frontend Architecture

```text
traffic-ai-react/
├── public/
│   └── config.json              # Runtime-configurable URLs and feature flags
├── src/
│   ├── components/
│   │   └── IntersectionPage.tsx  # Core UI (logs + upcoming signal visuals)
│   ├── hooks/
│   │   └── useSignalState.ts     # WebSocket logic + state binding
│   ├── stores/
│   │   ├── ConfigStore.ts
│   │   ├── SignalStore.ts
│   │   ├── RootStore.ts
│   │   └── StoreContext.ts
│   └── types/
│       └── SignalState.ts        # Strongly typed signal state model
```

---

## 📂 API Endpoint

**POST** `/traffic/backend/api/v1/video/upload`
Upload traffic footage with camera identifier.

**Request Parameters:**

* `videoFile` (MultipartFile) – Video clip to analyze
* `camera` (Enum) – One of `CAM_A`, `CAM_B`, `CAM_C`, `CAM_D`

**Returns:**
Metadata + success message

---

## 🧠 Signal State Model

```ts
type Road = 'ROAD_A' | 'ROAD_B' | 'ROAD_C' | 'ROAD_D';
type SignalColor = 'RED' | 'GREEN';

interface SignalState {
  signalMap: Record<Road, SignalColor>;
  isPriorityMode: boolean;
  priorityRoad: Road | null;
}
```

---

## 🛠️ Config (Backend `application.yml`)

```yaml
traffic-ai:
  signal:
    round-robin-interval-ms: 10000
    priority-timeout-ms: 30000
  frame-extraction:
    interval-seconds: 5

custom-vision:
  prediction:
    decoy-mode: true
    key: ${CV_PREDICTION_KEY}
    endpoint: ${CV_PREDICTION_ENDPOINT}
    project-id: ${CV_PREDICTION_PROJECT_ID}
    published-name: ${CV_PREDICTION_PUBLISHED_NAME}
    prediction-threshold: 0.7
```

---

## ⚙️ Config (Frontend `public/config.json`)

```json
{
  "webSocketBaseUrl": "http://localhost:8081/ws",
  "backEndBaseUrl": "http://localhost:8081/traffic/backend/api/v1",
  "topics": {
    "baseUrl": "/topic/v1",
    "signal": "/traffic-signal/state"
  },
  "featureFlags": {
    "roads": true,
    "signal": true
  }
}
```

---

## 🧭 Roadmap

* 🟢 **\[Next]** Render live 4-road signal intersection with colored signals
* 🚨 Blink ambulance icons and priority roads during priority mode
* 📝 Add visual countdown timers per signal
* 🔍 Camera activity and mock prediction feed simulation

---

## 👨‍💻 Contributors

Built with ❤️ by [Saptak Das](https://github.com/saptakds)

Exploring practical AI for smarter cities and public safety.
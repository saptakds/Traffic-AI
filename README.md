# 🚦 TrafficAI: AI-Based Ambulance Prioritization at Traffic Signals

> **Smart cities need smarter signals.**
> **TrafficAI** is a prototype system that uses video footage from traffic signal cameras to detect ambulances in real-time, enabling automated traffic signal prioritization at intersections.

---

## 🔍 The Problem

Emergency vehicles like ambulances often lose critical time waiting in traffic.
Traditional traffic signal systems aren’t equipped to detect such vehicles or dynamically reprioritize flow during emergencies — especially in urban congestion.

---

## 💡 The Vision

TrafficAI proposes an AI-first, automation-friendly solution:

* ✅ Leverage existing CCTV infrastructure at intersections
* ✅ Detect ambulances using cloud-based computer vision
* ✅ Dynamically alter signal flow to prioritize emergency vehicles

---

## ✅ Current Capabilities (as of **Day 6**)

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

* Integrated with **Azure Custom Vision**
* Supports **decoy mode** for safe testing
* Logs ambulance detection results with confidence
* Maps each camera to a road (`ROAD_A` to `ROAD_D`)

### 🚦 Traffic Signal Control Logic

* Round-robin switching every 10 seconds (**configurable**)
* **Priority mode** for ambulance-detected roads
* Priority persists for 30 seconds (**configurable**) then reverts
* Logs signal state transitions with timestamps and clarity

### 📡 Real-Time WebSocket Integration

* Signal state updates published to: `/topic/v1/traffic-signal/state`
* WebSocket endpoint: `/ws`, powered by **STOMP over SockJS**
* Live tested with both React UI and raw SockJS clients

---

## 💻 Frontend Simulation UI — **Ambulance Priority Now Visualized!**

TrafficAI now features a **visually enhanced React frontend** that reflects both **round-robin** and **ambulance-priority** traffic modes in real time.

**New Features Completed in Day 6:**

* 🔴🟢 **Tinted road background** based on signal state (subtle red/green glow)
* 🚑 **Ambulance icon overlay** for priority road
* 🎆 **Siren-style animation**:

  * 🔁 Alternating **red/blue border**
  * 💡 Blinking **green tint** layer during ambulance mode
* 🧩 Componentized and responsive **3×3 intersection grid layout**
* ✅ All visuals updated via real-time **WebSocket state**
* 🎨 Fully styled with **Tailwind CSS** and custom animations

---

### 🌐 Intersection UI Behavior

| Feature              | Round-Robin Mode | Ambulance-Priority Mode |
| -------------------- | ---------------- | ----------------------- |
| Road Border Color    | Green / Red      | Blinks Red/Blue         |
| Tint Background      | Subtle Green/Red | Pulsing Green Tint      |
| Ambulance Emoji (🚑) | ❌                | ✅ Appears on road       |
| Center Icon          | 👮               | 🚨 + Road name          |
| Divider Line         | Dotted white     | Dotted white            |

---

## 🖥️ Frontend Architecture

```
traffic-ai-react/
├── public/
│   └── config.json              # Runtime-configurable URLs and feature flags
├── src/
│   ├── components/
│   │   ├── RoadSignal.tsx        # Visual unit per road
│   │   ├── IntersectionGrid.tsx  # Lays out roads in 3×3 grid
│   │   └── IntersectionPage.tsx  # Top-level wrapper
│   ├── hooks/
│   │   └── useSignalState.ts     # WebSocket connection + store updates
│   ├── stores/                   # MobX stores
│   │   ├── ConfigStore.ts
│   │   ├── SignalStore.ts
│   │   └── RootStore.ts
│   ├── types/
│   │   └── SignalState.ts        # Signal structure types
│   └── index.css                 # Tailwind + custom styles
```

---

## 📂 API Endpoint

**POST** `/traffic/backend/api/v1/video/upload`
Upload traffic footage with camera identifier.

**Request Parameters:**

* `videoFile` (MultipartFile) – Video clip to analyze
* `camera` (Enum) – One of `CAM_A`, `CAM_B`, `CAM_C`, `CAM_D`

**Returns:**
JSON metadata + success message

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

## ⚙️ Backend Config (`application.yml`)

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

## ⚙️ Frontend Config (`public/config.json`)

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

* 🔁 Animate ambulance emoji (bounce/flash)
* 📱 Mobile responsiveness polish
* ⏱️ Show signal change countdowns
* 🎦 Simulated CCTV stream thumbnails (mock)
* 🧪 Add unit test coverage for frontend

---

## 👨‍💻 Contributors

Built with ❤️ by [Saptak Das](https://github.com/saptakds)

> Exploring practical AI for smarter cities and public safety.
# 🚦 TrafficAI: AI-Based Ambulance Prioritization at Traffic Signals

> **Smart cities need smarter signals.**
> TrafficAI is a prototype system that uses video footage from traffic signal cameras to detect ambulances in real-time, enabling automated traffic signal prioritization at intersections.

---

## 🔍 The Problem

Emergency vehicles like ambulances often lose critical time waiting in traffic.
Traditional traffic signal systems are not equipped to detect such vehicles or dynamically reprioritize signal flow during emergencies — especially under peak congestion.

---

## 💡 The Vision

TrafficAI proposes an AI-first, automation-friendly solution:

* Leverage existing CCTV infrastructure at traffic intersections.
* Detect ambulances using cloud-based computer vision models.
* Modify signal timing to prioritize emergency vehicle paths.

---

## ✅ Current Capabilities (as of Day 4)

### 🔼 Video Upload & Ingestion

* REST API to upload traffic footage with camera ID
* Enum-based camera identifiers (`CAM_A` to `CAM_D`)
* Swagger UI for quick testing and validation
* Multipart support with a 100MB upload limit

### 🎥 Frame Extraction & Processing

* Frames are extracted every 5 seconds from uploaded videos
* Powered by JavaCV (FFmpegFrameGrabber)
* Async processing using `@Async` avoids blocking I/O
* Temporary files are auto-cleaned post-processing

### 🧠 Ambulance Detection (AI)

* Integrated with Azure Custom Vision for inference
* Decoy mode for testing without hitting API limits
* Configurable prediction threshold via `application.yml`
* Logs ambulance detection confidence scores
* Maps each camera to a road using enums

### 🚦 Traffic Signal Control Logic

* Round-robin switching across four roads (`ROAD_A` to `ROAD_D`)
* Signal state updates every 10 seconds (configurable)
* Ambulance detection triggers **priority mode** for affected road
* Priority mode overrides rotation for 30s (configurable)
* After timeout, system gracefully reverts to round-robin
* Logs signal changes and transitions clearly

### 📡 Real-Time WebSocket Integration

* Signal state changes are broadcast in real-time via STOMP over WebSocket
* Lightweight test client using SockJS + StompJS for live monitoring
* Endpoint: `/ws`, Topic: `/topic/v1/traffic-signal/state`
* Enables real-time visual feedback for future frontend simulator

### ⚙️ Tech Stack

* **Spring Boot 3.5.3** (Java 21)
* **Azure Custom Vision** for ambulance classification
* **JavaCV (FFmpeg)** for frame extraction
* **Spring WebClient** for non-blocking HTTP calls
* **Spring Scheduling & Async** for timed signal control
* **Spring WebSocket (STOMP)** for live broadcasting
* **Springdoc OpenAPI** for Swagger documentation

---

## 📂 API Endpoint

**POST** `/traffic/backend/api/v1/video/upload`
Upload traffic footage with the camera identifier.

**Request Parameters:**

* `videoFile` (MultipartFile) – video clip to analyze
* `camera` (Enum) – one of `CAM_A`, `CAM_B`, `CAM_C`, `CAM_D`

**Returns:**

* File metadata and success message

---

## 🔧 Configuration Highlights

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

## 🧭 Roadmap

* Return AI prediction results in the upload response
* Build a frontend simulator using React + TypeScript
* Visualize signal state updates in real-time via WebSocket
* Persist signal transitions for audit & analytics
* Add unit and integration tests for detection + state logic

---

## 👨‍💻 Contributors

Built with ❤️ by [Saptak Das](https://github.com/saptakds)

Exploring practical AI for smart infrastructure.
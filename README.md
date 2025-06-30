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

- Leverage existing CCTV infrastructure at traffic intersections.
- Detect ambulances using cloud-based computer vision models.
- Modify signal timing to prioritize emergency vehicle paths.

---

## ✅ Current Capabilities

### 🔼 Video Upload & Ingestion

- REST API to upload traffic footage with camera ID
- Enum-based camera identifiers (`CAM_A` to `CAM_D`)
- Swagger UI for quick testing and validation
- Multipart support with a 100MB upload limit

### 🎥 Frame Extraction & Processing

- Frames are extracted every 5 seconds from uploaded videos
- Async processing enabled using `@Async` to avoid blocking I/O
- Temporary files are cleaned up post-processing

### 🧠 Ambulance Detection (AI)

- Integrated with Azure Custom Vision for inference
- Configurable prediction threshold via `application.yml`
- Logs confidence scores for each frame's prediction
- Decoupled service layer for easy extension/replacement

### ⚙️ Tech Stack

- **Spring Boot 3.5.3** (Java 21)
- **Azure Custom Vision** for ambulance classification
- **JavaCV (FFmpeg)** for frame extraction
- **WebClient (Spring WebFlux)** for non-blocking API calls
- **Springdoc OpenAPI** for Swagger documentation

---

## 📂 API Endpoint

**POST** `/traffic/backend/api/v1/video/upload`  
Upload traffic footage with the camera identifier.

**Request Parameters:**
- `videoFile` (MultipartFile) – video clip to analyze
- `camera` (Enum) – one of `CAM_A`, `CAM_B`, `CAM_C`, `CAM_D`

**Returns:**
- File metadata and success message

---

## 🔧 Configuration Highlights

```yaml
traffic-ai:
  frame-extraction:
    interval-seconds: 5

custom-vision:
  prediction:
    key: ${CV_PREDICTION_KEY}
    endpoint: ${CV_PREDICTION_ENDPOINT}
    project-id: ${CV_PREDICTION_PROJECT_ID}
    published-name: ${CV_PREDICTION_PUBLISHED_NAME}
    prediction-threshold: 0.7
````

---

## 🚦 Roadmap

* Store and return prediction results to client
* Add video-to-signal decision simulator
* Integrate historical decision logging for analytics
* Write unit + integration tests for prediction workflow

---

## 👨‍💻 Contributors

Built with ❤️ by [Saptak Das](https://github.com/saptakds)

Exploring practical AI for smart infrastructure.
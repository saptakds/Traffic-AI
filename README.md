# 🚦 TrafficAI: AI-Based Ambulance Prioritization at Traffic Signals

> **Smart cities need smarter signals.**
> TrafficAI is a prototype system that uses video footage from traffic signal cameras to detect ambulances in real-time, enabling automated traffic signal prioritization.

---

## 🔍 The Problem

Emergency vehicles like ambulances often lose critical time waiting in traffic. Human-operated traffic signals are not equipped to detect such vehicles or dynamically prioritize them, especially during peak hours.

---

## 💡 Our Proposal

TrafficAI proposes an AI-first approach:

* Use existing CCTV footage at traffic intersections.
* Detect ambulances using computer vision.
* Relay signal priority decisions to the traffic control system.

This not only improves emergency response time but also sets the foundation for scalable smart city integrations.

---

## ⚙️ Current Capabilities (Day 1)

✅ **Video Upload via Swagger**
Users can upload traffic footage through a REST endpoint with:

* File upload (video format)
* Camera source (enum dropdown)

✅ **Backend Stack**

* Spring Boot (v3+)
* Multipart upload support
* Swagger/OpenAPI 3 (via Springdoc)
* Custom logging and exception handling

✅ **Validation & Configs**

* 100MB video upload cap (configurable)
* Enum-based camera identifiers (CAM\_A to CAM\_D)

---

## 🛣️ Next Steps

* Integrate real-time object detection
* Return detection results in `VideoResponse`
* Optionally plug into a simulated signal control system

---

## 📂 API Preview

* **POST** `/traffic/backend/api/v1/video/upload`
  Upload video and specify camera. Returns metadata about the file.

---

## 👨‍💻 Contributors

Built with ❤️ by [Saptak Das](https://github.com/saptakds)
Exploring practical AI for smart infrastructure.
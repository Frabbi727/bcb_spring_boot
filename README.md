# BCB Player Onboarding Backend

This repository houses the BCB Player Onboarding Backend service built with Java 17 and Spring Boot 4.1.0. It supports dynamic player registrations, OTP authentication, coach reviews, admin oversight, and is fully configured to run as a Model Context Protocol (MCP) server over stdio.

---

## 📋 Table of Contents
1. [Prerequisites](#-prerequisites)
2. [Quick Start (Local Development)](#-quick-start-local-development)
3. [Running via Docker & Docker Compose](#-running-via-docker--docker-compose)
4. [Running as an MCP Server](#-running-as-an-mcp-server)
5. [Testing the APIs](#-testing-the-apis)
    - [Authentication flow](#1-authentication-flow)
    - [Player Registration flow](#2-player-registration-flow)

---

## 🛠️ Prerequisites
*   **Java Development Kit (JDK):** Version 17
*   **Docker:** Installed and running (required if running containerized)

---

## ⚡ Quick Start (Local Development)
To run the server directly on your host machine (exposes the REST API on port `8080`):

### 1. Build the application
```bash
./gradlew clean build
```

### 2. Run the application
```bash
./gradlew bootRun
```
The REST API will be available at `http://localhost:8080`.

---

## 🐳 Running via Docker & Docker Compose
To run containerized without needing local Java or Gradle installations:

### 1. Build and Start Container
```bash
docker-compose up --build
```
This automatically compiles the project inside a multi-stage Docker environment and starts the server on port `8080`.

### 2. Stop Container
```bash
docker-compose down
```

---

## 🤖 Running as an MCP Server
The backend is configured to work as an MCP server using Stdio transport. To activate this, the `mcp` Spring profile must be enabled.

### 1. Compile the JAR file
```bash
./gradlew bootJar
```

### 2. Launch the MCP Server via Stdio
```bash
java -jar build/libs/bcb_player_onboarding_backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=mcp
```
*(Note: In MCP mode, console logs are silenced and the web server is disabled to keep stdout clean for JSON-RPC messages).*

### 3. Agent TUI Configuration
Configure your AI agent client settings (`.agents/config/mcp_config.json`):
```json
{
  "mcpServers": {
    "onboarding-mcp-server": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/bcb_player_onboarding_backend/build/libs/bcb_player_onboarding_backend-0.0.1-SNAPSHOT.jar",
        "--spring.profiles.active=mcp"
      ]
    }
  }
}
```

---

## 🧪 Testing the APIs

Here is how you can test the primary API flows using `curl` or any API client.

### 1. Authentication Flow
Authentication uses simulated OTP verification. Use OTP code `123456` for registrations, and `654321` or `123456` for logins.

#### Send Registration OTP
```bash
curl -X POST http://localhost:8080/auth/register/otp/send \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber": "+8801711112233"}'
```
*(Copy the returned `sessionId` from the response).*

#### Verify Registration OTP
```bash
curl -X POST http://localhost:8080/auth/register/otp/verify \
  -H "Content-Type: application/json" \
  -d '{"sessionId": "<SESSION_ID>", "phoneNumber": "+8801711112233", "otp": "123456"}'
```
*(Copy the returned `registrationToken` from the response to use as the Bearer token in the player flow).*

---

### 2. Player Registration Flow
Use the `registrationToken` in the `Authorization` header to save step-by-step draft data.

#### Save Personal Info
```bash
curl -X PUT http://localhost:8080/player/registration/personal \
  -H "Authorization: Bearer <REGISTRATION_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "nameEn": "Fazle Rabbi",
    "nameBn": "ফজলে রাব্বি",
    "dob": "2010-05-15T00:00:00.000Z",
    "birthRegistrationNumber": "20102692518104523"
  }'
```

#### Submit Draft Registration
```bash
curl -X POST http://localhost:8080/player/registration/submit \
  -H "Authorization: Bearer <REGISTRATION_TOKEN>"
```

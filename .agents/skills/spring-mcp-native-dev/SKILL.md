---
name: spring-mcp-native-dev
description: "Assists with building, testing, and troubleshooting the Spring Boot 4.x application, testing the Stdio MCP server, and ensuring GraalVM Native Image compatibility."
---

# Spring Boot MCP & GraalVM Native Development Guide

This skill provides guides and instructions for developing, testing, and packaging the BCB Player Onboarding Backend service.

---

## 1. Development & Build Workflows

Always use the Gradle Wrapper (`./gradlew`) for build and execution tasks:

- **Build and Assembly:** `./gradlew clean build`
- **Fast Build (Skip Tests):** `./gradlew build -x test`
- **Run the Application locally:** `./gradlew bootRun`
- **Run JUnit Tests:** `./gradlew test`

---

## 2. Testing and Troubleshooting the Stdio MCP Server

Since this application is configured to run as an MCP server using the Standard Input/Output (Stdio) transport protocol, you must adhere to these strict process constraints:

### Avoid Stdout Pollution
- **No Console Logs:** Any application logging or print statements directed to `stdout` will corrupt the JSON-RPC message stream, causing the client/agent connection to disconnect.
- **Redirecting Logs:** Ensure all logs are routed to `stderr` or a file. The console log pattern must remain empty (`logging.pattern.console=`).
- **Startup Banner:** Ensure `spring.main.banner-mode=off` is active so the Spring Boot ASCII art is not written to `stdout`.

### Verifying Tool Registration
To verify that your tools are registered correctly:
1. Run `./gradlew bootJar` to compile the latest code.
2. Launch the jar locally from the command line:
   ```bash
   java -jar build/libs/bcb_player_onboarding_backend-0.0.1-SNAPSHOT.jar
   ```
3. Send a test JSON-RPC initialization request via `stdin` to inspect the available tools.

---

## 3. GraalVM AOT & Native Image Guidelines

This project supports compiling to a GraalVM native binary. When editing or adding Java code, ensure compatibility with Ahead-of-Time (AOT) compilation:

### Key Constraints
1. **Dynamic Reflection:** Avoid Java Reflection API (`Class.forName()`, `Method.invoke()`) without registering metadata, as the compiler discards unused classes.
2. **Dynamic Proxies:** Avoid runtime dynamic proxy creation.
3. **Classpath Resources:** Reading files dynamically from the classpath at runtime requires registration in resource configuration files.

### Commands for Native Compilation
- **Run Tests in Native Mode:**
  ```bash
  ./gradlew nativeTest
  ```
- **Compile to Native Executable:**
  ```bash
  ./gradlew nativeCompile
  ```
  The compiled standalone executable will be generated at `build/native/nativeCompile/bcb_player_onboarding_backend`.

---

## 4. Onboarding Domain Business Rules

When implementing or extending player onboarding logic, enforce these domain rules:

- **Age Limits:** Players must be between **18 and 40** years of age (inclusive) to be eligible.
- **Medical Fitness:** A mandatory fitness check must be passed before registration status can be activated.
- **Player Roles:** Valid positions include `Batsman`, `Bowler`, `Wicketkeeper`, and `All-rounder`.

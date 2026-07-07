---
trigger: always_on
---
# Workspace Rules: General Guidelines

This rules file applies to all interactions within the `bcb_player_onboarding_backend` repository.

## Project Context
- **Name:** BCB Player Onboarding Backend (`bcb_player_onboarding_backend`).
- **Domain:** Player onboarding and management backend service.
- **Tech Stack:** Java 17, Spring Boot 4.1.0, Lombok, Gradle, GraalVM Native Image support.

## Core Directives

### 1. Build and Run Commands
- Always use the Gradle Wrapper (`./gradlew`) for executing gradle tasks (never use raw `gradle` command).
- Key tasks:
  - Clean and build: `./gradlew clean build`
  - Run the application: `./gradlew bootRun`
  - Build native executable: `./gradlew nativeCompile`
  - Run tests: `./gradlew test`

### 2. Spring Boot 4.x & Java 17 Conventions
- Keep code clean, modern, and aligned with Java 17 features (e.g., Records, Text Blocks, Switch expressions, pattern matching).
- Leverage modern Spring Boot structures and dependencies properly.

### 3. File & Symbol Links
- When referencing files in chat or comments, use clickable markdown links (e.g., `[build.gradle](file:///path/to/build.gradle)`).
- When mentioning classes/symbols, format them appropriately.

### 4. Git Commit Messages
- Follow clean semantic commit conventions (e.g., `feat:`, `fix:`, `refactor:`, `test:`, `chore:`).

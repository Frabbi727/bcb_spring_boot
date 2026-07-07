---
trigger: glob
globs: ["src/**/*.java"]
description: "Guidelines and conventions for writing Java source code in this project."
---
# Java & Spring Boot Development Rules

This rule is applied automatically to all Java files within the project.

## Code Style & Language Features
- **Java Version:** Target Java 17. Use modern features when applicable:
  - **Records:** Use records for simple immutable data transfer objects (DTOs) and request/response payloads.
  - **Text Blocks:** Use text blocks (`""" ... """`) for multi-line strings, SQL queries, or JSON strings.
  - **Pattern Matching:** Use pattern matching for `instanceof` checks.
- **Null Safety:** Avoid returning `null` where possible; prefer `Optional` for values that may be absent.

## Spring Boot & Lombok Best Practices
- **Dependency Injection:** Use constructor injection instead of `@Autowired` fields.
  - Apply `@RequiredArgsConstructor` from Lombok on classes (such as controllers, services, repositories) to automatically generate the constructor for `final` fields.
- **Lombok Usage:**
  - Prefer `@Getter` and `@Setter` over `@Data` on complex domain models or entities to prevent performance issues and circular dependencies in `toString()` or `hashCode()`.
  - Use `@Slf4j` for logging. Do not instantiate loggers manually.

## GraalVM Native Image Compatibility
Since this project is configured for GraalVM Native Images (using Native Build Tools):
- **Avoid Dynamic Reflection:** Do not use runtime reflection, dynamic class loading, or dynamic proxies unless registered in the reachability metadata.
- **Spring AOT Friendly:** Write code that conforms to Spring's Ahead-of-Time (AOT) compiler constraints. Avoid runtime bean registration or conditional bean loading that cannot be evaluated at build time.

# BCB Player Onboarding Backend

This is the workspace root for the BCB Player Onboarding Backend service.

Workspace-scoped agent guidelines and constraints are configured in the [.agents/rules/](file:///Users/fazlerabbi/Desktop/Projects/bcb_player_onboarding_backend/.agents/rules/) directory:
- [General Rules](file:///Users/fazlerabbi/Desktop/Projects/bcb_player_onboarding_backend/.agents/rules/general_rules.md): General project guidelines and commands.
- [Java & Spring Boot Rules](file:///Users/fazlerabbi/Desktop/Projects/bcb_player_onboarding_backend/.agents/rules/java_rules.md): Coding style, Lombok, and GraalVM native image compatibility constraints.

Please ensure you adhere to these guidelines for all code generation and modifications in this project.

## Local MCP Server Configuration

This project is configured to run as an MCP (Model Context Protocol) Server over stdio. 

- **Configuration File:** [.agents/config/mcp_config.json](file:///Users/fazlerabbi/Desktop/Projects/bcb_player_onboarding_backend/.agents/config/mcp_config.json)
- **Tool Class:** [PlayerOnboardingMcpTools.java](file:///Users/fazlerabbi/Desktop/Projects/bcb_player_onboarding_backend/src/main/java/org/bracit/bcb_player_onboarding_backend/mcp/PlayerOnboardingMcpTools.java)
- **Command to Build:** `./gradlew bootJar`

### Exposed Tools:
1. `verifyPlayerEligibility`: Checks age (must be 18-40) and health fitness.
2. `registerPlayer`: Registers player roles and status.

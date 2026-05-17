# Establish CI and repository hygiene for modernization work

## Priority
High

## Labels
`modernization`, `ci`, `developer-experience`, `maintenance`

## Background
Modernization work needs repeatable tooling so build failures are visible and future dependency/target-platform updates are safer. The repository should define Maven, CI, generated-file handling, formatting, and agent/developer operating instructions.

## Scope
- Add Maven Wrapper and pin the Maven version used by CI.
- Add CI that runs a full Tycho build, license checks, and dependency/license reports.
- Add optional Linux UI smoke tests under xvfb if/when UI tests exist.
- Add or refresh `.gitignore` for generated outputs, copied libraries, native artifacts, IDE metadata, and test output.
- Add formatter/import-order configuration, preferably compatible with Eclipse JDT.
- Add root `AGENTS.md` so coding agents know build commands, metadata files, and project guardrails.
- Configure dependency update automation such as Renovate after the target-platform strategy is stable.

## Acceptance criteria
- CI runs on pull requests and main-branch pushes.
- Developers can run the same build locally through `./mvnw`.
- Generated artifacts are ignored or explicitly documented if committed.
- `AGENTS.md` exists with project-specific build and Eclipse plug-in instructions.

## References
- See `docs/modernization-tasks.md`, sections "Establish CI and repository hygiene" and "Suggested `AGENTS.md` content".

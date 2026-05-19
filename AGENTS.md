# AGENTS.md

## Project overview

Mirur is an Eclipse/Tycho plug-in repository for visually debugging arrays in an IDE. Treat PDE metadata as source:
`META-INF/MANIFEST.MF`, `plugin.xml`, `feature.xml`, `category.xml`, `build.properties`, and the checked-in target definition must stay in sync with Java changes.

## Build and verification

- Use Maven 3.9.x so local runs match CI: `mvn -B -Dtycho.localArtifacts=ignore clean verify`.
- The parent POM enforces Maven 3.9.x, and CI runs on pull requests plus pushes to `main`/`master`.
- Run with JDK 21; CI uses Temurin 21.
- The parent `verify` lifecycle includes the Mycila license-header check for `src/main/java/**`.
- Generate dependency and third-party license reports with the same commands used by `.github/workflows/ci.yml` when changing dependencies.

## Eclipse plug-in guardrails

- Keep `releng/target-platform/mirur.target` pinned to a dated Eclipse repository after validating any target-platform update.
- Do not add implicit Eclipse repositories to child modules; use the target-platform module and parent Tycho configuration.
- If bundle dependencies change, update both Java/PDE metadata and any affected feature or repository metadata.
- The JOGL fragment modules copy native/runtime artifacts during Maven builds; do not commit generated `target/`, copied `lib/` jars, or unpacked native binaries unless explicitly documented.

## Formatting and imports

- Prefer the Eclipse JDT formatter profile at `releng/formatter/eclipse-java-formatter.xml`.
- Prefer the import order at `releng/formatter/eclipse.importorder`.
- Do not perform repository-wide formatting unless the task asks for it.

## Repository hygiene

- Generated Maven, Tycho, p2, IDE, workspace, native, and test-output artifacts should remain ignored by `.gitignore`.
- Keep dependency automation conservative. Renovate is configured in `renovate.json`; target-platform or Tycho updates should be validated with the full Maven build before merge.

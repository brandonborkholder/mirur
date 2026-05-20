# IntelliJ Plugin Packaging, Verification, and Publication

This document captures the Phase 5 plan and current setup for the parallel IntelliJ plugin.

## Packaging strategy (cross-platform)

Current strategy:

- Build a **single IntelliJ plugin ZIP** from `mirur-intellij-plugin` using Gradle IntelliJ Platform tooling.
- Keep IntelliJ-host integration in the IntelliJ module, while reusing shared logic from `mirur-core` and `mirur-render` where possible.
- For JOGL/native dependencies, prefer one of the following in Phase 6 depending on renderer extraction outcome:
  - bundle platform-classified natives and load lazily per OS, or
  - produce Marketplace variants if native payload size/compatibility becomes problematic.

## CI matrix

GitHub Actions workflow `.github/workflows/intellij-plugin.yml` provides:

- plugin build on:
  - `ubuntu-latest`
  - `macos-latest`
  - `windows-latest`
- verifier run on `ubuntu-latest`.

## Compatibility checks

- Plugin metadata and compatibility bounds are configured in `mirur-intellij-plugin/build.gradle.kts`:
  - `sinceBuild = "261"`
  - `untilBuild = "261.*"`
- CI runs `verifyPlugin` to perform IntelliJ Plugin Verifier checks.

## Publication/signing path

Current publication path recommendation:

1. Build and verify in CI (`buildPlugin`, `verifyPlugin`).
2. Publish to an internal channel first.
3. Promote to JetBrains Marketplace after manual debugger workflows pass parity checks.

Signing recommendation:

- Use JetBrains Marketplace signing in CI with repository secrets for certificate/private-key material.
- Keep signing disabled on pull requests from forks.


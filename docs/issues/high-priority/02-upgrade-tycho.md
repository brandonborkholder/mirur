# Upgrade Tycho in staged migrations

## Priority
High

## Labels
`modernization`, `tycho`, `maven`, `build`

## Background
The project is pinned to Tycho 1.1.0, which is old enough that dependency resolution already fails in the current environment. A modern Tycho version is required before reliable CI, target-platform management, and p2 publication can be established.

## Scope
- Capture the current build failure as a baseline.
- Upgrade Tycho in staged branches, crossing major versions deliberately rather than making unrelated changes at the same time.
- Update or replace legacy Tycho plug-in configuration as required by current Tycho documentation.
- Keep Maven + Tycho as the primary build path.
- Evaluate Tycho's current support for OSGi tests, API baseline checks, p2 metadata, and source bundles.

## Acceptance criteria
- The parent POM uses a supported Tycho release.
- `mvn -B -Dtycho.localArtifacts=ignore clean verify` resolves the target platform and reaches compilation/test phases.
- Any required migration notes are documented in `docs/development.md` or `docs/modernization-tasks.md`.
- Tycho upgrade commits do not include unrelated module renames or broad dependency upgrades.

## References
- Parent POM currently pins `tycho.version` to `1.1.0`.
- See `docs/modernization-tasks.md`, section "Upgrade Tycho in a staged branch".

# Rework third-party dependency and native-library packaging

## Priority
High

## Labels
`modernization`, `dependencies`, `osgi`, `jogl`

## Background
The UI plug-in copies Maven dependencies into `mirur-ui/lib` during the build and embeds JOGL/Glimpse-related jars directly in the bundle classpath. This makes dependency provenance, updates, source bundles, native fragments, and vulnerability scanning harder to manage.

## Scope
- Inventory all embedded third-party jars, licenses, source jars, and known vulnerabilities.
- Decide which dependencies should come from the Eclipse target/p2 repositories, which should be wrapped/generated from Maven artifacts, and which should remain embedded.
- Modernize JOGL, Glimpse, FastUtil, Guava, and MigLayout versions only after target-platform and Tycho upgrades are stable.
- Validate native fragment packaging for Linux, Windows, macOS x86_64, and macOS aarch64 if supported.
- Update bundle manifests and `build.properties` to match the chosen packaging strategy.

## Acceptance criteria
- Dependency inventory exists with versions, licenses, and packaging strategy.
- Generated/copied dependencies are either removed from source control or clearly documented as committed artifacts.
- Native fragments have current platform filters and host ranges.
- Dependency packaging works in a clean checkout and CI environment.

## References
- `mirur-ui` currently copies dependencies into `lib` during Maven initialization.
- `mirur-jogl-*` modules unpack platform native artifacts.
- See `docs/modernization-tasks.md`, section "Rework third-party dependency handling".

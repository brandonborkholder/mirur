# Replace Indigo-era Eclipse target with a pinned target platform

## Priority
High

## Labels
`modernization`, `eclipse`, `tycho`, `build`

## Background
The build currently resolves Eclipse dependencies from the Indigo release train. This makes the build depend on very old Eclipse APIs and makes it difficult to validate compatibility with current Eclipse IDE releases.

## Scope
- Add a checked-in PDE target definition under `releng/target-platform/`.
- Start with a current Eclipse release repository for exploration, then pin to a dated release train for reproducible CI.
- Replace HTTP update-site URLs with HTTPS.
- Keep the target definition minimal: include Platform/UI, JDT Debug, Debug UI, and any other units Mirur directly needs.
- Document the oldest supported Eclipse release and the latest tested Eclipse release.

## Acceptance criteria
- `releng/target-platform/mirur.target` exists and is documented.
- Maven/Tycho uses the checked-in target definition instead of an implicit Indigo p2 repository.
- The repository documents the selected Eclipse release train and support matrix.
- The build no longer references `http://download.eclipse.org/releases/indigo/`.

## References
- Parent POM currently declares the Indigo p2 repository.
- See `docs/modernization-tasks.md`, section "Move off the Indigo-era Eclipse target".

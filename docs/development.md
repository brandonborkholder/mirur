# Development Environment

## Eclipse target platform

Mirur uses a checked-in PDE target definition at
`releng/target-platform/mirur.target`. Maven/Tycho resolves Eclipse bundles
from that target through the `mirur-target-platform` reactor module instead of
using an implicit release-train repository from the parent POM.

The target is intentionally small. It pins the Eclipse 2026-03 simultaneous
release repository at `https://download.eclipse.org/releases/2026-03/202603111000/`
and includes only the Platform/UI, Debug UI, JDT Debug, core runtime, and JFace
Text installable units that Mirur declares directly in `mirur-ui/META-INF/MANIFEST.MF`.
Transitive dependencies are resolved by p2 from the same pinned repository.

## Eclipse compatibility matrix

| Compatibility item | Eclipse release | Notes |
| --- | --- | --- |
| Oldest supported Eclipse release | Eclipse IDE 2026-03 / Platform 4.39 | This is the pinned compile target for Tycho and PDE imports. |
| Latest tested Eclipse release | Eclipse IDE 2026-03 / Platform 4.39 | Update after validating Mirur on a newer quarterly Eclipse IDE release. |

When evaluating a newer Eclipse release, start with
`https://download.eclipse.org/releases/latest/` locally, then update
`releng/target-platform/mirur.target` to a dated repository once the build and
manual debugger smoke tests pass. Keep release-train URLs on HTTPS.

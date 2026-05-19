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

## Maven/Tycho build

Run Maven with JDK 21. Tycho `5.0.2` requires Maven `3.9.9` or newer
and JDK `21` for the Tycho plug-ins themselves, while Mirur
compiles its Java sources for Java 21 (`<release>21</release>`).

```bash
mvn -B -Dtycho.localArtifacts=ignore clean verify
```

## Automated Eclipse smoke tests

The `mirur-ui.tests` module is a Tycho `eclipse-test-plugin` that launches the
Eclipse IDE workbench with the Mirur UI bundle installed. Its smoke tests start
the bundle, verify the registered workbench extensions for Mirur views,
preferences, and debug detail panes, and instantiate the lightweight Statistics
view. The test workspace is created under `mirur-ui.tests/target/workspaces/` so
the checked-in `test-workspace/` directory can remain a manual/sample workspace
for debugger scenarios.

Run just the plug-in smoke tests after the upstream modules have been built with:

```bash
mvn -B -Dtycho.localArtifacts=ignore -pl mirur-agent,mirur-ui,mirur-ui.tests,releng/target-platform -am verify
```

On Linux CI agents, run this command under `xvfb-run` when no display server is
available because the tests intentionally exercise the Eclipse workbench UI
harness.

## Eclipse compatibility matrix

| Compatibility item | Supported baseline | Notes |
| --- | --- | --- |
| Oldest supported Eclipse release | Eclipse IDE 2026-03 / Platform 4.39 | This is the pinned compile target for Tycho and PDE imports. |
| Latest tested Eclipse release | Eclipse IDE 2026-03 / Platform 4.39 | Update after validating Mirur on a newer quarterly Eclipse IDE release. |
| Runtime JDK for Eclipse | JDK 21 | Use a 64-bit JDK that matches the selected Eclipse IDE runtime requirement. |
| Build JDK for Maven/Tycho | JDK 21 | Tycho `5.0.2` requires JDK 21 and Maven 3.9.9 or newer. |
| Java language/API level | Java 21 for the Eclipse plug-in; Java 8 for the injected agent | The parent POM uses `maven-compiler-plugin` with `<release>21</release>`, while `mirur-agent` keeps `JavaSE-1.8`/`javacTarget = 1.8` so remote agent classes remain loadable in older debuggee VMs. |

When evaluating a newer Eclipse release, start with
`https://download.eclipse.org/releases/latest/` locally, then update
`releng/target-platform/mirur.target` to a dated repository once the build and
manual debugger smoke tests pass. Keep release-train URLs on HTTPS.

## Runtime workbench launch

To test Mirur from Eclipse PDE:

1. Install and start Eclipse IDE 2026-03 or newer on JDK 21.
2. Import the Maven/Tycho projects and set `releng/target-platform/mirur.target`
   as the active target platform.
3. Create or update an Eclipse Application launch configuration that includes
   the `mirur.mirur-ui` plug-in and the required Eclipse Platform/JDT Debug
   bundles from the active target.
4. Run the launch with JDK 21. Use a fresh workspace when validating debugger
   behavior against the bundled `test-workspace/simple` sample.

### Required JVM exports for manual debugger testing

The runtime workbench still needs these VM arguments when exercising the
current visualization/debugger path:

```text
--add-exports java.base/java.lang=ALL-UNNAMED
--add-exports java.desktop/sun.awt=ALL-UNNAMED
--add-exports java.desktop/sun.java2d=ALL-UNNAMED
```

Owner: Mirur maintainers. Follow-up: isolate or remove the remaining
JDK-internal access in the rendering/debugger integration before claiming a
fully module-clean Java 21 runtime. Keep this list in sync with `README.md`
until the follow-up is complete.

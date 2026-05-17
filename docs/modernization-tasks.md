# Modernization and Upgrade Task List

This document records recommended work for modernizing Mirur while keeping the current change set limited to suggestions only.

## Current repository snapshot

Mirur is a Maven/Tycho-built Eclipse plug-in repository with these main modules:

- `mirur-agent`: Java agent bundle used by the debugger integration.
- `mirur-ui`: main Eclipse UI plug-in, views, preferences, array copying, and bundled visualization dependencies.
- `mirur-feature`: Eclipse feature wrapping the plug-ins/fragments.
- `mirur-repository`: p2 update-site repository module.
- `mirur-jogl-parent` plus `mirur-jogl-linux64`, `mirur-jogl-win64`, and `mirur-jogl-osx`: platform native fragments for JOGL.
- `test-workspace`: manual/sample workspace content rather than automated test fixtures.

## Highest-priority upgrade tasks

### 1. Move off the Indigo-era Eclipse target

- Replace the current `http://download.eclipse.org/releases/indigo/` p2 target with an explicit `.target` file checked into the repository, likely under `releng/target-platform/`.
- Start with a current Eclipse simultaneous-release repository, for example `https://download.eclipse.org/releases/latest/` for exploratory work, then pin to a dated release train for reproducible CI.
- Use HTTPS for all update-site URLs.
- Keep the target definition small: include the Eclipse Platform/JDT/Debug/UI units that Mirur really needs instead of making an entire release train the implicit target platform.
- Add a task to test both "oldest supported Eclipse" and "latest Eclipse" before declaring compatibility.

Rationale: the parent POM currently uses an Indigo p2 repository and old bundle lower bounds such as Eclipse UI/JDT Debug 3.7.x. A modern target platform will surface API removals, split packages, JPMS access issues, and native-library behavior changes early.

### 2. Upgrade Tycho in a staged branch

- Stay with Maven + Tycho as the primary build. Tycho remains the Eclipse project's Maven-centric build system for Eclipse plug-ins, OSGi bundles, features, p2 repositories, RCP applications, and BND workspaces.
- Upgrade in stages rather than jumping directly from Tycho `1.1.0` to the latest line:
  1. Establish a reproducible baseline with the current build.
  2. Upgrade to a newer Tycho 2.x/3.x/4.x line only as needed to cross breaking changes.
  3. Then evaluate Tycho 5.x, which is current in 2026.
- Replace legacy Tycho plug-in names/configuration where the current Tycho documentation requires it.
- Add Tycho's test support for OSGi/UI tests and consider Tycho API Tools/Baseline checks once public API boundaries are defined.
- Investigate Tycho's pomless/structured layout only after the normal Maven build is healthy. Do not make this the first migration because this repository already has a small explicit multi-module Maven structure.

External references checked on 2026-05-17:

- Tycho official docs describe Tycho as Maven tooling for Eclipse plug-ins, OSGi bundles, features, update sites/p2 repositories, RCP applications, and BND workspaces: https://tycho.eclipseprojects.io/doc/latest/
- Eclipse project metadata lists Tycho `5.0.2` as released on 2026-01-24: https://projects.eclipse.org/projects/technology.tycho/governance
- Tycho target-platform docs recommend p2 repositories with `<layout>p2</layout>` and support `.target` files for finer control: https://tycho.eclipseprojects.io/doc/main/TargetPlatform.html
- Tycho build-properties docs note Tycho supports only a subset of PDE `build.properties` keys and suggests Maven/toolchain alternatives for some legacy keys: https://tycho.eclipseprojects.io/doc/latest/BuildProperties.html

### 3. Modernize Java runtime and compiler strategy

- Decide and document the support matrix:
  - Minimum runtime Eclipse/JDK combination.
  - JDK used to run Maven/Tycho in CI.
  - Java language level used to compile Mirur bundles.
- Move from source/target `8` and JavaSE-1.8 bundle execution environments to a supported baseline such as Java 17 if the selected Eclipse release train requires it.
- Prefer `maven-compiler-plugin` `release` over separate `source`/`target` where compatible with the selected Tycho version.
- Revisit JVM arguments in `README.md`; the current `--add-exports` requirements indicate deep reflection/internal API access that should be minimized or isolated.

### 4. Rework third-party dependency handling

- Inventory all embedded libraries in `mirur-ui/lib` and confirm licenses, source availability, and CVEs.
- Replace copying Maven dependencies into `mirur-ui/lib` during `initialize` with a more reproducible approach:
  - Prefer target-platform/p2-provided bundles when available.
  - For plain Maven jars, use Tycho's current `pomDependencies` or generated OSGi bundles if appropriate.
  - Keep native fragments for platform-specific JOGL artifacts only if still required.
- Update JOGL/Glimpse/FastUtil/Guava/MigLayout versions after checking compatibility with current Eclipse SWT/OpenGL behavior.
- Verify whether the separate JOGL native fragments still need `J2SE-1.5` bundle execution environments and whether macOS should support both x86_64 and aarch64.

### 5. Improve p2 repository and release flow

- Stop writing release output to a sibling checkout such as `../mirur-update-site/update-site/` during a normal `mvn install`.
- Make repository output a build artifact from `mirur-repository/target/repository` and publish it from CI.
- Remove or replace Pack200/jarprocessor instructions; Pack200 is obsolete in modern Java/Eclipse distributions.
- Add signing strategy for jars/update-site metadata if distributing publicly.
- Update `category.xml` so architecture filters match actual supported fragments; it currently advertises `x86` even though fragments are x86_64-only.
- Add release checklist tasks for version bumping, changelog generation, tag creation, GitHub release/upload, and update-site publication.

### 6. Add automated test structure

- Split tests into:
  - Plain JVM unit tests for `mirur.core` array visitors and data conversion.
  - OSGi/Tycho plug-in tests for extension wiring and Eclipse services.
  - SWTBot or Eclipse UI smoke tests for views/preferences if maintainers want UI coverage.
- Move manual sample projects under a clearly named `examples/` or `test-workspace/README.md` and document how they are used.
- Add regression tests for primitive arrays, boxed arrays, lists, jagged arrays, complex/interleaved arrays, sorting, min/max, finite-value handling, CSV export, and tooltip/selection behavior.

### 7. Establish CI and repository hygiene

- Add Maven Wrapper (`mvnw`, `.mvn/wrapper`) and commit the Maven version used by CI.
- Add GitHub Actions or another CI workflow that runs at least:
  - `./mvnw -B -Dtycho.localArtifacts=ignore clean verify`
  - license check
  - dependency vulnerability/license reporting
  - optional UI smoke test on Linux with xvfb
- Add `.gitignore` entries for generated `target/`, copied `lib/` jars, native files unpacked into fragment projects, IDE metadata, and test output if missing.
- Add formatting/import rules. For Java, consider Eclipse JDT formatter configuration because this is an Eclipse plug-in project.
- Add dependency update automation such as Renovate, configured carefully for Tycho/p2 and Maven dependency updates.

## Recommended repository structure changes

A conservative Maven/Tycho-friendly layout could be:

```text
/
  AGENTS.md                         # Coding-agent operating instructions
  README.md                         # User/developer quick start
  docs/
    modernization-tasks.md          # This task list
    architecture.md                 # Bundle responsibilities and extension points
    release.md                      # Versioning, p2 publication, signing
    development.md                  # IDE import, target platform, run/debug configs
  releng/
    target-platform/
      mirur.target                  # Pinned Eclipse target definition
    ci/
      README.md                     # CI/test matrix notes, if needed
  examples/
    simple/                         # Rename/move current manual test workspace projects
  mirur-agent/
  mirur-ui/
  mirur-feature/
  mirur-repository/
  mirur-jogl-parent/
  mirur-jogl-linux64/
  mirur-jogl-win64/
  mirur-jogl-osx/
```

Keep module renames optional. If renaming modules, prefer Eclipse/OSGi-style names for consistency:

- `mirur-ui` -> `mirur.mirur-ui` or `plugins/mirur.mirur-ui`
- `mirur-agent` -> `mirur.jdk-agent` or `plugins/mirur.jdk-agent`
- `mirur-feature` -> `features/mirur.feature`
- `mirur-repository` -> `releng/mirur.repository`
- native fragments -> `fragments/mirur.jogl.*`

Do not combine renames with the Tycho upgrade; do them after the build is green so any resolver breakage is attributable.

## Suggested `AGENTS.md` content

Add a root-level `AGENTS.md` so Codex, Claude, and other coding agents can work safely. Suggested sections:

```markdown
# AGENTS.md

## Project overview
Mirur is an Eclipse plug-in built with Maven and Tycho. The main UI bundle is `mirur-ui`; the Java agent bundle is `mirur-agent`; `mirur-feature` and `mirur-repository` package the installable feature and p2 repository; `mirur-jogl-*` projects provide JOGL native fragments.

## Ground rules
- Prefer Maven/Tycho metadata (`pom.xml`, `META-INF/MANIFEST.MF`, `feature.xml`, `category.xml`, `build.properties`) over IDE-only metadata.
- Do not edit generated `target/` outputs or copied dependency jars in `mirur-ui/lib` unless the task is specifically about dependency packaging.
- Keep bundle IDs, artifact IDs, and feature IDs aligned when changing versions.
- Avoid broad dependency updates without running a full Tycho build.
- Do not add try/catch wrappers around imports.

## Build and test commands
- `mvn -B -Dtycho.localArtifacts=ignore clean verify`
- `mvn -B -pl mirur-ui -am test`
- `mvn -B license:check`

## Eclipse plug-in notes
- Check `META-INF/MANIFEST.MF` when adding package imports or bundle dependencies.
- Check `build.properties` when adding resources that must be shipped in the bundle.
- Check `plugin.xml` when changing views, preference pages, or debug UI integration.
- Check `feature.xml` and `category.xml` when changing installable units.

## Modernization priorities
- Keep Maven + Tycho unless explicitly asked to evaluate another build system.
- Prefer a checked-in `.target` definition under `releng/target-platform`.
- Prefer CI-published p2 repositories over writing into a sibling update-site checkout.
```

## Eclipse plug-in build-system recommendation

- Recommended default: continue with Maven + Tycho. This is the least disruptive and is still the standard headless build path for Maven-based Eclipse plug-in, feature, and p2 repository builds.
- Recommended enhancement: add a checked-in PDE target file and use Tycho to resolve against it. This improves reproducibility and helps developers import the same target into Eclipse IDE.
- Optional future evaluation: BND workspace layout/pomless Tycho. This can reduce POM boilerplate for OSGi-heavy projects, but it is not the best first step here because Mirur already uses Tycho packaging types and only has a handful of modules.
- Avoid as a first migration: Gradle-only rebuild. Gradle can build OSGi artifacts, but Eclipse plug-in ecosystems still commonly rely on PDE metadata and Tycho for p2/update-site workflows. A Gradle migration would add risk without solving the current known blockers.

## Issue backlog

High-priority issue drafts for the seven sections above are available under `docs/issues/high-priority/` so they can be copied directly into GitHub issues or used by another issue tracker.

### Build and dependency issues

- [ ] Replace Indigo p2 target with a pinned `.target` file.
- [ ] Upgrade Tycho from `1.1.0` through a staged migration to a supported modern release.
- [ ] Replace HTTP URLs in build metadata with HTTPS.
- [ ] Remove Pack200/jarprocessor release steps.
- [ ] Stop publishing to a sibling checkout during `install`.
- [ ] Review and modernize Maven plug-in versions.
- [ ] Add Maven Wrapper.
- [ ] Add CI with JDK/Eclipse/Tycho matrix.
- [ ] Add dependency vulnerability and license reports.
- [ ] Decide whether copied jars in `mirur-ui/lib` are generated or committed; enforce with `.gitignore` and documentation.

### Eclipse/OSGi issues

- [ ] Update `Bundle-RequiredExecutionEnvironment` values.
- [ ] Review `Require-Bundle` version ranges and replace with `Import-Package` where appropriate.
- [ ] Add/validate source bundles for update-site consumers.
- [ ] Validate native fragment host ranges and platform filters.
- [ ] Add macOS aarch64 support if JOGL/Glimpse support it.
- [ ] Check `plugin.xml` extension points against current Eclipse Platform/JDT Debug APIs.

### Code issues

- [ ] Remove or isolate use of JDK internals that require `--add-exports`.
- [ ] Audit JDI array-copy behavior on modern JDKs.
- [ ] Improve error handling and logging around debug-target communication and native/OpenGL initialization.
- [ ] Add tests for all array visitors and plot data transformations.
- [ ] Review high-DPI/font-scaling behavior.
- [ ] Review linear/nonlinear axis labeling bugs.
- [ ] Add copy-to-clipboard support or close the existing TODO.

### Documentation issues

- [ ] Expand `README.md` with current build prerequisites, target-platform setup, and troubleshooting.
- [ ] Add `docs/development.md` for importing into Eclipse and launching a runtime workbench.
- [ ] Add `docs/release.md` for versioning, signing, p2 publishing, and rollback.
- [ ] Add root `AGENTS.md` for coding agents.
- [ ] Move or explain `test-workspace` examples.
- [ ] Replace dead/legacy project URLs if `mirur.io` is no longer maintained.

## Suggested first implementation sequence

1. Add `AGENTS.md`, Maven Wrapper, `.gitignore`, and CI skeleton.
2. Capture current build failures and document prerequisites.
3. Add `releng/target-platform/mirur.target` pinned to a current Eclipse release.
4. Upgrade Tycho until `mvn clean verify` resolves and packages the p2 repository.
5. Update Java baseline and remove or minimize `--add-exports` requirements.
6. Rework dependency packaging and native fragments.
7. Add automated unit/plug-in tests.
8. Update release flow and publish artifacts from CI.

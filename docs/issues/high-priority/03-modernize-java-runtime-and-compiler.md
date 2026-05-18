# Define and modernize the Java runtime/compiler baseline

## Priority
High

## Labels
`modernization`, `java`, `eclipse`, `build`

## Status
Resolved by PR #30 for the initial Java 21 baseline. The remaining JDK-internal access is tracked as a separate follow-up in `docs/modernization-tasks.md`.

## Background
Historically, the build compiled with source/target 8 while using a JDK 11 toolchain, and the bundles declared JavaSE-1.8 or older execution environments. Current Eclipse IDE releases require newer Java runtimes, so Mirur needed an explicit support matrix and compiler strategy.

## Scope
- Define the minimum supported Eclipse release, runtime JDK, build JDK, and Java language level.
- Replace `source`/`target` with `release` where compatible with the selected Tycho/Maven setup.
- Update bundle execution environments consistently across plug-ins and fragments.
- Review runtime launch arguments and remove or isolate access to JDK internals where possible.
- Document how to launch the runtime workbench on the supported JDK.

## Acceptance criteria
- A documented Java/Eclipse support matrix exists.
- Maven compiler settings and bundle execution environments match the support matrix.
- Runtime workbench instructions are updated for the selected JDK.
- Any remaining `--add-exports` or internal JDK access is documented with owner and follow-up tasks.

## Resolution references
- Parent POM now uses `maven-compiler-plugin` with `<release>21</release>`.
- Tycho target-platform configuration now uses `JavaSE-21`.
- Bundles and fragments now declare `Bundle-RequiredExecutionEnvironment: JavaSE-21`.
- `docs/development.md` documents the Java/Eclipse support matrix, runtime workbench launch instructions, and the owner/follow-up for remaining `--add-exports` requirements.
- See `docs/modernization-tasks.md`, section "Modernize Java runtime and compiler strategy".

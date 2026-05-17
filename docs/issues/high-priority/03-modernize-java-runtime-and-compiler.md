# Define and modernize the Java runtime/compiler baseline

## Priority
High

## Labels
`modernization`, `java`, `eclipse`, `build`

## Background
The build compiles with source/target 8 while using a JDK 11 toolchain, and the bundles declare JavaSE-1.8 or older execution environments. Current Eclipse IDE releases require newer Java runtimes, so Mirur needs an explicit support matrix and compiler strategy.

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

## References
- Parent POM currently sets `source` and `target` to `8` and requests a JDK 11 toolchain.
- Bundles currently declare JavaSE-1.8 or J2SE-1.5 execution environments.
- See `docs/modernization-tasks.md`, section "Modernize Java runtime and compiler strategy".

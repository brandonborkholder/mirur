# Add automated test structure for core, OSGi, and UI behavior

## Priority
High

## Labels
`modernization`, `tests`, `quality`, `eclipse`

## Background
The repository has some test sources and a manual sample workspace, but it does not yet have a clearly documented automated test strategy for core array logic, OSGi integration, or Eclipse UI behavior.

## Scope
- Separate plain JVM unit tests for array visitors, conversion, sorting, statistics, finite-value handling, and CSV export.
- Add Tycho/OSGi plug-in tests for extension wiring and Eclipse service integration.
- Evaluate SWTBot or a lighter runtime-workbench smoke test for views, preferences, and debug UI integration.
- Move or document `test-workspace` as manual examples if it is not an automated fixture.
- Ensure tests run headlessly in CI where feasible.

## Acceptance criteria
- Test strategy is documented.
- At least one core JVM test suite runs under Maven.
- OSGi/UI test approach is chosen and documented, even if UI tests are initially smoke-only.
- Manual examples are either moved under `examples/` or documented in place.

## References
- Existing `test-workspace` appears to contain manual/sample Java projects.
- See `docs/modernization-tasks.md`, section "Add automated test structure".

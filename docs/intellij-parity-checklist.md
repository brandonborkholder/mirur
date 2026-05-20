# IntelliJ vs Eclipse Feature Parity Checklist

This checklist tracks feature parity between the existing Eclipse plugin and the new IntelliJ plugin.

## Legend

- ✅ Complete
- 🟡 Partial / scaffolded
- ⬜ Not started

## Core integration

- ✅ Shared core array/visitor model extracted to `mirur-core`.
- 🟡 Shared render host abstraction module (`mirur-render`) created; full painter extraction pending.
- 🟡 IntelliJ plugin skeleton module created.

## Debugger integration

- 🟡 "Submit to Mirur" action registered in IntelliJ debugger popup.
- 🟡 XDebugger session bridge scaffolded.
- ⬜ Debug-value extraction from variables tree (object/array payloads).
- ⬜ Remote `mirur-agent` deployment/attach flow.
- ⬜ Chunked transfer and cancellation handling for large payloads.

## Views and UX

- 🟡 Mirur tool window registered and render placeholder shown.
- ⬜ 1D line chart view parity.
- ⬜ 1D bar chart view parity.
- ⬜ 2D heatmap view parity.
- ⬜ Histogram view parity.
- ⬜ Toggle/duplicate view actions.
- ⬜ Export/statistics actions.

## Settings and persistence

- ✅ IntelliJ persistent settings service scaffolded (`PersistentStateComponent`).
- ⬜ Settings UI page with user-facing controls.
- ⬜ Migration defaults aligned with Eclipse preferences.

## Packaging and CI

- ✅ IntelliJ plugin CI matrix (Linux/macOS/Windows) configured.
- ✅ IntelliJ plugin verifier CI configured.
- ✅ Release/signing path documented.

## Release readiness gate (non-performance)

- ⬜ End-to-end debugger submission (primitive arrays) works.
- ⬜ End-to-end rendering for MVP visualizations works.
- ⬜ Manual smoke checklist passes on supported IDE/JDK matrix.
- ⬜ Known gaps documented in release notes.

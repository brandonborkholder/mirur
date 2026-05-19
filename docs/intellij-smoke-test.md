# IntelliJ Plugin Smoke Test Script (Manual)

This script is the Phase 6 non-performance validation checklist for the IntelliJ plugin.

## Preconditions

- IntelliJ IDEA Community `2026.1.x`
- JDK 21
- Plugin ZIP built from `mirur-intellij-plugin`
- Sample project with a breakpoint and arrays in scope

## Build plugin artifact

From repository root:

```bash
gradle -p mirur-intellij-plugin --no-daemon clean buildPlugin
```

Expected artifact:

- `mirur-intellij-plugin/build/distributions/*.zip`

## Install plugin

1. IntelliJ IDEA → Settings → Plugins → gear icon → Install Plugin from Disk.
2. Select the built Mirur ZIP.
3. Restart IDE.

## Smoke checks

### A) Plugin loads

- Verify `Mirur` tool window is present.
- Open tool window and confirm placeholder content appears.

### B) Action registration

- Start a debug session in a Java project.
- Open debugger variables popup/context menu.
- Confirm `Submit to Mirur` action is present.

### C) Debugger session bridge

- Trigger `Submit to Mirur` with an active debug session and selected expression.
- Confirm a Mirur notification appears stating expression was queued.
- Trigger action with no active debug session; confirm graceful "No active debug session" notification.

### D) Settings persistence

- Change settings values via persisted state (when UI is added) or by editing `mirur.xml`.
- Restart IDE and verify persisted values reload.

## Exit criteria for Phase 6 (excluding performance)

- Tool window appears and is stable.
- Debugger action is discoverable and invocable.
- Session bridge handles active/inactive debugger cases without exceptions.
- Persistent state survives restart.

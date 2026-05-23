# Mirur IntelliJ Plugin — Minimal Design Document (v0)

## 1. Context and goals

Mirur’s Eclipse plugin visualizes numeric arrays during debugging so developers do not need to inspect huge `toString()` outputs. Existing Mirur visualizations include:
- 1D line and bar views,
- 2D heatmap,
- histogram/statistics,
- additional specialized views for images, scatter, and complex data.

The IntelliJ plugin should start minimal and focus on the highest-value interaction: **select array-like debug values and open a visualization quickly**.

## 2. What Mirur does today (behavior to preserve)

From this repository’s current implementation and metadata:
- Integrates into debug variable selection flows and submits array data to a dedicated UI view.
- Supports multiple render modes over numeric sequences and matrices.
- Includes basic view-level actions such as duplicate/new view, reset axes, pinning, transpose/scale/gradient toggles, and strategy selection.

For IntelliJ v0, preserve only the core value path:
1. pick a variable in debugger,
2. send values to Mirur tool window,
3. show a default plot quickly.

## 3. Product decisions from maintainer feedback

- **Parity target**: feature parity over time, not pixel-perfect parity with Eclipse.
- **Runtime support target**: Java debug targets from **Java 8 onward** (local and remote where supported by IntelliJ APIs).
- **Fallback behavior**: always show a default visualization first for all data shapes, then allow user selection/refinement.
- **Default docking**: Mirur tool window should open at the **bottom** by default to maximize horizontal plot real estate.

## 4. Proposed IntelliJ UI layout

### 4.1 Entry points

1. **Debugger Variables context menu**
   - Action: `Visualize with Mirur`
   - Appears when selected value looks numeric-array-like.

2. **Debugger value inline icon (optional in v0.1)**
   - Small Mirur icon near eligible values.
   - Defer if complexity is high; context menu is enough for v0.

### 4.2 Tool window

Create a **bottom-docked** tool window named **Mirur** (user can move it later using normal IntelliJ behavior).

#### Header / toolbar (top row)

Left to right:
1. **View type segmented control/dropdown**: `Auto | Line | Bar | Heatmap | Histogram`
2. **Refresh** button (re-read current variable from debug session)
3. **Pin** toggle (freeze current dataset; ignore variable selection changes)
4. **Reset Axes** button
5. **Settings** gear (opens plugin settings dialog)

#### Main content area

- **Primary plot canvas** occupies most of space.
- Use dark/light-aware theme integration with IntelliJ UI defaults.
- Tooltip on hover: index/coordinates + value.

#### Footer/status strip

- Dataset badge: e.g., `double[2048]` or `float[256][256]`
- Min/Max summary
- Sampling indicator if data was truncated/downsampled

### 4.3 Empty/error states

- **Empty**: “Select a numeric array in Debug Variables, then choose ‘Visualize with Mirur’.”
- **Unsupported type**: show concise reason + link to supported types.
- **Disconnected session**: “Debug session ended. Refresh unavailable.”

## 5. Minimal interaction model

### 5.1 Default view selection (Auto)

- 1D numeric sequence → **Line**
- 2D numeric array/matrix → **Heatmap**
- >2D or jagged/irregular nested structures → **Histogram** over flattened values (with warning badge), with follow-up controls to choose slice/projection mode

### 5.2 Supported inputs for v0

- Primitive numeric arrays (`int[]`, `float[]`, `double[]`, etc.)
- Boxed numeric arrays (`Double[]`, etc.)
- `List<Number>` (size-limited)

### 5.3 Performance/safety rules

- Soft cap total elements (example: 2M) with user warning.
- For very large 1D arrays, downsample for initial render; keep raw data for detailed readout when possible.
- Never block IDE UI thread during variable extraction/transform/render.

## 6. Button placement rationale (quick decisions)

- **View type first**: most frequent user intent is changing chart representation.
- **Refresh next**: explicit data reload tied to debug state changes.
- **Pin adjacent to refresh**: users often compare “live” vs “frozen” view.
- **Reset axes near pin**: viewport operation, not data operation.
- **Settings at far right**: infrequent action following IntelliJ toolbar conventions.

## 7. Rendering strategy options: Eclipse parity vs IntelliJ-native

This section explains the requested differences so direction can be selected explicitly.

### Option A: Reuse Mirur rendering concepts closely

Meaning:
- Keep Mirur’s existing plot semantics and behavior as the reference model.
- Implement equivalent render modes and interactions (line/bar/heatmap/histogram defaults, axis reset behavior, pinning model).

Pros:
- Faster functional parity with existing Eclipse users.
- Easier cross-IDE docs and user expectations.
- Lower product-risk for “does Mirur behave like Mirur?”.

Cons:
- May feel less native to IntelliJ interaction conventions.
- Could require custom rendering work instead of leveraging IntelliJ ecosystem components.

### Option B: Adapt to IntelliJ-native charting patterns

Meaning:
- Preserve Mirur’s data-visualization intent, but map controls/interactions to standard IntelliJ UX patterns and components.
- Accept minor behavior differences if they improve consistency with JetBrains tooling.

Pros:
- Better native feel for IntelliJ users.
- Potentially simpler maintenance if standard UI abstractions are used.

Cons:
- Higher risk of drift from Eclipse Mirur semantics.
- Documentation and support may need IDE-specific explanations.

### Recommendation for v0

Use a **hybrid approach**:
- keep Mirur semantics and default data decisions,
- present controls using IntelliJ-native chrome/layout patterns,
- avoid deep fidelity work until feature parity baseline is stable.


## 8. Concrete UX differences: Mirur-style vs IntelliJ-native

This section provides concrete, screen-level examples of where behavior would differ.

### Example A: Toolbar control style

- **Mirur-style carry-over**: custom segmented buttons, iconography, and control spacing modeled after current Eclipse plugin affordances.
- **IntelliJ-native**: use `ActionToolbar` + `ToggleAction`/`ComboBoxAction`, standard IntelliJ spacing, hover, and overflow behavior.

Practical impact:
- Mirur-style works functionally, but users may perceive it as a “foreign widget strip” because spacing/hover/focus states differ from nearby IDE tool windows.
- IntelliJ-native blends better with built-in tool windows (Profiler, Services, Run).

### Example B: Multi-view behavior

- **Mirur-style carry-over**: duplicate/new-view workflows as primary interaction (separate panes/snapshots) similar to Eclipse approach.
- **IntelliJ-native**: single tool window with tabs or pinned snapshots listed in a side strip, following common JetBrains panel patterns.

Practical impact:
- Mirur-style can feel powerful but heavy early; users may lose track of many panes in a bottom dock.
- IntelliJ-native tab/list metaphors are easier for first-time users and fit existing mental models.

### Example C: Context menu wording and placement

- **Mirur-style carry-over**: action names and grouping copied from Eclipse verbs and menu hierarchy.
- **IntelliJ-native**: action labels and placement aligned with Debugger `XValue` conventions (short verbs, grouped with Evaluate/Watch flows).

Practical impact:
- Mirur-style labels may be understandable but can read as “ported” rather than “integrated,” especially if they are longer or grouped differently than neighboring actions.
- IntelliJ-native placement improves discoverability by matching expected debugger action clusters.

### Example D: Empty/error states

- **Mirur-style carry-over**: custom placeholder layouts/messages modeled on Eclipse views.
- **IntelliJ-native**: JetBrains empty-state typography, inline links/actions, and status text tone.

Practical impact:
- Mirur-style states can look visually inconsistent (font weight/margins/link affordances).
- IntelliJ-native states feel coherent with the rest of the IDE.

### Example E: Interaction shortcuts and gestures

- **Mirur-style carry-over**: pan/zoom/reset shortcuts matching Eclipse implementation assumptions.
- **IntelliJ-native**: keybindings and mouse gestures aligned with IntelliJ plot/editor conventions and configurable keymap behavior.

Practical impact:
- Mirur-style may surprise users if wheel/drag modifiers differ from common IntelliJ expectations.
- IntelliJ-native reduces re-learning cost.

### How “bad” is Mirur-style if used directly?

Short answer: **not catastrophic**, but noticeable in polish and adoption friction.

- **Low risk**: correctness of plotted data, fallback-first visualization, and core debug value extraction.
- **Medium risk**: perceived UX quality (looks/feels less native), discoverability of controls, and learnability for IntelliJ-first users.
- **Higher risk (later scale)**: maintainability when IntelliJ UI APIs evolve, if too much custom chrome bypasses platform actions/components.

### Recommendation from these examples

For v0, keep Mirur interaction semantics, but implement them with IntelliJ-native UI primitives where possible:
- toolbar/actions via IntelliJ action system,
- tool window/tab behaviors consistent with JetBrains defaults,
- native keymap/gesture expectations,
- native empty-state and status presentation.

This gives “Mirur behavior, IntelliJ feel,” minimizing both product drift and integration friction.

## 9. Visual style guide (minimal)

- Toolbar height follows default IntelliJ tool window header metrics.
- Plot area margins minimal, with readable axis labels.
- Color maps: start with one perceptually safe default (`Viridis`-like).
- Keep icon set small in v0: Mirur logo + generic IntelliJ icons for refresh/settings/pin.

## 10. Technical architecture sketch (IntelliJ)

### 10.1 Modules (logical)

1. **Debugger integration layer**
   - Reads selected `XValue`/debug value tree nodes.
   - Converts to normalized Mirur data model.

2. **Data model & transforms**
   - Internal structures for 1D/2D numeric buffers.
   - Min/max, histogram bins, optional downsampling.

3. **Renderer layer**
   - Swing/Skia/IntelliJ canvas-based rendering (choose simplest maintainable path).
   - Separate renderers for line/bar/heatmap/histogram.

4. **UI shell**
   - Tool window lifecycle, toolbar actions, state store.

### 10.2 State model

- `SessionState`: connected/disconnected, current debug frame id.
- `DatasetState`: source variable identity, shape, sample/full flags.
- `ViewState`: selected chart mode, axis ranges, pinned flag.

## 11. JVM/debugger compatibility baseline

- **Target support floor**: Java 8 debuggees.
- **Target support range**: Java 8 through current releases.
- **Language scope for v0**: Java only (other languages only when support is trivial).
- **Remote debugging**: support where IntelliJ exposes required value-inspection APIs consistently.

## 12. MVP scope and non-goals

### In scope (MVP)

- One bottom-docked tool window.
- One debugger context-menu action.
- Auto + 4 plot modes (line/bar/heatmap/histogram).
- Pin, refresh, reset axes.
- Basic stats footer.

### Out of scope (later)

- Multi-view tiling and duplicate-view workflows.
- Advanced palettes/gradients UI.
- Complex-number specialized views.
- Export images/CSV.
- 3D arrays or tensor slicing UI.

## 13. Future roadmap (phased)

### Phase 1 (MVP)
- Implement context action + bottom tool window + Auto render.

### Phase 2
- Add gradient chooser, transpose for 2D, and better jagged handling.

### Phase 3
- Add multi-view compare mode (pinned snapshots side-by-side).

### Phase 4
- Add export/share and richer statistics panel.

## 14. Notes on source review limitations

This working copy does not currently include a local `gh-pages` branch or local screenshot assets referenced by `README.md` (`../gh-pages/images/mirur-example.jpg`). The design above is therefore based on repository source, plugin actions, views, and docs in this checkout.

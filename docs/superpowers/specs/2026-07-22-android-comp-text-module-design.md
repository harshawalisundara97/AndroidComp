# Android Comp — Text Module Design

## Context

The skeleton + Buttons module (see
`2026-07-21-android-comp-skeleton-buttons-design.md`) proved the pattern:
category-agnostic navigation, a generic 13-section detail template, and a
per-category playground/preview registry keyed by component id. This design
covers **Milestone 6: Text module** — the second component category, built
entirely on top of that existing infrastructure with no architectural
changes.

Scope is display text only: `Text` and Material 3 typography. `TextField`
and other input widgets are a separate category ("Text Inputs", Milestone 7)
and are explicitly out of scope here.

Out of scope for this pass: `ClickableText`, `AnnotatedString`, selectable
text, and any other advanced text composables — deferred to a later pass if
needed.

## Package Structure (additions only)

```
app/
 data/
   componentcatalog/
     TextComponentCatalog.kt        # new: 5 ComponentSpec entries for Text
   repository/
     ComponentRepositoryImpl.kt     # modified: merges TextComponentCatalog.all
 features/
   text/
     preview/
       TextPreviewRegistry.kt       # new: id -> @Composable (TextPlaygroundState) -> Unit
       TextPreviews.kt              # new: one preview composable per typography role
     playground/
       TextPlaygroundState.kt       # new
       TextPlaygroundViewModel.kt   # new
       TextPlaygroundControls.kt    # new
```

Everything else (`core/navigation`, `core/ui`, `componentdetail`,
`domain/*`) is reused unmodified except where noted below.

## Data Model

No changes to `ComponentSpec`, `ComponentProperty`, or `CodeSample`. The
existing `ComponentCategory.TEXT` enum entry (already declared, unused until
now) is populated for the first time.

## Text Module Content

Five `ComponentSpec` entries, one per Material 3 typography role, each with
all 13 template sections populated:

| id | Title | Typography style used in preview |
|---|---|---|
| `text-display` | Display Text | `MaterialTheme.typography.displayMedium` |
| `text-headline` | Headline Text | `MaterialTheme.typography.headlineMedium` |
| `text-title` | Title Text | `MaterialTheme.typography.titleMedium` |
| `text-body` | Body Text | `MaterialTheme.typography.bodyMedium` |
| `text-label` | Label Text | `MaterialTheme.typography.labelMedium` |

Each entry's Compose Code section shows the corresponding `Text(...)` call
with the matching `style` parameter. XML Code sections show the
`TextView`/`android:textAppearance` equivalent. `relatedComponentIds` link
the five entries to each other (not to Buttons entries).

## Repository Wiring

`ComponentRepositoryImpl.getAll()` changes from returning
`ButtonComponentCatalog.all` to `ButtonComponentCatalog.all + TextComponentCatalog.all`.
`getByCategory()`/`getById()` continue to filter/find over this combined
list — no interface changes, since `ComponentRepository` was already
category-agnostic.

## Playground State Pattern (Text)

`TextPlaygroundState(text: String = "Sample text", bold: Boolean = false, italic: Boolean = false)`,
mirroring `PlaygroundState`'s shape. `TextPlaygroundViewModel` exposes it as
`StateFlow` with `setText`, `setBold`, `setItalic`. `TextPlaygroundControls`
renders a text field plus two switches ("Bold", "Italic").

`TextPreviewRegistry` maps each of the 5 ids above to a
`@Composable (TextPlaygroundState) -> Unit` that renders:

```kotlin
Text(
    text = state.text,
    style = MaterialTheme.typography.<role>,
    fontWeight = if (state.bold) FontWeight.Bold else null,
    fontStyle = if (state.italic) FontStyle.Italic else null,
)
```

This is a distinct state/registry pair from Buttons' `PlaygroundState`/
`ButtonPreviewRegistry` — categories do not share playground shape, since a
button's meaningful controls (label, enabled) and text's (content, weight,
italic) are different by nature. This mirrors the Buttons module's own
per-category pattern rather than introducing a new shared abstraction.

## Component Detail Screen Change

`ComponentDetailViewModel`/`ComponentDetailScreen` currently look up
`ButtonPreviewRegistry` unconditionally by component id. This must
generalize to resolve the correct registry (and correct playground
ViewModel type) based on the fetched `ComponentSpec.category`:

- `category == BUTTONS` → look up `ButtonPreviewRegistry`, use
  `PlaygroundViewModel`/`PlaygroundControls`
- `category == TEXT` → look up `TextPreviewRegistry`, use
  `TextPlaygroundViewModel`/`TextPlaygroundControls`

The exact mechanism (a `when` on category, or a small strategy interface) is
left to the implementation plan — this is the one place introducing a
two-category branch, and the plan should pick whichever keeps
`ComponentDetailScreen` thin and avoids a growing `when` for future
categories, without over-engineering a plugin system for two cases.

## Categories & Search

No changes needed. `CategoriesScreen` already lists all `ComponentCategory`
values and calls `GetComponentsByCategoryUseCase(TEXT)`, which will now
return non-empty results. `SearchComponentsUseCase`'s substring match over
`ComponentSpec.title` already works across the combined catalog.

## Error Handling

Unchanged from the Buttons module — `GetComponentDetailUseCase(id)` returns
`ComponentSpec?`, screen shows a "not found" empty state if null.

## Testing

- Unit tests for `TextPlaygroundViewModel` state transitions (TDD, mirrors
  `PlaygroundViewModelTest`).
- Unit test verifying `ComponentRepositoryImpl.getAll()`/`getByCategory(TEXT)`
  return the expected Text entries once merged (closes the coverage gap
  noted in the Buttons module review, this time for both categories).
- No UI/instrumented tests in this pass, consistent with the Buttons module.

## Milestone Mapping

| Spec Milestone | Covered here |
|---|---|
| 6. Text module | Yes |
| 7. Input module | No — next pass |
| 8. Remaining categories | No |

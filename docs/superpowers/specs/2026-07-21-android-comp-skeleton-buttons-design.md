# Android Comp — Skeleton + Buttons Module Design

## Context

Android Comp is a native Android app that teaches Android UI components through
live, interactive examples (see `Android_Comp_Project_Specification.md`). The
full spec covers 25 component categories and 10 milestones — too large for one
build pass.

This design scopes the **first build** to:

- Project setup + Clean Architecture skeleton (Milestone 1)
- Navigation shell across all 5 top-level destinations (Milestone 2)
- The reusable 13-section component detail template (Milestone 4)
- One fully built category, **Buttons**, to prove the pattern end-to-end
  (Milestone 5)
- A basic (non-fuzzy) Search over the static catalog (Milestone 3, minimal)

Out of scope for this pass: Room / persistence, Favorites persistence, offline
support, remaining 24 categories, XML-vs-Compose comparison tooling beyond
static code display. These are explicitly deferred to later milestones per
the project spec.

## Tech Stack

Kotlin, Jetpack Compose, Material 3, Hilt, Navigation Compose, MVVM, Clean
Architecture. Coil and Room are declared as dependencies now (per project
spec's fixed tech stack) but Room has no schema/usage yet — it's added when
Favorites persistence is built.

## Package Structure

```
app/
 core/
   ui/            # theme (Color/Type/Shape), shared composables:
                   # CodeBlock, SectionHeader, PropertyTable, PlaygroundCard
   navigation/    # NavHost, sealed Route classes, bottom nav bar
   di/            # Hilt modules (RepositoryModule, etc.)
 data/
   componentcatalog/
     ButtonComponentCatalog.kt   # hardcoded catalog of ComponentSpec for Buttons
   repository/
     ComponentRepositoryImpl.kt
 domain/
   model/
     ComponentSpec.kt      # id, name, category, overview, codeSamples, properties, events, ...
     ComponentProperty.kt
     CodeSample.kt         # language (COMPOSE/XML), code string
     ComponentCategory.kt  # enum, only BUTTONS populated this pass
   repository/
     ComponentRepository.kt        # interface
   usecase/
     GetComponentsByCategoryUseCase.kt
     GetComponentDetailUseCase.kt
     SearchComponentsUseCase.kt
 features/
   home/            # HomeScreen: category grid, entry point
   search/          # SearchScreen: text filter over ComponentRepository.getAll()
   categories/       # CategoriesScreen: list of categories (only Buttons enabled)
   favorites/        # FavoritesScreen: UI shell, empty-state only (no persistence)
   settings/          # SettingsScreen: dark/light mode toggle (theme only, no other settings yet)
   componentdetail/   # ComponentDetailScreen: generic 13-section template,
                       # driven by ComponentSpec from the route arg
   buttons/
     preview/         # Composables rendering each button variant for Live Preview
     playground/       # Per-button playground state (ViewModel) + controls
```

Every feature module follows **UI → ViewModel → UseCase → Repository → Data**.
`componentdetail` is category-agnostic — it renders whatever `ComponentSpec`
it's given. `buttons` supplies the actual interactive Compose code (live
preview + playground) since that can't be pure data.

## Data Model

```kotlin
data class ComponentSpec(
    val id: String,                    // "button-filled"
    val category: ComponentCategory,   // BUTTONS
    val title: String,                 // "Filled Button"
    val overview: String,
    val composeCode: CodeSample,
    val xmlCode: CodeSample?,          // null if not applicable
    val viewModelUsage: String?,       // markdown/plain text snippet, nullable
    val properties: List<ComponentProperty>,
    val events: List<String>,
    val bestPractices: List<String>,
    val commonMistakes: List<String>,
    val accessibilityNotes: List<String>,
    val performanceNotes: List<String>,
    val relatedComponentIds: List<String>,
    val minApi: Int                    // for "API compatibility" playground feature
)

data class ComponentProperty(
    val name: String,
    val type: String,
    val defaultValue: String,
    val description: String
)

data class CodeSample(val language: CodeLanguage, val code: String)
enum class CodeLanguage { COMPOSE, XML }
```

`ComponentSpec` does not embed a Composable reference — Live Preview and
Playground rendering is resolved by `id` via a lookup table in the `buttons`
feature module (`ButtonPreviewRegistry: Map<String, @Composable (PlaygroundState) -> Unit>`),
keeping domain models free of UI concerns.

## Buttons Module Content

Material 3 core set: `Button` (filled), `FilledTonalButton`, `OutlinedButton`,
`TextButton`, `ElevatedButton`, `IconButton`. Each gets a full `ComponentSpec`
with all 13 template sections populated.

## Navigation

Sealed `Route` classes under `core/navigation`:

- `Route.Home`, `Route.Search`, `Route.Categories`, `Route.Favorites`, `Route.Settings`
  — bottom nav bar destinations
- `Route.ComponentDetail(componentId: String)` — pushed from Categories/Search/Home,
  not part of bottom nav

`ComponentDetailScreen` receives `componentId` as a nav arg, calls
`GetComponentDetailUseCase(id)`, renders the 13 sections.

## Component Detail Template (13 Sections)

Rendered in this fixed order, each as its own composable in `core/ui` or
`componentdetail`:
1. Overview — text
2. Live Preview — live-rendered composable from `ButtonPreviewRegistry`
3. Interactive Playground — property controls (sliders/switches/dropdowns)
   bound to `PlaygroundState`, re-rendering Live Preview reactively
4. Compose Code — `CodeBlock` with copy-to-clipboard
5. XML Code — `CodeBlock`, section hidden if `xmlCode == null`
6. Activity/ViewModel usage — text/code block, hidden if null
7. Properties — `PropertyTable`
8. Events — bullet list
9. Best Practices — bullet list
10. Common Mistakes — bullet list
11. Accessibility — bullet list
12. Performance Notes — bullet list
13. Related Components — chips linking to other `ComponentSpec` ids (navigates
    to `Route.ComponentDetail`)

## Playground State Pattern

`PlaygroundViewModel` (one per component instance, keyed by componentId) holds
a `PlaygroundState` (e.g. `enabled: Boolean`, `text: String`, `variant: ButtonVariant`)
as `StateFlow`. Playground controls dispatch intents that update this state;
Live Preview composable observes it and recomposes. This same pattern is
reused by every future category's playground.

## Search (minimal)

`SearchComponentsUseCase` does a case-insensitive substring match over
`ComponentSpec.title` across all loaded categories (just Buttons this pass).
No fuzzy matching, no history — that's deferred.

## Error Handling

- `GetComponentDetailUseCase(id)` returns `ComponentSpec?`; screen shows a
  "component not found" empty state if null (defensive against bad nav args,
  not expected in normal flow since ids are internal constants).
- No network/IO in this pass, so no loading/error states beyond that.

## Testing

- Unit tests for use cases (`GetComponentsByCategoryUseCase`,
  `SearchComponentsUseCase`) against the hardcoded `ButtonComponentCatalog`.
- Unit tests for `PlaygroundViewModel` state transitions.
- No UI/instrumented tests in this pass (can be added once the pattern is
  validated manually).

## Milestone Mapping

| Spec Milestone | Covered here |
|---|---|
| 1. Project setup | Yes |
| 2. Navigation | Yes |
| 3. Search | Minimal (substring match) |
| 4. Reusable component page | Yes |
| 5. Buttons module | Yes |
| 6. Text module | No — next pass, reuses this template |
| 7. Input module | No |
| 8. Remaining categories | No |
| 9. Offline support | No — Room deferred |
| 10. Release | No |

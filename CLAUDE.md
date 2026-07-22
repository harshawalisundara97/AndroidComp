# Android Comp — Design System

This is the binding visual design language for this app's UI. Apply it to
all new screens/components and retrofit existing ones to match. It is
inspired by premium FinTech-style mobile design (Apple HIG + Material 3
principles) — use as a design *language*, not a literal copy of any
reference image.

## Style

Modern FinTech-inspired minimalism. Clean, card-based, spacious, premium.
Native platform feel (Material 3 on Android). No glassmorphism, no heavy
gradients, no skeuomorphism/neumorphism, no cluttered layouts, no tiny
touch targets, no complex navigation.

## Visual Language

- Background: white (light) / true dark surface (dark theme)
- Primary text: black / dark gray
- Secondary text: gray
- Success: green
- Warning/progress: orange
- Informational: blue
- Borders: light gray (`#F2F2F2`)
- Shadows: soft only, very low elevation — never heavy

## Spacing (8pt system)

- Screen padding: 20–24dp
- Card padding: 16–20dp
- Section spacing: 24–32dp

## Border Radius

- Cards: 24dp
- Buttons: 18dp
- Inputs: 16dp
- Bottom navigation: 26dp
- Dialogs: 28dp

## Typography

Android: Google Sans / Roboto.

| Role | Size/Weight |
|---|---|
| Heading | 28 Bold |
| Section Title | 20 SemiBold |
| Body | 16 Regular |
| Caption | 13 Regular |
| Small Label | 12 Medium |

## Components (design system target set)

Primary/Secondary/Outlined/Text/Icon Button, FAB, Card, Statistic Card,
Dashboard Card, Progress Card, Search Bar, Text Field, Dropdown, Chip,
Badge, Switch, Checkbox, Radio Button, Tabs, Bottom Sheet, Modal Dialog,
Snackbar, Toast, Circular/Linear Progress, Lists, Settings Row, Empty
State, Error State, Loading Skeleton, Chart/Graph Card (rounded bar/line
charts, minimal axis), Calendar, Date Picker, Time Picker.

Build these incrementally as screens need them — don't pre-build the full
set speculatively.

## Navigation

Floating, rounded bottom navigation bar (26dp corner radius). Destinations:
Home, Explore, Analytics, Notifications, Profile — note this differs from
Android Comp's current 5 tabs (Home, Search, Categories, Favorites,
Settings); map conceptually rather than renaming the app's actual IA
without discussion.

## Animation

Subtle only: fade, scale, slide, card elevation change, ripple, progress
animation, FAB expansion. No flashy/attention-grabbing motion.

## Icons & Illustration

Outlined, rounded-style icons. Flat modern illustrations where needed
(empty states, onboarding).

## Accessibility

- Minimum touch target: 48x48dp
- Contrast: WCAG AA
- Support Dynamic Type / font scaling

## Responsive

Support Android phones and tablets (adaptive layout). iOS sizes are listed
in the original brief for cross-platform parity reference, but this
codebase is Android/Compose only — no iOS target exists here.

## Dark Mode

Full light and dark theme, not just a dimmed light theme.

## Android Implementation Notes

- Material 3, Jetpack Compose, Dynamic Color (`dynamicColorScheme`) where
  available (Android 12+), fallback to a static branded scheme below that.
- Adaptive layouts (phone vs. tablet via `WindowSizeClass`).
- MVVM, state hoisting, reusable composables — consistent with this
  project's existing Clean Architecture (see
  `docs/superpowers/specs/2026-07-21-android-comp-skeleton-buttons-design.md`).

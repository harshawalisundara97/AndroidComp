# Android Comp Skeleton + Buttons Module Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a working Android app skeleton (Clean Architecture, Hilt, Navigation Compose, Material 3) with a bottom-nav shell across Home/Search/Categories/Favorites/Settings, a reusable 13-section component detail template, and one fully populated category — Buttons (6 Material 3 button variants) — to validate the pattern end-to-end.

**Architecture:** MVVM + Clean Architecture with `domain` (pure Kotlin models/usecases/repository interfaces), `data` (hardcoded Kotlin object catalog + repository impl), `core` (navigation, DI, shared UI), and `features` (one package per screen/category) layers. UI → ViewModel → UseCase → Repository → Data, per the project spec.

**Tech Stack:** Kotlin 2.1.0, Jetpack Compose (BOM 2026.06.00), Material 3, Hilt 2.59.2 (KSP), Navigation Compose 2.8.5, Coil 2.7.0 (declared, unused this pass), minSdk 26, targetSdk/compileSdk 35.

## Global Constraints

- Package name: `com.androidcomp.app` (applicationId and root package).
- minSdk 26, targetSdk 35, compileSdk 35 — per Material 3 / Compose baseline requirements.
- Kotlin 2.1.0, AGP 8.7.3, Hilt 2.59.2, Navigation Compose 2.8.5, Compose BOM 2026.06.00 — exact versions, no ranges.
- All DI via Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`) — no manual factories.
- Room and Favorites persistence are explicitly OUT of scope this pass (per design doc) — Favorites screen is UI-only empty state.
- No network/IO calls this pass — all content is hardcoded Kotlin objects in `data/componentcatalog`.
- Every ButtonComponent `ComponentSpec` must populate all 13 template sections (nullable fields `xmlCode`/`viewModelUsage` may be non-null or explicitly null, never omitted).
- Unit tests only this pass (JVM, no instrumented/UI tests) — use JUnit4 + Google Truth for assertions.
- Gradle Kotlin DSL (`build.gradle.kts`), version catalog (`gradle/libs.versions.toml`) for all dependency versions.

---

## File Structure

```
AndroidComp/
 settings.gradle.kts
 build.gradle.kts                          (root)
 gradle/libs.versions.toml
 app/
   build.gradle.kts
   src/main/
     AndroidManifest.xml
     java/com/androidcomp/app/
       AndroidCompApp.kt                    (Application, @HiltAndroidApp)
       MainActivity.kt                       (@AndroidEntryPoint)
       core/
         ui/
           theme/Color.kt
           theme/Type.kt
           theme/Theme.kt                    (AndroidCompTheme)
           CodeBlock.kt
           SectionHeader.kt
           PropertyTable.kt
           BulletList.kt
         navigation/
           Route.kt
           AndroidCompNavHost.kt
           BottomNavBar.kt
         di/
           RepositoryModule.kt
       domain/
         model/
           ComponentCategory.kt
           CodeSample.kt
           ComponentProperty.kt
           ComponentSpec.kt
         repository/
           ComponentRepository.kt
         usecase/
           GetComponentsByCategoryUseCase.kt
           GetComponentDetailUseCase.kt
           SearchComponentsUseCase.kt
       data/
         componentcatalog/
           ButtonComponentCatalog.kt
         repository/
           ComponentRepositoryImpl.kt
       features/
         home/
           HomeScreen.kt
           HomeViewModel.kt
         search/
           SearchScreen.kt
           SearchViewModel.kt
         categories/
           CategoriesScreen.kt
           CategoriesViewModel.kt
         favorites/
           FavoritesScreen.kt
         settings/
           SettingsScreen.kt
           SettingsViewModel.kt
         componentdetail/
           ComponentDetailScreen.kt
           ComponentDetailViewModel.kt
         buttons/
           preview/
             ButtonPreviewRegistry.kt
             ButtonPreviews.kt
           playground/
             PlaygroundState.kt
             PlaygroundViewModel.kt
             PlaygroundControls.kt
   src/test/java/com/androidcomp/app/
     domain/usecase/GetComponentsByCategoryUseCaseTest.kt
     domain/usecase/GetComponentDetailUseCaseTest.kt
     domain/usecase/SearchComponentsUseCaseTest.kt
     features/buttons/playground/PlaygroundViewModelTest.kt
```

---

## Task 1: Project Scaffold (Gradle, Manifest, Application, MainActivity)

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle/libs.versions.toml`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/androidcomp/app/AndroidCompApp.kt`
- Create: `app/src/main/java/com/androidcomp/app/MainActivity.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/ui/theme/Color.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/ui/theme/Type.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/ui/theme/Theme.kt`

**Interfaces:**
- Produces: `AndroidCompTheme(content: @Composable () -> Unit)` composable used by `MainActivity` and every screen preview.

- [ ] **Step 1: Create `settings.gradle.kts`**

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "AndroidComp"
include(":app")
```

- [ ] **Step 2: Create `gradle/libs.versions.toml`**

```toml
[versions]
agp = "8.7.3"
kotlin = "2.1.0"
ksp = "2.1.0-1.0.29"
coreKtx = "1.15.0"
lifecycle = "2.8.7"
activityCompose = "1.9.3"
composeBom = "2026.06.00"
hilt = "2.59.2"
hiltNavigationCompose = "1.2.0"
navigationCompose = "2.8.5"
coil = "2.7.0"
junit = "4.13.2"
truth = "1.4.4"
coroutinesTest = "1.9.0"

[libraries]
core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
truth = { group = "com.google.truth", name = "truth", version.ref = "truth" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutinesTest" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

- [ ] **Step 3: Create root `build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
}
```

- [ ] **Step 4: Create `app/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.androidcomp.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.androidcomp.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.activity.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.navigation.compose)
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
}
```

- [ ] **Step 5: Create `app/src/main/AndroidManifest.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:name=".AndroidCompApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="Android Comp"
        android:theme="@style/Theme.AndroidComp">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.AndroidComp">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

Note: `@mipmap/ic_launcher` and `@style/Theme.AndroidComp` (a minimal `values/themes.xml` splash/base theme) are standard AGP defaults — create a minimal `app/src/main/res/values/themes.xml`:

```xml
<resources>
    <style name="Theme.AndroidComp" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
```

And create `app/src/main/res/values/strings.xml`:

```xml
<resources>
    <string name="app_name">Android Comp</string>
</resources>
```

- [ ] **Step 6: Create `AndroidCompApp.kt`**

```kotlin
package com.androidcomp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidCompApp : Application()
```

- [ ] **Step 7: Create `core/ui/theme/Color.kt`**

```kotlin
package com.androidcomp.app.core.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

- [ ] **Step 8: Create `core/ui/theme/Type.kt`**

```kotlin
package com.androidcomp.app.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
```

- [ ] **Step 9: Create `core/ui/theme/Theme.kt`**

```kotlin
package com.androidcomp.app.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AndroidCompTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
```

- [ ] **Step 10: Create `MainActivity.kt`** (placeholder content, replaced by nav host in Task 5)

```kotlin
package com.androidcomp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.androidcomp.app.core.ui.theme.AndroidCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidCompTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Text("Android Comp")
                }
            }
        }
    }
}
```

- [ ] **Step 11: Build to verify scaffold compiles**

Run: `./gradlew :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 12: Commit**

```bash
git add settings.gradle.kts build.gradle.kts gradle/libs.versions.toml app/build.gradle.kts app/src/main/AndroidManifest.xml app/src/main/res app/src/main/java/com/androidcomp/app/AndroidCompApp.kt app/src/main/java/com/androidcomp/app/MainActivity.kt app/src/main/java/com/androidcomp/app/core/ui/theme
git commit -m "Scaffold Android Comp project with Hilt, Compose, Material 3"
```

---

## Task 2: Domain Models

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/domain/model/ComponentCategory.kt`
- Create: `app/src/main/java/com/androidcomp/app/domain/model/CodeSample.kt`
- Create: `app/src/main/java/com/androidcomp/app/domain/model/ComponentProperty.kt`
- Create: `app/src/main/java/com/androidcomp/app/domain/model/ComponentSpec.kt`

**Interfaces:**
- Produces: `ComponentCategory` enum, `CodeLanguage` enum, `CodeSample` data class, `ComponentProperty` data class, `ComponentSpec` data class — consumed by every later task (catalog, repository, usecases, all UI).

- [ ] **Step 1: Create `ComponentCategory.kt`**

```kotlin
package com.androidcomp.app.domain.model

enum class ComponentCategory(val displayName: String) {
    BUTTONS("Buttons"),
    TEXT("Text"),
    TEXT_INPUTS("Text Inputs"),
    IMAGES("Images"),
    LAYOUTS("Layouts"),
    LISTS("Lists"),
    NAVIGATION("Navigation"),
    DIALOGS("Dialogs"),
    MENUS("Menus"),
    PROGRESS("Progress"),
    SELECTION_CONTROLS("Selection Controls"),
    SLIDERS("Sliders"),
    GESTURES("Gestures"),
    ANIMATIONS("Animations"),
    GRAPHICS("Graphics"),
    CAMERA("Camera"),
    PERMISSIONS("Permissions"),
    NOTIFICATIONS("Notifications"),
    STORAGE("Storage"),
    NETWORKING("Networking"),
    MAPS("Maps"),
    SENSORS("Sensors"),
    MEDIA("Media"),
    MATERIAL_COMPONENTS("Material Components")
}
```

- [ ] **Step 2: Create `CodeSample.kt`**

```kotlin
package com.androidcomp.app.domain.model

enum class CodeLanguage {
    COMPOSE,
    XML
}

data class CodeSample(
    val language: CodeLanguage,
    val code: String
)
```

- [ ] **Step 3: Create `ComponentProperty.kt`**

```kotlin
package com.androidcomp.app.domain.model

data class ComponentProperty(
    val name: String,
    val type: String,
    val defaultValue: String,
    val description: String
)
```

- [ ] **Step 4: Create `ComponentSpec.kt`**

```kotlin
package com.androidcomp.app.domain.model

data class ComponentSpec(
    val id: String,
    val category: ComponentCategory,
    val title: String,
    val overview: String,
    val composeCode: CodeSample,
    val xmlCode: CodeSample?,
    val viewModelUsage: String?,
    val properties: List<ComponentProperty>,
    val events: List<String>,
    val bestPractices: List<String>,
    val commonMistakes: List<String>,
    val accessibilityNotes: List<String>,
    val performanceNotes: List<String>,
    val relatedComponentIds: List<String>,
    val minApi: Int
)
```

- [ ] **Step 5: Build to verify compiles**

Run: `./gradlew :app:compileDebugKotlin`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/domain/model
git commit -m "Add domain models: ComponentSpec, CodeSample, ComponentProperty, ComponentCategory"
```

---

## Task 3: Button Component Catalog (Data Layer)

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/data/componentcatalog/ButtonComponentCatalog.kt`

**Interfaces:**
- Consumes: `ComponentSpec`, `ComponentCategory.BUTTONS`, `CodeSample`, `CodeLanguage`, `ComponentProperty` from Task 2.
- Produces: `object ButtonComponentCatalog { val all: List<ComponentSpec> }` with 6 entries, ids: `"button-filled"`, `"button-filled-tonal"`, `"button-outlined"`, `"button-text"`, `"button-elevated"`, `"button-icon"`.

- [ ] **Step 1: Create `ButtonComponentCatalog.kt`**

```kotlin
package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object ButtonComponentCatalog {

    private val filled = ComponentSpec(
        id = "button-filled",
        category = ComponentCategory.BUTTONS,
        title = "Filled Button",
        overview = "A high-emphasis button with a solid fill color, used for the single most " +
            "important action on a screen (e.g. \"Submit\", \"Confirm\").",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Button(onClick = { /* action */ }) {
                    Text("Filled Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Filled Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onSubmitClicked() {
                viewModel.submit()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state."),
            ComponentProperty("colors", "ButtonColors", "ButtonDefaults.buttonColors()", "Container/content colors."),
            ComponentProperty("shape", "Shape", "ButtonDefaults.shape", "The button's shape.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Use at most one Filled Button per screen for the primary action.",
            "Keep labels short and action-oriented (\"Save\", not \"Save Changes Now\")."
        ),
        commonMistakes = listOf(
            "Using multiple Filled Buttons on the same screen, diluting visual hierarchy.",
            "Disabling the button without explaining why to the user."
        ),
        accessibilityNotes = listOf(
            "Minimum touch target is 48x48dp — Button already meets this by default.",
            "Ensure the label text has sufficient contrast against the container color."
        ),
        performanceNotes = listOf(
            "Avoid creating new lambda instances for onClick on every recomposition; hoist state."
        ),
        relatedComponentIds = listOf("button-filled-tonal", "button-outlined", "button-elevated"),
        minApi = 21
    )

    private val filledTonal = ComponentSpec(
        id = "button-filled-tonal",
        category = ComponentCategory.BUTTONS,
        title = "Filled Tonal Button",
        overview = "A medium-emphasis button using a tonal color, useful for actions that need " +
            "more emphasis than an Outlined Button but less than a Filled Button.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                FilledTonalButton(onClick = { /* action */ }) {
                    Text("Filled Tonal Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.TonalButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Filled Tonal Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onSecondaryActionClicked() {
                viewModel.performSecondaryAction()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state."),
            ComponentProperty("colors", "ButtonColors", "ButtonDefaults.filledTonalButtonColors()", "Container/content colors.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Use for secondary actions that still deserve emphasis, e.g. \"Add to cart\" next to a primary \"Buy now\"."
        ),
        commonMistakes = listOf(
            "Using Filled Tonal and Filled Buttons together without a clear hierarchy reason."
        ),
        accessibilityNotes = listOf(
            "Tonal color contrast is lower than Filled — verify against WCAG AA for the label text."
        ),
        performanceNotes = listOf(
            "Same recomposition considerations as Filled Button."
        ),
        relatedComponentIds = listOf("button-filled", "button-outlined"),
        minApi = 21
    )

    private val outlined = ComponentSpec(
        id = "button-outlined",
        category = ComponentCategory.BUTTONS,
        title = "Outlined Button",
        overview = "A medium-emphasis button with a stroked border and transparent background, " +
            "typically used for alternative or secondary actions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                OutlinedButton(onClick = { /* action */ }) {
                    Text("Outlined Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Outlined Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onCancelClicked() {
                viewModel.cancel()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("border", "BorderStroke?", "ButtonDefaults.outlinedButtonBorder", "The stroke drawn around the button.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Pair with a Filled Button for a clear primary/secondary action pair (e.g. \"Cancel\" / \"Save\")."
        ),
        commonMistakes = listOf(
            "Using an Outlined Button as the only action on a screen when a Filled Button would communicate priority better."
        ),
        accessibilityNotes = listOf(
            "The border alone is not sufficient contrast signal — ensure label text meets contrast requirements independently."
        ),
        performanceNotes = listOf(
            "No additional overhead versus Filled Button; border is drawn via Modifier, not a separate layer."
        ),
        relatedComponentIds = listOf("button-filled", "button-text"),
        minApi = 21
    )

    private val text = ComponentSpec(
        id = "button-text",
        category = ComponentCategory.BUTTONS,
        title = "Text Button",
        overview = "A low-emphasis button with no container, typically used for the least " +
            "important actions, such as in dialogs or cards.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                TextButton(onClick = { /* action */ }) {
                    Text("Text Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.TextButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Text Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onLearnMoreClicked() {
                viewModel.openLearnMore()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Use inside dialogs (e.g. \"Cancel\") or alongside cards where a container would add visual noise."
        ),
        commonMistakes = listOf(
            "Using Text Buttons for primary actions — low emphasis makes them easy to miss."
        ),
        accessibilityNotes = listOf(
            "Touch target must still be 48x48dp minimum even though visually the button looks smaller."
        ),
        performanceNotes = listOf(
            "Lightest-weight button variant — no container draw, minimal overdraw."
        ),
        relatedComponentIds = listOf("button-outlined", "button-filled"),
        minApi = 21
    )

    private val elevated = ComponentSpec(
        id = "button-elevated",
        category = ComponentCategory.BUTTONS,
        title = "Elevated Button",
        overview = "A button with a shadow to convey elevation, typically used when a button " +
            "needs to stand out against a busy or colored background.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                ElevatedButton(onClick = { /* action */ }) {
                    Text("Elevated Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.ElevatedButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Elevated Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onPromotedActionClicked() {
                viewModel.performPromotedAction()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("elevation", "ButtonElevation?", "ButtonDefaults.buttonElevation()", "Shadow elevation per interaction state.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Reserve for buttons placed on top of colored or image backgrounds where a flat button would lack contrast."
        ),
        commonMistakes = listOf(
            "Using Elevated Button on a plain white background — the shadow reads as unnecessary visual noise."
        ),
        accessibilityNotes = listOf(
            "Shadow is a purely visual cue; do not rely on it alone to convey interactivity — labels must be descriptive."
        ),
        performanceNotes = listOf(
            "Shadow rendering adds a small compositing cost — avoid overusing on long scrolling lists."
        ),
        relatedComponentIds = listOf("button-filled", "button-filled-tonal"),
        minApi = 21
    )

    private val icon = ComponentSpec(
        id = "button-icon",
        category = ComponentCategory.BUTTONS,
        title = "Icon Button",
        overview = "A compact, icon-only button for common, space-constrained actions such as " +
            "toolbar or app bar actions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                IconButton(onClick = { /* action */ }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.IconButton"
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    app:icon="@drawable/ic_favorite" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onFavoriteToggled() {
                viewModel.toggleFavorite()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Always pair with a `contentDescription` on the Icon — Icon Button has no visible label."
        ),
        commonMistakes = listOf(
            "Omitting contentDescription, making the button unusable for screen reader users.",
            "Using Icon Button for actions whose meaning isn't obvious from the icon alone."
        ),
        accessibilityNotes = listOf(
            "contentDescription is mandatory, not optional, for this component.",
            "Default size meets the 48x48dp minimum touch target."
        ),
        performanceNotes = listOf(
            "Negligible overhead; icon vector rendering is the only extra cost versus TextButton."
        ),
        relatedComponentIds = listOf("button-text"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(filled, filledTonal, outlined, text, elevated, icon)
}
```

- [ ] **Step 2: Build to verify compiles**

Run: `./gradlew :app:compileDebugKotlin`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/data/componentcatalog
git commit -m "Add hardcoded Button component catalog with 6 Material 3 variants"
```

---

## Task 4: Repository, Use Cases, and Hilt DI Module

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/domain/repository/ComponentRepository.kt`
- Create: `app/src/main/java/com/androidcomp/app/data/repository/ComponentRepositoryImpl.kt`
- Create: `app/src/main/java/com/androidcomp/app/domain/usecase/GetComponentsByCategoryUseCase.kt`
- Create: `app/src/main/java/com/androidcomp/app/domain/usecase/GetComponentDetailUseCase.kt`
- Create: `app/src/main/java/com/androidcomp/app/domain/usecase/SearchComponentsUseCase.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/di/RepositoryModule.kt`
- Test: `app/src/test/java/com/androidcomp/app/domain/usecase/GetComponentsByCategoryUseCaseTest.kt`
- Test: `app/src/test/java/com/androidcomp/app/domain/usecase/GetComponentDetailUseCaseTest.kt`
- Test: `app/src/test/java/com/androidcomp/app/domain/usecase/SearchComponentsUseCaseTest.kt`

**Interfaces:**
- Consumes: `ComponentSpec`, `ComponentCategory` from Task 2; `ButtonComponentCatalog.all` from Task 3.
- Produces: `ComponentRepository` interface with `getAll(): List<ComponentSpec>`, `getByCategory(category: ComponentCategory): List<ComponentSpec>`, `getById(id: String): ComponentSpec?`. `GetComponentsByCategoryUseCase(category): List<ComponentSpec>`, `GetComponentDetailUseCase(id): ComponentSpec?`, `SearchComponentsUseCase(query): List<ComponentSpec>` — all consumed by ViewModels in Tasks 5–8.

- [ ] **Step 1: Write failing tests for use cases**

Create `app/src/test/java/com/androidcomp/app/domain/usecase/GetComponentsByCategoryUseCaseTest.kt`:

```kotlin
package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.repository.ComponentRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepository(
    private val specs: List<com.androidcomp.app.domain.model.ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class GetComponentsByCategoryUseCaseTest {

    @Test
    fun `returns only specs matching the requested category`() {
        val buttonSpec = com.androidcomp.app.domain.model.ComponentSpec(
            id = "button-filled", category = ComponentCategory.BUTTONS, title = "Filled Button",
            overview = "", composeCode = com.androidcomp.app.domain.model.CodeSample(com.androidcomp.app.domain.model.CodeLanguage.COMPOSE, ""),
            xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
            bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
            performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
        )
        val textSpec = buttonSpec.copy(id = "text-label", category = ComponentCategory.TEXT, title = "Label")
        val repository = FakeComponentRepository(listOf(buttonSpec, textSpec))
        val useCase = GetComponentsByCategoryUseCase(repository)

        val result = useCase(ComponentCategory.BUTTONS)

        assertThat(result).containsExactly(buttonSpec)
    }
}
```

- [ ] **Step 2: Run test to verify it fails (use case doesn't exist)**

Run: `./gradlew :app:testDebugUnitTest --tests "*GetComponentsByCategoryUseCaseTest*"`
Expected: FAIL — compilation error, `GetComponentsByCategoryUseCase` and `ComponentRepository` unresolved.

- [ ] **Step 3: Create `domain/repository/ComponentRepository.kt`**

```kotlin
package com.androidcomp.app.domain.repository

import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec

interface ComponentRepository {
    fun getAll(): List<ComponentSpec>
    fun getByCategory(category: ComponentCategory): List<ComponentSpec>
    fun getById(id: String): ComponentSpec?
}
```

- [ ] **Step 4: Create `domain/usecase/GetComponentsByCategoryUseCase.kt`**

```kotlin
package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class GetComponentsByCategoryUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(category: ComponentCategory): List<ComponentSpec> =
        repository.getByCategory(category)
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "*GetComponentsByCategoryUseCaseTest*"`
Expected: PASS

- [ ] **Step 6: Write failing test for `GetComponentDetailUseCase`**

Create `app/src/test/java/com/androidcomp/app/domain/usecase/GetComponentDetailUseCaseTest.kt`:

```kotlin
package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepository(
    private val specs: List<ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class GetComponentDetailUseCaseTest {

    private val spec = ComponentSpec(
        id = "button-filled", category = ComponentCategory.BUTTONS, title = "Filled Button",
        overview = "", composeCode = CodeSample(CodeLanguage.COMPOSE, ""),
        xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
        bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
        performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
    )

    @Test
    fun `returns the spec matching the given id`() {
        val useCase = GetComponentDetailUseCase(FakeComponentRepository(listOf(spec)))

        val result = useCase("button-filled")

        assertThat(result).isEqualTo(spec)
    }

    @Test
    fun `returns null when no spec matches the id`() {
        val useCase = GetComponentDetailUseCase(FakeComponentRepository(listOf(spec)))

        val result = useCase("does-not-exist")

        assertThat(result).isNull()
    }
}
```

- [ ] **Step 7: Run test to verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "*GetComponentDetailUseCaseTest*"`
Expected: FAIL — `GetComponentDetailUseCase` unresolved.

- [ ] **Step 8: Create `domain/usecase/GetComponentDetailUseCase.kt`**

```kotlin
package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class GetComponentDetailUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(id: String): ComponentSpec? = repository.getById(id)
}
```

- [ ] **Step 9: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "*GetComponentDetailUseCaseTest*"`
Expected: PASS

- [ ] **Step 10: Write failing test for `SearchComponentsUseCase`**

Create `app/src/test/java/com/androidcomp/app/domain/usecase/SearchComponentsUseCaseTest.kt`:

```kotlin
package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepository(
    private val specs: List<ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class SearchComponentsUseCaseTest {

    private fun spec(id: String, title: String) = ComponentSpec(
        id = id, category = ComponentCategory.BUTTONS, title = title,
        overview = "", composeCode = CodeSample(CodeLanguage.COMPOSE, ""),
        xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
        bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
        performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
    )

    @Test
    fun `matches titles case-insensitively by substring`() {
        val filled = spec("button-filled", "Filled Button")
        val outlined = spec("button-outlined", "Outlined Button")
        val useCase = SearchComponentsUseCase(FakeComponentRepository(listOf(filled, outlined)))

        val result = useCase("filled")

        assertThat(result).containsExactly(filled)
    }

    @Test
    fun `returns empty list when query matches nothing`() {
        val filled = spec("button-filled", "Filled Button")
        val useCase = SearchComponentsUseCase(FakeComponentRepository(listOf(filled)))

        val result = useCase("zzz")

        assertThat(result).isEmpty()
    }
}
```

- [ ] **Step 11: Run test to verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "*SearchComponentsUseCaseTest*"`
Expected: FAIL — `SearchComponentsUseCase` unresolved.

- [ ] **Step 12: Create `domain/usecase/SearchComponentsUseCase.kt`**

```kotlin
package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class SearchComponentsUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(query: String): List<ComponentSpec> =
        repository.getAll().filter { it.title.contains(query, ignoreCase = true) }
}
```

- [ ] **Step 13: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "*SearchComponentsUseCaseTest*"`
Expected: PASS

- [ ] **Step 14: Create `data/repository/ComponentRepositoryImpl.kt`**

```kotlin
package com.androidcomp.app.data.repository

import com.androidcomp.app.data.componentcatalog.ButtonComponentCatalog
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class ComponentRepositoryImpl @Inject constructor() : ComponentRepository {

    private val catalog: List<ComponentSpec> = ButtonComponentCatalog.all

    override fun getAll(): List<ComponentSpec> = catalog

    override fun getByCategory(category: ComponentCategory): List<ComponentSpec> =
        catalog.filter { it.category == category }

    override fun getById(id: String): ComponentSpec? =
        catalog.find { it.id == id }
}
```

- [ ] **Step 15: Create `core/di/RepositoryModule.kt`**

```kotlin
package com.androidcomp.app.core.di

import com.androidcomp.app.data.repository.ComponentRepositoryImpl
import com.androidcomp.app.domain.repository.ComponentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindComponentRepository(
        impl: ComponentRepositoryImpl
    ): ComponentRepository
}
```

- [ ] **Step 16: Run full unit test suite**

Run: `./gradlew :app:testDebugUnitTest`
Expected: `BUILD SUCCESSFUL`, all tests pass.

- [ ] **Step 17: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/domain/repository app/src/main/java/com/androidcomp/app/domain/usecase app/src/main/java/com/androidcomp/app/data/repository app/src/main/java/com/androidcomp/app/core/di app/src/test
git commit -m "Add ComponentRepository, use cases, and Hilt DI module with tests"
```

---

## Task 5: Navigation Shell (Routes, NavHost, Bottom Nav, MainActivity wiring)

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/core/navigation/Route.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/navigation/BottomNavBar.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/navigation/AndroidCompNavHost.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/home/HomeScreen.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/categories/CategoriesScreen.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/favorites/FavoritesScreen.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/settings/SettingsScreen.kt`
- Modify: `app/src/main/java/com/androidcomp/app/MainActivity.kt`

**Interfaces:**
- Produces: `sealed class Route` with `Home`, `Search`, `Categories`, `Favorites`, `Settings` (bottom-nav routes, no args) and `ComponentDetail(componentId: String)` (pushed route). `AndroidCompNavHost(navController: NavHostController)` composable. `HomeScreen(onCategoryClick: (ComponentCategory) -> Unit)`, `CategoriesScreen(onComponentClick: (String) -> Unit)` stubs — Search and ComponentDetail screens filled in Tasks 6–7.

- [ ] **Step 1: Create `core/navigation/Route.kt`**

```kotlin
package com.androidcomp.app.core.navigation

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Search : Route("search")
    data object Categories : Route("categories")
    data object Favorites : Route("favorites")
    data object Settings : Route("settings")

    data class ComponentDetail(val componentId: String) : Route("component_detail") {
        companion object {
            const val ARG_COMPONENT_ID = "componentId"
            const val ROUTE_PATTERN = "component_detail/{$ARG_COMPONENT_ID}"
            fun buildRoute(componentId: String) = "component_detail/$componentId"
        }
    }
}

val bottomNavRoutes = listOf(Route.Home, Route.Search, Route.Categories, Route.Favorites, Route.Settings)
```

- [ ] **Step 2: Create `core/navigation/BottomNavBar.kt`**

```kotlin
package com.androidcomp.app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private fun iconFor(route: Route): ImageVector = when (route) {
    Route.Home -> Icons.Filled.Home
    Route.Search -> Icons.Filled.Search
    Route.Categories -> Icons.Filled.Category
    Route.Favorites -> Icons.Filled.Favorite
    Route.Settings -> Icons.Filled.Settings
    else -> Icons.Filled.Home
}

private fun labelFor(route: Route): String = when (route) {
    Route.Home -> "Home"
    Route.Search -> "Search"
    Route.Categories -> "Categories"
    Route.Favorites -> "Favorites"
    Route.Settings -> "Settings"
    else -> ""
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavRoutes.forEach { route ->
            val selected = currentDestination?.hierarchy?.any { it.route == route.path } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route.path) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(iconFor(route), contentDescription = labelFor(route)) },
                label = { Text(labelFor(route)) }
            )
        }
    }
}
```

- [ ] **Step 3: Create `features/home/HomeScreen.kt`**

```kotlin
package com.androidcomp.app.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onComponentClick: (String) -> Unit) {
    Scaffold { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Android Comp")
            Text("Browse categories to explore live Android UI components.")
        }
    }
}
```

- [ ] **Step 4: Create `features/categories/CategoriesScreen.kt`**

```kotlin
package com.androidcomp.app.features.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriesScreen(
    onComponentClick: (String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val components by viewModel.buttonComponents.collectAsState()

    Scaffold { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(components, key = { it.id }) { spec ->
                ListItem(
                    headlineContent = { Text(spec.title) },
                    modifier = Modifier.clickable { onComponentClick(spec.id) }
                )
            }
        }
    }
}
```

- [ ] **Step 5: Create `features/categories/CategoriesViewModel.kt`**

```kotlin
package com.androidcomp.app.features.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.GetComponentsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    getComponentsByCategoryUseCase: GetComponentsByCategoryUseCase
) : ViewModel() {

    private val _buttonComponents = MutableStateFlow<List<ComponentSpec>>(emptyList())
    val buttonComponents: StateFlow<List<ComponentSpec>> = _buttonComponents.asStateFlow()

    init {
        _buttonComponents.value = getComponentsByCategoryUseCase(ComponentCategory.BUTTONS)
    }
}
```

- [ ] **Step 6: Create `features/favorites/FavoritesScreen.kt`**

```kotlin
package com.androidcomp.app.features.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FavoritesScreen() {
    Scaffold { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("No favorites yet — this comes with persistence in a later milestone.")
        }
    }
}
```

- [ ] **Step 7: Create `features/settings/SettingsScreen.kt`**

```kotlin
package com.androidcomp.app.features.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val darkMode by viewModel.darkModeEnabled.collectAsState()

    Scaffold { padding ->
        Row(Modifier.padding(padding).padding(16.dp)) {
            Text("Dark mode")
            Switch(checked = darkMode, onCheckedChange = viewModel::setDarkMode)
        }
    }
}
```

- [ ] **Step 8: Create `features/settings/SettingsViewModel.kt`**

```kotlin
package com.androidcomp.app.features.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
    }
}
```

- [ ] **Step 9: Create `core/navigation/AndroidCompNavHost.kt`** (Search and ComponentDetail wired as placeholders, replaced in Tasks 6–7)

```kotlin
package com.androidcomp.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidcomp.app.features.categories.CategoriesScreen
import com.androidcomp.app.features.favorites.FavoritesScreen
import com.androidcomp.app.features.home.HomeScreen
import com.androidcomp.app.features.settings.SettingsScreen

@Composable
fun AndroidCompNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Route.Home.path) {
        composable(Route.Home.path) {
            HomeScreen(onComponentClick = { id ->
                navController.navigate(Route.ComponentDetail.buildRoute(id))
            })
        }
        composable(Route.Search.path) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Search — wired in a later task")
            }
        }
        composable(Route.Categories.path) {
            CategoriesScreen(onComponentClick = { id ->
                navController.navigate(Route.ComponentDetail.buildRoute(id))
            })
        }
        composable(Route.Favorites.path) {
            FavoritesScreen()
        }
        composable(Route.Settings.path) {
            SettingsScreen()
        }
        composable(
            route = Route.ComponentDetail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Route.ComponentDetail.ARG_COMPONENT_ID) { type = NavType.StringType })
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Component detail — wired in a later task")
            }
        }
    }
}
```

- [ ] **Step 10: Modify `MainActivity.kt` to wire nav host + bottom nav**

Replace the full file contents:

```kotlin
package com.androidcomp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.androidcomp.app.core.navigation.AndroidCompNavHost
import com.androidcomp.app.core.navigation.BottomNavBar
import com.androidcomp.app.core.ui.theme.AndroidCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidCompTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { padding ->
                    Modifier.padding(padding)
                    AndroidCompNavHost(navController)
                }
            }
        }
    }
}
```

- [ ] **Step 11: Add Compose Material Icons Extended dependency** (needed for `Icons.Filled.Category`)

In `gradle/libs.versions.toml`, add under `[libraries]`:

```toml
compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
```

In `app/build.gradle.kts`, add to `dependencies`:

```kotlin
implementation(libs.compose.material.icons.extended)
```

- [ ] **Step 12: Build and install to verify navigation compiles and runs**

Run: `./gradlew :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 13: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/core/navigation app/src/main/java/com/androidcomp/app/features/home app/src/main/java/com/androidcomp/app/features/categories app/src/main/java/com/androidcomp/app/features/favorites app/src/main/java/com/androidcomp/app/features/settings app/src/main/java/com/androidcomp/app/MainActivity.kt gradle/libs.versions.toml app/build.gradle.kts
git commit -m "Add navigation shell: 5-tab bottom nav plus component detail route"
```

---

## Task 6: Search Screen

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/features/search/SearchScreen.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/search/SearchViewModel.kt`
- Modify: `app/src/main/java/com/androidcomp/app/core/navigation/AndroidCompNavHost.kt`

**Interfaces:**
- Consumes: `SearchComponentsUseCase` from Task 4.
- Produces: `SearchScreen(onComponentClick: (String) -> Unit)` — wired into `Route.Search` in the nav host, replacing the Task 5 placeholder.

- [ ] **Step 1: Create `features/search/SearchViewModel.kt`**

```kotlin
package com.androidcomp.app.features.search

import androidx.lifecycle.ViewModel
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.SearchComponentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchComponentsUseCase: SearchComponentsUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<ComponentSpec>>(emptyList())
    val results: StateFlow<List<ComponentSpec>> = _results.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        _results.value = if (newQuery.isBlank()) emptyList() else searchComponentsUseCase(newQuery)
    }
}
```

- [ ] **Step 2: Create `features/search/SearchScreen.kt`**

```kotlin
package com.androidcomp.app.features.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchScreen(
    onComponentClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()

    Scaffold { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChanged,
                label = { Text("Search components") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn {
                items(results, key = { it.id }) { spec ->
                    ListItem(
                        headlineContent = { Text(spec.title) },
                        modifier = Modifier.clickable { onComponentClick(spec.id) }
                    )
                }
            }
        }
    }
}
```

- [ ] **Step 3: Modify `AndroidCompNavHost.kt` to wire Search**

Replace the Search placeholder block:

```kotlin
        composable(Route.Search.path) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Search — wired in a later task")
            }
        }
```

with:

```kotlin
        composable(Route.Search.path) {
            SearchScreen(onComponentClick = { id ->
                navController.navigate(Route.ComponentDetail.buildRoute(id))
            })
        }
```

And add the import:

```kotlin
import com.androidcomp.app.features.search.SearchScreen
```

- [ ] **Step 4: Build to verify compiles**

Run: `./gradlew :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/features/search app/src/main/java/com/androidcomp/app/core/navigation/AndroidCompNavHost.kt
git commit -m "Add Search screen with live substring filtering"
```

---

## Task 7: Shared UI Components (CodeBlock, SectionHeader, PropertyTable, BulletList)

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/core/ui/SectionHeader.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/ui/CodeBlock.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/ui/PropertyTable.kt`
- Create: `app/src/main/java/com/androidcomp/app/core/ui/BulletList.kt`

**Interfaces:**
- Consumes: `ComponentProperty`, `CodeSample` from Task 2.
- Produces: `SectionHeader(title: String)`, `CodeBlock(codeSample: CodeSample)` (with copy-to-clipboard), `PropertyTable(properties: List<ComponentProperty>)`, `BulletList(items: List<String>)` — all consumed by `ComponentDetailScreen` in Task 8.

- [ ] **Step 1: Create `core/ui/SectionHeader.kt`**

```kotlin
package com.androidcomp.app.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}
```

- [ ] **Step 2: Create `core/ui/CodeBlock.kt`**

```kotlin
package com.androidcomp.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun CodeBlock(codeSample: CodeSample) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = codeSample.code,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { clipboardManager.setText(AnnotatedString(codeSample.code)) }) {
            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy code")
        }
    }
}
```

- [ ] **Step 3: Create `core/ui/PropertyTable.kt`**

```kotlin
package com.androidcomp.app.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.domain.model.ComponentProperty

@Composable
fun PropertyTable(properties: List<ComponentProperty>) {
    Column(Modifier.fillMaxWidth()) {
        properties.forEach { property ->
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.weight(1f)) {
                    Text(property.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${property.type} (default: ${property.defaultValue})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(property.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
```

- [ ] **Step 4: Create `core/ui/BulletList.kt`**

```kotlin
package com.androidcomp.app.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BulletList(items: List<String>) {
    Column {
        items.forEach { item ->
            Row(Modifier.padding(vertical = 2.dp)) {
                Text("• ")
                Text(item)
            }
        }
    }
}
```

- [ ] **Step 5: Build to verify compiles**

Run: `./gradlew :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/core/ui/SectionHeader.kt app/src/main/java/com/androidcomp/app/core/ui/CodeBlock.kt app/src/main/java/com/androidcomp/app/core/ui/PropertyTable.kt app/src/main/java/com/androidcomp/app/core/ui/BulletList.kt
git commit -m "Add shared UI components for the component detail template"
```

---

## Task 8: Button Playground State + Preview Registry

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/features/buttons/playground/PlaygroundState.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/buttons/playground/PlaygroundViewModel.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/buttons/playground/PlaygroundControls.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/buttons/preview/ButtonPreviews.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/buttons/preview/ButtonPreviewRegistry.kt`
- Test: `app/src/test/java/com/androidcomp/app/features/buttons/playground/PlaygroundViewModelTest.kt`

**Interfaces:**
- Produces: `PlaygroundState(label: String, enabled: Boolean)`. `PlaygroundViewModel` with `state: StateFlow<PlaygroundState>`, `setLabel(String)`, `setEnabled(Boolean)`. `PlaygroundControls(state: PlaygroundState, onLabelChange: (String) -> Unit, onEnabledChange: (Boolean) -> Unit)`. `ButtonPreviewRegistry: Map<String, @Composable (PlaygroundState) -> Unit>` keyed by the 6 button component ids from Task 3. Consumed by `ComponentDetailScreen` in Task 9.

- [ ] **Step 1: Write failing test for `PlaygroundViewModel`**

Create `app/src/test/java/com/androidcomp/app/features/buttons/playground/PlaygroundViewModelTest.kt`:

```kotlin
package com.androidcomp.app.features.buttons.playground

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaygroundViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default state has enabled true and default label`() {
        val viewModel = PlaygroundViewModel()

        val state = viewModel.state.value

        assertThat(state.enabled).isTrue()
        assertThat(state.label).isEqualTo("Button")
    }

    @Test
    fun `setLabel updates state label`() {
        val viewModel = PlaygroundViewModel()

        viewModel.setLabel("Click me")

        assertThat(viewModel.state.value.label).isEqualTo("Click me")
    }

    @Test
    fun `setEnabled updates state enabled flag`() {
        val viewModel = PlaygroundViewModel()

        viewModel.setEnabled(false)

        assertThat(viewModel.state.value.enabled).isFalse()
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "*PlaygroundViewModelTest*"`
Expected: FAIL — `PlaygroundViewModel` and `PlaygroundState` unresolved.

- [ ] **Step 3: Create `features/buttons/playground/PlaygroundState.kt`**

```kotlin
package com.androidcomp.app.features.buttons.playground

data class PlaygroundState(
    val label: String = "Button",
    val enabled: Boolean = true
)
```

- [ ] **Step 4: Create `features/buttons/playground/PlaygroundViewModel.kt`**

```kotlin
package com.androidcomp.app.features.buttons.playground

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlaygroundViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(PlaygroundState())
    val state: StateFlow<PlaygroundState> = _state.asStateFlow()

    fun setLabel(label: String) {
        _state.value = _state.value.copy(label = label)
    }

    fun setEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(enabled = enabled)
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "*PlaygroundViewModelTest*"`
Expected: PASS

- [ ] **Step 6: Create `features/buttons/playground/PlaygroundControls.kt`**

```kotlin
package com.androidcomp.app.features.buttons.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlaygroundControls(
    state: PlaygroundState,
    onLabelChange: (String) -> Unit,
    onEnabledChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.label,
            onValueChange = onLabelChange,
            label = { Text("Label") },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Text("Enabled")
            Switch(checked = state.enabled, onCheckedChange = onEnabledChange)
        }
    }
}
```

- [ ] **Step 7: Create `features/buttons/preview/ButtonPreviews.kt`**

```kotlin
package com.androidcomp.app.features.buttons.preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.androidcomp.app.features.buttons.playground.PlaygroundState

@Composable
fun FilledButtonPreview(state: PlaygroundState) {
    Button(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun FilledTonalButtonPreview(state: PlaygroundState) {
    FilledTonalButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun OutlinedButtonPreview(state: PlaygroundState) {
    OutlinedButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun TextButtonPreview(state: PlaygroundState) {
    TextButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun ElevatedButtonPreview(state: PlaygroundState) {
    ElevatedButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun IconButtonPreview(state: PlaygroundState) {
    IconButton(onClick = {}, enabled = state.enabled) {
        Icon(Icons.Filled.Favorite, contentDescription = state.label)
    }
}
```

- [ ] **Step 8: Create `features/buttons/preview/ButtonPreviewRegistry.kt`**

```kotlin
package com.androidcomp.app.features.buttons.preview

import androidx.compose.runtime.Composable
import com.androidcomp.app.features.buttons.playground.PlaygroundState

object ButtonPreviewRegistry {
    val previews: Map<String, @Composable (PlaygroundState) -> Unit> = mapOf(
        "button-filled" to { state -> FilledButtonPreview(state) },
        "button-filled-tonal" to { state -> FilledTonalButtonPreview(state) },
        "button-outlined" to { state -> OutlinedButtonPreview(state) },
        "button-text" to { state -> TextButtonPreview(state) },
        "button-elevated" to { state -> ElevatedButtonPreview(state) },
        "button-icon" to { state -> IconButtonPreview(state) }
    )
}
```

- [ ] **Step 9: Run full test suite and build**

Run: `./gradlew :app:testDebugUnitTest :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`, all tests pass.

- [ ] **Step 10: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/features/buttons app/src/test/java/com/androidcomp/app/features/buttons
git commit -m "Add Button playground state, controls, and preview registry"
```

---

## Task 9: Component Detail Screen (13-Section Template) + Final Navigation Wiring

**Files:**
- Create: `app/src/main/java/com/androidcomp/app/features/componentdetail/ComponentDetailViewModel.kt`
- Create: `app/src/main/java/com/androidcomp/app/features/componentdetail/ComponentDetailScreen.kt`
- Modify: `app/src/main/java/com/androidcomp/app/core/navigation/AndroidCompNavHost.kt`

**Interfaces:**
- Consumes: `GetComponentDetailUseCase` (Task 4), `SectionHeader`/`CodeBlock`/`PropertyTable`/`BulletList` (Task 7), `PlaygroundViewModel`/`PlaygroundControls` (Task 8), `ButtonPreviewRegistry` (Task 8).
- Produces: `ComponentDetailScreen(componentId: String, onRelatedComponentClick: (String) -> Unit)` — final screen, wired into `Route.ComponentDetail` in the nav host, replacing the Task 5 placeholder.

- [ ] **Step 1: Create `features/componentdetail/ComponentDetailViewModel.kt`**

```kotlin
package com.androidcomp.app.features.componentdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.androidcomp.app.core.navigation.Route
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.GetComponentDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ComponentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getComponentDetailUseCase: GetComponentDetailUseCase
) : ViewModel() {

    private val _spec = MutableStateFlow<ComponentSpec?>(null)
    val spec: StateFlow<ComponentSpec?> = _spec.asStateFlow()

    init {
        val componentId: String = checkNotNull(
            savedStateHandle[Route.ComponentDetail.ARG_COMPONENT_ID]
        )
        _spec.value = getComponentDetailUseCase(componentId)
    }
}
```

- [ ] **Step 2: Create `features/componentdetail/ComponentDetailScreen.kt`**

```kotlin
package com.androidcomp.app.features.componentdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidcomp.app.core.ui.BulletList
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.core.ui.PropertyTable
import com.androidcomp.app.core.ui.SectionHeader
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.features.buttons.playground.PlaygroundControls
import com.androidcomp.app.features.buttons.playground.PlaygroundViewModel
import com.androidcomp.app.features.buttons.preview.ButtonPreviewRegistry

@Composable
fun ComponentDetailScreen(
    onRelatedComponentClick: (String) -> Unit,
    detailViewModel: ComponentDetailViewModel = hiltViewModel(),
    playgroundViewModel: PlaygroundViewModel = hiltViewModel()
) {
    val spec by detailViewModel.spec.collectAsState()
    val playgroundState by playgroundViewModel.state.collectAsState()

    Scaffold { padding ->
        val currentSpec = spec
        if (currentSpec == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Component not found")
            }
            return@Scaffold
        }

        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            item { ComponentDetailContent(currentSpec, playgroundState, playgroundViewModel, onRelatedComponentClick) }
        }
    }
}

@Composable
private fun ComponentDetailContent(
    spec: ComponentSpec,
    playgroundState: com.androidcomp.app.features.buttons.playground.PlaygroundState,
    playgroundViewModel: PlaygroundViewModel,
    onRelatedComponentClick: (String) -> Unit
) {
    Column {
        Text(spec.title)

        // 1. Overview
        SectionHeader("Overview")
        Text(spec.overview)

        // 2. Live Preview
        SectionHeader("Live Preview")
        ButtonPreviewRegistry.previews[spec.id]?.invoke(playgroundState)

        // 3. Interactive Playground
        SectionHeader("Interactive Playground")
        PlaygroundControls(
            state = playgroundState,
            onLabelChange = playgroundViewModel::setLabel,
            onEnabledChange = playgroundViewModel::setEnabled
        )

        // 4. Compose Code
        SectionHeader("Compose Code")
        CodeBlock(spec.composeCode)

        // 5. XML Code
        spec.xmlCode?.let { xml ->
            SectionHeader("XML Code")
            CodeBlock(xml)
        }

        // 6. Activity/ViewModel usage
        spec.viewModelUsage?.let { usage ->
            SectionHeader("Activity/ViewModel Usage")
            Text(usage)
        }

        // 7. Properties
        SectionHeader("Properties")
        PropertyTable(spec.properties)

        // 8. Events
        SectionHeader("Events")
        BulletList(spec.events)

        // 9. Best Practices
        SectionHeader("Best Practices")
        BulletList(spec.bestPractices)

        // 10. Common Mistakes
        SectionHeader("Common Mistakes")
        BulletList(spec.commonMistakes)

        // 11. Accessibility
        SectionHeader("Accessibility")
        BulletList(spec.accessibilityNotes)

        // 12. Performance Notes
        SectionHeader("Performance Notes")
        BulletList(spec.performanceNotes)

        // 13. Related Components
        SectionHeader("Related Components")
        spec.relatedComponentIds.forEach { relatedId ->
            AssistChip(onClick = { onRelatedComponentClick(relatedId) }, label = { Text(relatedId) })
        }
    }
}
```

- [ ] **Step 3: Modify `AndroidCompNavHost.kt` to wire ComponentDetail**

Replace the ComponentDetail placeholder block:

```kotlin
        composable(
            route = Route.ComponentDetail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Route.ComponentDetail.ARG_COMPONENT_ID) { type = NavType.StringType })
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Component detail — wired in a later task")
            }
        }
```

with:

```kotlin
        composable(
            route = Route.ComponentDetail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Route.ComponentDetail.ARG_COMPONENT_ID) { type = NavType.StringType })
        ) {
            ComponentDetailScreen(onRelatedComponentClick = { id ->
                navController.navigate(Route.ComponentDetail.buildRoute(id))
            })
        }
```

And add the import:

```kotlin
import com.androidcomp.app.features.componentdetail.ComponentDetailScreen
```

- [ ] **Step 4: Build to verify full app compiles**

Run: `./gradlew :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5: Run full unit test suite**

Run: `./gradlew :app:testDebugUnitTest`
Expected: `BUILD SUCCESSFUL`, all tests pass (use cases + PlaygroundViewModel).

- [ ] **Step 6: Install and manually verify on device/emulator**

Run: `./gradlew :app:installDebug`

Manually verify:
- App launches to Home tab with bottom nav visible across all 5 tabs.
- Categories tab lists the 6 Button components.
- Tapping a button component opens Component Detail with all 13 sections visible, Live Preview renders the real button, Playground label/enabled controls update the Live Preview live, Compose/XML code blocks show with working copy button, Related Components chips navigate to other button detail screens.
- Search tab filters button titles as you type.
- Favorites tab shows the empty-state message.
- Settings tab toggles dark mode and the whole app theme switches.

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/androidcomp/app/features/componentdetail app/src/main/java/com/androidcomp/app/core/navigation/AndroidCompNavHost.kt
git commit -m "Add Component Detail screen with full 13-section template, wire final navigation"
```

---

## Self-Review Notes

**Spec coverage:** Every design-doc section maps to a task — package structure (Tasks 1–9), data model (Task 2), Buttons content (Task 3), navigation (Tasks 5–6, 9), 13-section template (Task 9), playground state pattern (Task 8), minimal search (Task 6), error handling (Task 9 Step 2, "Component not found" state), testing (Tasks 4, 8 unit tests). Room/Favorites persistence and remaining categories are confirmed out of scope per the design doc and are not tasked here.

**Type consistency verified:** `ComponentSpec.id` (String) used consistently across `ButtonComponentCatalog`, `ComponentRepository`, all three use cases, `Route.ComponentDetail.componentId`, and `ButtonPreviewRegistry` keys. `PlaygroundState(label, enabled)` signature consistent between `PlaygroundViewModel`, `PlaygroundControls`, and all 6 `*ButtonPreview` composables in Task 8.

**No placeholders:** All catalog content, code samples, and test assertions are fully written out — no TBD/TODO markers.

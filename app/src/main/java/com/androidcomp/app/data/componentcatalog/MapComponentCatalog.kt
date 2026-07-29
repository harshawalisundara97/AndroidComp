package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object MapComponentCatalog {

    private val googleMapCompose = ComponentSpec(
        id = "map-google",
        category = ComponentCategory.MAPS,
        title = "Google Maps (Compose)",
        overview = "Renders an interactive Google Map inside a Compose hierarchy using the " +
            "official maps-compose library, driven by a remembered CameraPositionState for " +
            "camera control.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(37.4220, -122.0841), 14f)
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false)
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(37.4220, -122.0841)),
                        title = "Googleplex"
                    )
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <fragment
                    android:id="@+id/map"
                    android:name="com.google.android.gms.maps.SupportMapFragment"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent" />
            """.trimIndent()
        ),
        viewModelUsage = """
            class MapViewModel : ViewModel() {
                private val _markerPosition = MutableStateFlow(LatLng(37.4220, -122.0841))
                val markerPosition: StateFlow<LatLng> = _markerPosition.asStateFlow()

                fun onLocationUpdated(latLng: LatLng) {
                    _markerPosition.value = latLng
                }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("cameraPositionState", "CameraPositionState", "rememberCameraPositionState()", "Holds and controls the map's current camera position, bearing, tilt, and zoom."),
            ComponentProperty("properties", "MapProperties", "MapProperties()", "Map-level configuration such as map type, my-location layer, and min/max zoom."),
            ComponentProperty("uiSettings", "MapUiSettings", "MapUiSettings()", "Controls built-in UI affordances like zoom controls, compass, and gestures."),
            ComponentProperty("onMapClick", "((LatLng) -> Unit)?", "null", "Callback invoked when the user taps a point on the map.")
        ),
        events = listOf("onMapClick — fired with the tapped LatLng.", "cameraPositionState changes — fired as the user pans/zooms the map."),
        bestPractices = listOf(
            "Request ACCESS_FINE_LOCATION before setting MapProperties(isMyLocationEnabled = true), or the map throws a SecurityException.",
            "Hoist CameraPositionState to a ViewModel-backed value when the camera needs to be driven by app state (e.g. search results)."
        ),
        commonMistakes = listOf(
            "Placing a GoogleMap composable inside a scrolling LazyColumn item without disabling scroll gestures, causing gesture conflicts.",
            "Forgetting to add the Maps API key to AndroidManifest.xml's com.google.android.geo.API_KEY meta-data, causing a blank gray map."
        ),
        accessibilityNotes = listOf(
            "Provide an alternative, accessible list-based view of markers for screen reader users, since map surfaces are not fully screen-reader navigable.",
            "Ensure custom Marker info windows have adequate text contrast and touch target size."
        ),
        performanceNotes = listOf(
            "Avoid recreating MapProperties/MapUiSettings instances on every recomposition; remember them to prevent unnecessary map reconfiguration.",
            "Limit the number of simultaneous Marker composables (hundreds+) — use clustering (maps-compose-utils) for large datasets."
        ),
        relatedComponentIds = listOf("map-marker-camera"),
        minApi = 21
    )

    private val markerCamera = ComponentSpec(
        id = "map-marker-camera",
        category = ComponentCategory.MAPS,
        title = "Marker & Camera Position State",
        overview = "Covers placing interactive markers on a map and driving/animating the " +
            "camera programmatically via CameraPositionState, the core building blocks for " +
            "any map-based feature.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val markerState = rememberMarkerState(position = LatLng(37.4220, -122.0841))
                val cameraPositionState = rememberCameraPositionState()
                val scope = rememberCoroutineScope()

                GoogleMap(cameraPositionState = cameraPositionState) {
                    Marker(
                        state = markerState,
                        title = "Selected Location",
                        onClick = {
                            scope.launch {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngZoom(markerState.position, 16f)
                                )
                            }
                            true
                        }
                    )
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            class LocationSearchViewModel : ViewModel() {
                private val _selectedLatLng = MutableStateFlow<LatLng?>(null)
                val selectedLatLng: StateFlow<LatLng?> = _selectedLatLng.asStateFlow()

                fun onResultSelected(latLng: LatLng) {
                    _selectedLatLng.value = latLng
                }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("state", "MarkerState", "rememberMarkerState()", "Holds the marker's draggable position and drag state."),
            ComponentProperty("draggable", "Boolean", "false", "Whether the user can drag the marker to reposition it."),
            ComponentProperty("onClick", "((Marker) -> Boolean)?", "null", "Callback fired on marker tap; return true to consume the click and suppress the default info window."),
            ComponentProperty("animate", "suspend (CameraUpdate) -> Unit", "n/a", "Suspend function on CameraPositionState that smoothly animates the camera to a new position/zoom.")
        ),
        events = listOf("onClick — fired when a marker is tapped.", "markerState.dragState changes — fired while a draggable marker is being moved."),
        bestPractices = listOf(
            "Use cameraPositionState.animate() inside a coroutine scope for smooth transitions instead of instantly snapping the camera position.",
            "Keep MarkerState instances remembered and hoisted so drag/position updates survive recomposition."
        ),
        commonMistakes = listOf(
            "Mutating markerState.position directly from a background thread instead of the main thread, which can cause inconsistent map redraws.",
            "Calling animate() without a CoroutineScope tied to the composable's lifecycle, leaking the animation across navigation."
        ),
        accessibilityNotes = listOf(
            "Draggable markers need an accessible alternative (e.g. a text field for coordinates) for users who cannot perform precise drag gestures.",
            "Info windows opened on marker click should contain real, readable text, not just an icon."
        ),
        performanceNotes = listOf(
            "Camera animate() calls are relatively expensive; debounce rapid successive calls (e.g. from a fast-typing search box).",
            "Avoid recreating rememberMarkerState with a new position value directly, which resets drag state — update markerState.position instead."
        ),
        relatedComponentIds = listOf("map-google"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(googleMapCompose, markerCamera)
}

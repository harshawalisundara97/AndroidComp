package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object StorageComponentCatalog {

    private val preferencesDataStore = ComponentSpec(
        id = "storage-datastore",
        category = ComponentCategory.STORAGE,
        title = "Preferences DataStore",
        overview = "A modern, coroutine/Flow-based replacement for SharedPreferences that " +
            "persists simple key-value data asynchronously and transactionally, avoiding the " +
            "main-thread blocking and lack of consistency guarantees of the old API.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val Context.dataStore by preferencesDataStore(name = "settings")

                val THEME_KEY = stringPreferencesKey("theme")

                val themeFlow: Flow<String> = context.dataStore.data
                    .map { prefs -> prefs[THEME_KEY] ?: "system" }

                suspend fun setTheme(context: Context, theme: String) {
                    context.dataStore.edit { prefs ->
                        prefs[THEME_KEY] = theme
                    }
                }

                @Composable
                fun ThemeSetting(dataStore: DataStore<Preferences>) {
                    val theme by dataStore.data
                        .map { it[THEME_KEY] ?: "system" }
                        .collectAsState(initial = "system")
                    Text("Current theme: ${'$'}theme")
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            class SettingsViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
                val theme: StateFlow<String> = dataStore.data
                    .map { it[THEME_KEY] ?: "system" }
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

                fun setTheme(value: String) = viewModelScope.launch {
                    dataStore.edit { it[THEME_KEY] = value }
                }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("name", "String", "required", "The file name used to back the DataStore, delegated via preferencesDataStore(name)."),
            ComponentProperty("data", "Flow<Preferences>", "n/a", "A cold Flow that emits the current preferences on every change."),
            ComponentProperty("edit", "suspend (MutablePreferences) -> Unit", "required", "Transactional block for atomically updating stored values."),
            ComponentProperty("corruptionHandler", "ReplaceFileCorruptionHandler?", "null", "Optional handler to recover from a corrupted preferences file.")
        ),
        events = listOf("data collection — Flow emits a new Preferences snapshot whenever any key changes."),
        bestPractices = listOf(
            "Create exactly one DataStore instance per file name at the application/Context level (via the property delegate) to avoid multiple-instances-same-file crashes.",
            "Use edit {} for all writes so multiple concurrent updates apply atomically instead of racing."
        ),
        commonMistakes = listOf(
            "Instantiating preferencesDataStore multiple times for the same file, which throws IllegalStateException at runtime.",
            "Reading dataStore.data.first() on the main thread without a coroutine scope, causing a blocking call or crash."
        ),
        accessibilityNotes = listOf(
            "DataStore is a data-layer API with no direct UI; ensure any Compose UI observing it (e.g. via collectAsState) still meets standard accessibility guidelines.",
            "When a stored preference changes app language or text scale, ensure the resulting recomposition announces the change to screen readers where relevant."
        ),
        performanceNotes = listOf(
            "All reads/writes happen off the main thread via Dispatchers.IO internally; never wrap calls in your own blocking runBlocking on the main thread.",
            "Prefer collecting only the specific key needed via map{} rather than the whole Preferences object, to reduce unnecessary recompositions."
        ),
        relatedComponentIds = listOf("storage-room"),
        minApi = 21
    )

    private val room = ComponentSpec(
        id = "storage-room",
        category = ComponentCategory.STORAGE,
        title = "Room Database",
        overview = "A SQLite object-mapping persistence library providing compile-time verified " +
            "queries, entities, and DAOs, with first-class Kotlin Coroutines/Flow support for " +
            "structured local data storage.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Entity
                data class Note(
                    @PrimaryKey(autoGenerate = true) val id: Long = 0,
                    val title: String,
                    val body: String
                )

                @Dao
                interface NoteDao {
                    @Query("SELECT * FROM Note ORDER BY id DESC")
                    fun observeNotes(): Flow<List<Note>>

                    @Insert
                    suspend fun insert(note: Note)
                }

                @Database(entities = [Note::class], version = 1)
                abstract class AppDatabase : RoomDatabase() {
                    abstract fun noteDao(): NoteDao
                }

                @Composable
                fun NoteList(dao: NoteDao) {
                    val notes by dao.observeNotes().collectAsState(initial = emptyList())
                    LazyColumn {
                        items(notes) { note -> Text(note.title) }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            class NotesViewModel(private val dao: NoteDao) : ViewModel() {
                val notes: StateFlow<List<Note>> = dao.observeNotes()
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

                fun addNote(title: String, body: String) = viewModelScope.launch {
                    dao.insert(Note(title = title, body = body))
                }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("entities", "Array<KClass<*>>", "required", "The list of @Entity classes managed by the database, declared on @Database."),
            ComponentProperty("version", "Int", "required", "The schema version; must be incremented alongside a Migration when the schema changes."),
            ComponentProperty("exportSchema", "Boolean", "true", "Whether Room exports the schema to a JSON file for migration testing."),
            ComponentProperty("fallbackToDestructiveMigration", "Boolean", "false", "If true, drops and recreates tables instead of requiring an explicit Migration.")
        ),
        events = listOf("observeNotes() Flow emission — fires automatically whenever the underlying table's data changes."),
        bestPractices = listOf(
            "Always provide a Migration when incrementing the database version in production; avoid fallbackToDestructiveMigration outside of debug builds.",
            "Build the RoomDatabase as a singleton (e.g. via Hilt) to avoid opening multiple connections to the same file."
        ),
        commonMistakes = listOf(
            "Running DAO queries directly on the main thread; Room throws an IllegalStateException for synchronous main-thread queries by default.",
            "Forgetting to bump the database version after adding/removing a column, causing a crash on existing installs."
        ),
        accessibilityNotes = listOf(
            "Room itself has no UI surface; ensure lists rendered from Flow<List<T>> (e.g. LazyColumn) provide proper content descriptions per item.",
            "When showing empty-database states, present a proper Empty State component rather than a blank screen."
        ),
        performanceNotes = listOf(
            "Use Flow-returning DAO methods for observed lists so Room only re-queries and emits when the relevant table actually changes.",
            "Add indices (@Index) on columns used in WHERE/ORDER BY clauses for large tables to avoid full table scans."
        ),
        relatedComponentIds = listOf("storage-datastore"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(preferencesDataStore, room)
}

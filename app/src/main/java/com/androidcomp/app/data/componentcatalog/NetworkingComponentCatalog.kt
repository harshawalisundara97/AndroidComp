package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object NetworkingComponentCatalog {

    private val retrofit = ComponentSpec(
        id = "networking-retrofit",
        category = ComponentCategory.NETWORKING,
        title = "Retrofit HTTP Client",
        overview = "A type-safe HTTP client for Android/Java/Kotlin that turns a REST API into " +
            "a Kotlin interface, handling request building, serialization, and coroutine " +
            "suspend-function support on top of OkHttp.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                interface ApiService {
                    @GET("users/{id}")
                    suspend fun getUser(@Path("id") id: String): User
                }

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.example.com/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(okHttpClient)
                    .build()

                val api = retrofit.create(ApiService::class.java)

                @Composable
                fun UserProfile(userId: String, api: ApiService) {
                    var user by remember { mutableStateOf<User?>(null) }
                    LaunchedEffect(userId) {
                        user = api.getUser(userId)
                    }
                    user?.let { Text(it.name) }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            class UserViewModel(private val api: ApiService) : ViewModel() {
                private val _user = MutableStateFlow<User?>(null)
                val user: StateFlow<User?> = _user.asStateFlow()

                fun loadUser(id: String) = viewModelScope.launch {
                    runCatching { api.getUser(id) }
                        .onSuccess { _user.value = it }
                        .onFailure { /* expose error state */ }
                }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("baseUrl", "String", "required", "The base URL all @GET/@POST paths are resolved against; must end with a trailing slash."),
            ComponentProperty("client", "OkHttpClient", "OkHttpClient()", "The underlying HTTP client handling connections, interceptors, and timeouts."),
            ComponentProperty("addConverterFactory", "Converter.Factory", "required", "Serialization strategy, e.g. MoshiConverterFactory or GsonConverterFactory."),
            ComponentProperty("callAdapterFactory", "CallAdapter.Factory?", "null", "Optional adapter for non-suspend return types (e.g. RxJava); not needed when using suspend functions.")
        ),
        events = listOf("suspend fun calls — each annotated interface method performs a network request and suspends until the response arrives."),
        bestPractices = listOf(
            "Declare API interfaces with suspend functions rather than Call<T> to integrate cleanly with coroutines and ViewModel scopes.",
            "Use a single shared OkHttpClient/Retrofit instance (injected as a singleton) instead of constructing one per request."
        ),
        commonMistakes = listOf(
            "Calling a suspend Retrofit function directly on the main thread outside of a coroutine scope, causing a compile error or ANR if misused.",
            "Forgetting the INTERNET permission in the manifest, causing all requests to fail silently with a SecurityException/UnknownHostException."
        ),
        accessibilityNotes = listOf(
            "Networking has no direct UI; ensure loading and error states surfaced to the UI use proper Loading Skeleton / Error State components with content descriptions.",
            "Announce network failures via accessible error text, not color alone (e.g. a red icon with no label)."
        ),
        performanceNotes = listOf(
            "Reuse a single OkHttpClient across the app — it manages a connection pool and thread pool that are expensive to recreate.",
            "Enable HTTP response caching via OkHttp's Cache to reduce redundant network calls for repeat requests."
        ),
        relatedComponentIds = listOf("networking-okhttp-interceptor"),
        minApi = 21
    )

    private val okHttpInterceptor = ComponentSpec(
        id = "networking-okhttp-interceptor",
        category = ComponentCategory.NETWORKING,
        title = "OkHttp Interceptor",
        overview = "A mechanism to observe, modify, or short-circuit HTTP requests and responses " +
            "flowing through OkHttp — commonly used for auth headers, logging, and retry logic, " +
            "and used internally by Retrofit's underlying client.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val token = tokenProvider()
                        val request = chain.request().newBuilder()
                            .apply { token?.let { addHeader("Authorization", "Bearer ${'$'}it") } }
                            .build()
                        return chain.proceed(request)
                    }
                }

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor { currentToken })
                    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                    .build()
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("chain", "Interceptor.Chain", "required", "Provides access to the current request and the ability to proceed with a (possibly modified) one."),
            ComponentProperty("addInterceptor", "(Interceptor) -> Builder", "n/a", "Registers an application interceptor, run once per logical call."),
            ComponentProperty("addNetworkInterceptor", "(Interceptor) -> Builder", "n/a", "Registers a network interceptor, run once per network request (including redirects/retries)."),
            ComponentProperty("level", "HttpLoggingInterceptor.Level", "Level.NONE", "Controls verbosity of HttpLoggingInterceptor output (NONE/BASIC/HEADERS/BODY).")
        ),
        events = listOf("intercept(chain) — invoked for every outgoing request passing through the client."),
        bestPractices = listOf(
            "Strip HttpLoggingInterceptor.Level.BODY logging from release builds to avoid leaking sensitive request/response payloads.",
            "Keep interceptors small and single-purpose (auth, logging, retry) rather than one interceptor doing everything."
        ),
        commonMistakes = listOf(
            "Adding an auth interceptor as a network interceptor when an application interceptor was intended, causing it to run multiple times on retries/redirects.",
            "Logging full request/response bodies containing passwords or tokens in production builds."
        ),
        accessibilityNotes = listOf(
            "Not UI-facing; ensure any auth-failure interceptor logic surfaces a clear, accessible re-login prompt rather than a silent failure."
        ),
        performanceNotes = listOf(
            "Each added interceptor adds a small per-request overhead; avoid excessive interceptor chains for hot-path endpoints.",
            "HttpLoggingInterceptor at BODY level meaningfully slows down large payload requests and should be gated behind a debug flag."
        ),
        relatedComponentIds = listOf("networking-retrofit"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(retrofit, okHttpInterceptor)
}

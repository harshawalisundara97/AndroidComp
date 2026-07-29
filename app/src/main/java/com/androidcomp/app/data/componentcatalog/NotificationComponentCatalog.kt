package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object NotificationComponentCatalog {

    private val basicNotification = ComponentSpec(
        id = "notification-basic",
        category = ComponentCategory.NOTIFICATIONS,
        title = "NotificationCompat.Builder",
        overview = "Builds a backward-compatible notification (title, text, icon, actions) that " +
            "is then posted to the system tray via `NotificationManagerCompat`. Requires a " +
            "notification channel on API 26+ and the `POST_NOTIFICATIONS` runtime permission " +
            "on API 33+.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                fun showDownloadCompleteNotification(context: Context) {
                    val notification = NotificationCompat.Builder(context, CHANNEL_ID_DOWNLOADS)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Download complete")
                        .setContentText("your_file.pdf has finished downloading.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build()

                    if (ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        NotificationManagerCompat.from(context)
                            .notify(NOTIFICATION_ID_DOWNLOAD, notification)
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun onDownloadFinished(fileName: String) {
                notifier.showDownloadComplete(fileName)
            }
            // ViewModel delegates to a thin Notifier class wrapping NotificationManagerCompat,
            // keeping Context access out of the ViewModel itself.
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("setContentTitle", "CharSequence", "none", "The notification's bold title line."),
            ComponentProperty("setContentText", "CharSequence", "none", "The notification's body text."),
            ComponentProperty("setSmallIcon", "Int (drawable res)", "required", "Status bar icon; must be a flat, single-color icon per platform guidelines."),
            ComponentProperty("setPriority", "Int", "PRIORITY_DEFAULT", "Pre-Oreo importance hint; on Oreo+ the channel's importance takes precedence."),
            ComponentProperty("setAutoCancel", "Boolean", "false", "Whether tapping the notification dismisses it automatically.")
        ),
        events = listOf(
            "PendingIntent (via setContentIntent) — fired when the user taps the notification.",
            "NotificationManagerCompat.notify — posts/updates the notification for a given ID; calling again with the same ID updates it in place."
        ),
        bestPractices = listOf(
            "Check for `POST_NOTIFICATIONS` (API 33+) before calling notify(); a denied permission silently drops the notification rather than crashing.",
            "Reuse a stable notification ID per logical notification so repeated calls to notify() update it instead of stacking duplicates."
        ),
        commonMistakes = listOf(
            "Building a notification without first creating its NotificationChannel on API 26+, which silently prevents the notification from showing.",
            "Omitting setAutoCancel(true) on transient notifications, leaving stale notifications in the tray after the user has already acted on them."
        ),
        accessibilityNotes = listOf(
            "TalkBack announces contentTitle and contentText automatically — keep both concise and meaningful rather than generic (\"Update\").",
            "Avoid conveying critical information through icon color alone; pair with descriptive text since some icon colors aren't distinguishable to color-blind users."
        ),
        performanceNotes = listOf(
            "Building and posting a notification is cheap, but attaching large Bitmaps (e.g. setLargeIcon) repeatedly can add GC pressure — reuse decoded bitmaps where possible.",
            "Batch related updates into a single notify() call rather than rapid repeated notify() calls, which the system may throttle or coalesce anyway."
        ),
        relatedComponentIds = listOf("notification-channel"),
        minApi = 21
    )

    private val notificationChannel = ComponentSpec(
        id = "notification-channel",
        category = ComponentCategory.NOTIFICATIONS,
        title = "NotificationChannel",
        overview = "Groups notifications by type on API 26+ (Oreo and above), letting the user " +
            "control importance, sound, and visibility per channel from system Settings. Required " +
            "before posting any notification on API 26+; ignored on lower API levels.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                fun createDownloadsChannel(context: Context) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                            CHANNEL_ID_DOWNLOADS,
                            "Downloads",
                            NotificationManager.IMPORTANCE_DEFAULT
                        ).apply {
                            description = "Notifies when a download finishes"
                        }
                        val manager = context.getSystemService(NotificationManager::class.java)
                        manager.createNotificationChannel(channel)
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("id", "String", "required", "Stable, unique channel identifier referenced when building notifications."),
            ComponentProperty("name", "CharSequence", "required", "User-visible channel name shown in system Settings."),
            ComponentProperty("importance", "Int", "IMPORTANCE_DEFAULT", "Controls default sound/heads-up behavior (e.g. IMPORTANCE_HIGH, IMPORTANCE_LOW)."),
            ComponentProperty("description", "String?", "null", "Optional user-visible description shown under the channel name in Settings.")
        ),
        events = listOf("createNotificationChannel — registers the channel once; calling again with the same id is a safe no-op update."),
        bestPractices = listOf(
            "Create all channels once at app startup (e.g. in Application.onCreate), not lazily right before the first notification.",
            "Use a distinct channel per notification category (downloads, messages, alerts) so users can mute one without muting all."
        ),
        commonMistakes = listOf(
            "Changing a channel's importance/sound after creation — most channel settings are immutable once created and require a new channel id to change.",
            "Creating a NotificationChannel unconditionally without the Build.VERSION_CODES.O check, which is fine at runtime but signals a misunderstanding of the API's applicability."
        ),
        accessibilityNotes = listOf(
            "Give channels clear, descriptive names since users rely on them to decide which categories to mute or prioritize.",
            "Respect the user's channel-level Do Not Disturb/importance overrides — the app cannot force a higher importance than the user allows."
        ),
        performanceNotes = listOf(
            "createNotificationChannel is idempotent and cheap to call repeatedly; still, prefer calling it once at startup rather than before every notify()."
        ),
        relatedComponentIds = listOf("notification-basic"),
        minApi = 26
    )

    val all: List<ComponentSpec> = listOf(basicNotification, notificationChannel)
}

package com.androidcomp.app.features.news

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NewsItem(
    val id: String,
    val title: String,
    val snippet: String,
    val body: String,
    val icon: ImageVector,
    val tint: Color,
    val isNew: Boolean = false
)

// Static snapshot of headlines from developer.android.com (as of this app's
// last content refresh) — not a live feed, no network fetch.
object NewsCatalog {
    val all = listOf(
        NewsItem(
            id = "android-17",
            title = "Android 17 is here",
            snippet = "Officially released with AOSP source — top 5 updates to prep your app for API 37",
            body = "Android 17 is officially released, with source code available on AOSP. " +
                "This release brings the top updates developers need to know to prepare their " +
                "apps for the new platform version and target API level 37, including refreshed " +
                "platform APIs, updated behavior changes, and compatibility guidance for existing " +
                "apps moving to the new target SDK.",
            icon = Icons.Outlined.NewReleases,
            tint = Color(0xFF2F6FED),
            isNew = true
        ),
        NewsItem(
            id = "android-xr",
            title = "Dive into Android XR",
            snippet = "Expanded engine support and new resources for building and scaling XR experiences",
            body = "The Android XR platform continues to expand, with broader game-engine support " +
                "and a growing set of resources for building, testing, and scaling XR experiences " +
                "across headsets and glasses. New guidance covers everything from initial setup to " +
                "performance tuning for immersive apps.",
            icon = Icons.Outlined.ViewInAr,
            tint = Color(0xFF2E9E5B),
            isNew = true
        ),
        NewsItem(
            id = "ai-assistance",
            title = "Elevating AI assistance",
            snippet = "Android Bench adds updated LLM benchmarking methodology and new benchmarked models",
            body = "Android Bench now features an updated benchmarking methodology, a wider set of " +
                "benchmarked models, and new opportunities for community contributions — helping " +
                "developers see which LLMs are actually most helpful for real Android development " +
                "tasks.",
            icon = Icons.Outlined.AutoAwesome,
            tint = Color(0xFFE58A2E)
        )
    )
}

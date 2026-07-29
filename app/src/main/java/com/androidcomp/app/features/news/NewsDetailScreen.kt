package com.androidcomp.app.features.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.AppBadge
import com.androidcomp.app.core.ui.AppTopBar

@Composable
fun NewsDetailScreen(newsId: String, onBackClick: () -> Unit) {
    val news = NewsCatalog.all.firstOrNull { it.id == newsId }

    Scaffold(topBar = { AppTopBar(news?.title ?: "News", onBackClick = onBackClick) }) { padding ->
        if (news == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Article not found")
            }
            return@Scaffold
        }

        LazyColumn(Modifier.padding(padding)) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(news.tint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        news.icon,
                        contentDescription = null,
                        tint = news.tint,
                        modifier = Modifier.size(64.dp)
                    )
                }
                Column(Modifier.padding(20.dp)) {
                    if (news.isNew) {
                        AppBadge("NEW", containerColor = news.tint)
                    }
                    Text(
                        news.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                    Text(
                        news.body,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Source: developer.android.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }
    }
}

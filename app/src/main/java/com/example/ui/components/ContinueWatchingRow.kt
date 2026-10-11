package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.data.model.PlaybackProgress

@Composable
fun ContinueWatchingRow(
    continueWatchingList: List<Pair<ContentItem, PlaybackProgress>>,
    onItemClick: (ContentItem, PlaybackProgress) -> Unit,
    modifier: Modifier = Modifier
) {
    if (continueWatchingList.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Continue Watching",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(continueWatchingList) { (item, progress) ->
                PosterCard(
                    item = item,
                    onClick = { onItemClick(item, progress) },
                    progressFraction = progress.progressFraction,
                    modifier = Modifier
                        .width(160.dp)
                        .height(235.dp)
                )
            }
        }
    }
}

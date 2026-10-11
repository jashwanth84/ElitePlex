package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StreamingPlatform

@Composable
fun PlatformRow(
    onPlatformClick: (StreamingPlatform) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Streaming Platforms",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(StreamingPlatform.values()) { platform ->
                PlatformBadgeItem(
                    platform = platform,
                    onClick = { onPlatformClick(platform) }
                )
            }
        }
    }
}

@Composable
private fun PlatformBadgeItem(
    platform: StreamingPlatform,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("platform_item_${platform.name.lowercase()}")
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    when (platform) {
                        StreamingPlatform.NETFLIX -> Brush.verticalGradient(listOf(Color(0xFF0F0F12), Color(0xFF000000)))
                        StreamingPlatform.PRIME_VIDEO -> Brush.verticalGradient(listOf(Color(0xFF00A8E1), Color(0xFF007399)))
                        StreamingPlatform.JIO_HOTSTAR -> Brush.linearGradient(listOf(Color(0xFF0C2050), Color(0xFF14539A)))
                        StreamingPlatform.LIONSGATE_PLAY -> Brush.verticalGradient(listOf(Color(0xFF222228), Color(0xFF141418)))
                        StreamingPlatform.SONY_LIV -> Brush.verticalGradient(listOf(Color(0xFF1F2430), Color(0xFF131720)))
                        StreamingPlatform.ZEE5 -> Brush.verticalGradient(listOf(Color(0xFF5B1B85), Color(0xFF381052)))
                    }
                )
                .border(
                    1.5.dp,
                    when (platform) {
                        StreamingPlatform.NETFLIX -> Color(0xFFE50914)
                        StreamingPlatform.PRIME_VIDEO -> Color(0xFF33C2FF)
                        StreamingPlatform.JIO_HOTSTAR -> Color(0xFF4C8DFF)
                        StreamingPlatform.LIONSGATE_PLAY -> Color(0xFFD4AF37)
                        else -> Color(0xFF444455)
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            when (platform) {
                StreamingPlatform.NETFLIX -> {
                    Text(
                        text = "N",
                        color = Color(0xFFE50914),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                StreamingPlatform.PRIME_VIDEO -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "prime",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "video",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                StreamingPlatform.JIO_HOTSTAR -> {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Hotstar",
                        tint = Color.Cyan,
                        modifier = Modifier.size(30.dp)
                    )
                }
                StreamingPlatform.LIONSGATE_PLAY -> {
                    Text(
                        text = "LIONSGATE\nPLAY",
                        color = Color(0xFFD4AF37),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 10.sp
                    )
                }
                StreamingPlatform.SONY_LIV -> {
                    Text(
                        text = "SONY\nLIV",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 12.sp
                    )
                }
                StreamingPlatform.ZEE5 -> {
                    Text(
                        text = "ZEE5",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = platform.displayName,
            color = Color(0xFFA0A0AA),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

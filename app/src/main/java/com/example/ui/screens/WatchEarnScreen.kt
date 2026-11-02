package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.User
import com.example.data.model.VideoTask
import com.example.ui.theme.PakBlackBg
import com.example.ui.theme.PakDarkBorder
import com.example.ui.theme.PakDarkSurface
import com.example.ui.theme.PakDarkSurfaceVariant
import com.example.ui.theme.PakEmeraldDark
import com.example.ui.theme.PakEmeraldGreen
import com.example.ui.theme.PakEmeraldLight
import com.example.ui.theme.PakGold
import com.example.ui.theme.PakGoldDark
import com.example.ui.theme.PakSuccess
import com.example.ui.theme.PakTextMuted
import com.example.ui.theme.PakTextPrimary
import com.example.ui.theme.PakTextSecondary
import com.example.ui.theme.PakWhite
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.VideoWatchState

@Composable
fun WatchEarnScreen(
    user: User?,
    videos: List<VideoTask>,
    watchState: VideoWatchState,
    onStartWatch: (VideoTask) -> Unit,
    onClosePlayer: () -> Unit,
    onNavigate: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlanActive = user?.isPlanActive == true
    val watchedCount = user?.videosWatchedToday ?: 0
    val dailyLimit = 10
    val isLimitReached = watchedCount >= dailyLimit

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PakBlackBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Watch & Earn",
                            color = PakWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "15-Second YouTube Video Tasks",
                            color = PakGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isPlanActive) PakEmeraldGreen.copy(alpha = 0.2f) else PakGold.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isPlanActive) "Plan A Active" else "Plan Locked",
                            color = if (isPlanActive) PakSuccess else PakGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Daily Progress Tracker Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakGold.copy(alpha = 0.3f))))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TODAY'S VIDEO QUOTA",
                                color = PakTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$watchedCount / $dailyLimit Videos",
                                color = PakGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val progress = (watchedCount / dailyLimit.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PakGold,
                            trackColor = PakDarkSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Earned today: Rs. ${watchedCount * 15}",
                                color = PakEmeraldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Remaining: ${dailyLimit - watchedCount}",
                                color = PakTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Locked Warning Card if plan not activated
            if (!isPlanActive) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF281C03)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakGoldDark, PakGold)))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PakGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = PakGold,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Videos Locked Until Payment is Confirmed",
                                color = PakWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Activate Plan A (Rs. 100) via JazzCash deposit to unlock 10 daily videos of 15 seconds.",
                                color = PakTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { onNavigate(ScreenTab.PLANS) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PakGold,
                                    contentColor = PakBlackBg
                                )
                            ) {
                                Text("Activate Plan A (Rs. 100)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Video Task Cards List
            item {
                Text(
                    text = "AVAILABLE VIDEO TASKS",
                    color = PakTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            items(videos) { video ->
                VideoTaskCard(
                    video = video,
                    isLocked = !isPlanActive || isLimitReached,
                    onWatchClick = { onStartWatch(video) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Active Watching Modal Dialog
        if (watchState.currentVideo != null) {
            VideoPlayerDialog(
                watchState = watchState,
                onClose = onClosePlayer
            )
        }
    }
}

@Composable
fun VideoTaskCard(
    video: VideoTask,
    isLocked: Boolean,
    onWatchClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("video_task_${video.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakDarkBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Simulated video preview box
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 65.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF1E2F24), Color(0xFF121B15))
                        )
                    )
                    .border(0.5.dp, PakDarkBorder, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = if (isLocked) PakTextMuted else PakGold,
                    modifier = Modifier.size(28.dp)
                )
                // Duration tag
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "15s",
                        color = PakWhite,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    color = PakWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = video.channelName,
                        color = PakTextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = PakEmeraldGreen,
                        modifier = Modifier.size(11.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+Rs. ${video.rewardAmount.toInt()}",
                        color = PakGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• 15 sec",
                        color = PakTextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onWatchClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLocked) PakDarkSurfaceVariant else PakEmeraldGreen,
                    contentColor = if (isLocked) PakTextMuted else PakBlackBg
                ),
                modifier = Modifier.testTag("watch_video_btn_${video.id}")
            ) {
                if (isLocked) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked", modifier = Modifier.size(14.dp))
                } else {
                    Text("Watch", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VideoPlayerDialog(
    watchState: VideoWatchState,
    onClose: () -> Unit
) {
    val video = watchState.currentVideo ?: return
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Dialog(onDismissRequest = { /* Require watch to complete or explicit close */ }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = PakDarkSurface,
            tonalElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "YouTube Video Task",
                        color = PakGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = PakTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Simulated YouTube Video Screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF000000))
                        .border(1.dp, PakDarkBorder, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (watchState.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Finished",
                                tint = PakSuccess,
                                modifier = Modifier
                                    .size(54.dp)
                                    .scale(pulseScale)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Verification Complete!",
                                color = PakSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PakEmeraldGreen.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Playing",
                                    tint = PakEmeraldGreen,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Playing 15s Video...",
                                color = PakTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Progress overlay on bottom
                    if (!watchState.isCompleted) {
                        val progress = (15 - watchState.secondsRemaining) / 15f
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .align(Alignment.BottomCenter),
                            color = PakEmeraldGreen,
                            trackColor = Color.Transparent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = video.title,
                    color = PakWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 2
                )
                Text(
                    text = "Channel: ${video.channelName}",
                    color = PakTextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Timer & Reward Display
                if (!watchState.isCompleted) {
                    Box(
                        modifier = Modifier
                            .size(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { (15 - watchState.secondsRemaining) / 15f },
                            modifier = Modifier.size(70.dp),
                            color = PakGold,
                            strokeWidth = 5.dp,
                            trackColor = PakDarkSurfaceVariant
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${watchState.secondsRemaining}",
                                color = PakGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "sec",
                                color = PakTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Please watch until the timer reaches 0 to earn Rs. 15",
                        color = PakTextSecondary,
                        fontSize = 11.sp
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PakEmeraldGreen.copy(alpha = 0.15f)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakEmeraldGreen, PakEmeraldGreen)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = PakSuccess, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+Rs. 15.00 Credited to Account Balance!",
                                color = PakSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onClose,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PakGold,
                            contentColor = PakBlackBg
                        )
                    ) {
                        Text("Claim & Next Video", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

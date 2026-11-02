package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PakBlackBg
import com.example.ui.theme.PakDarkBorder
import com.example.ui.theme.PakDarkSurface
import com.example.ui.theme.PakDarkSurfaceVariant
import com.example.ui.theme.PakEmeraldDark
import com.example.ui.theme.PakEmeraldGreen
import com.example.ui.theme.PakEmeraldLight
import com.example.ui.theme.PakError
import com.example.ui.theme.PakGold
import com.example.ui.theme.PakGoldDark
import com.example.ui.theme.PakSuccess
import com.example.ui.theme.PakTextMuted
import com.example.ui.theme.PakTextPrimary
import com.example.ui.theme.PakTextSecondary
import com.example.ui.theme.PakWarning
import com.example.ui.theme.PakWhite
import com.example.ui.viewmodel.ScreenTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PakTopBar(
    userId: String?,
    unreadNotificationsCount: Int,
    isAdminMode: Boolean,
    onNotificationClick: () -> Unit,
    onAdminToggleClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PakBlackBg,
            titleContentColor = PakTextPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(PakEmeraldGreen, PakEmeraldDark))
                        )
                        .border(1.dp, PakGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PK",
                        color = PakGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "PAK INVESTMENT",
                            color = PakWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = if (userId != null) "ID: $userId" else "Official Portal",
                        color = PakGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        actions = {
            // Notification bell
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.testTag("notification_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationsCount > 0) {
                            Badge(
                                containerColor = PakGold,
                                contentColor = PakBlackBg
                            ) {
                                Text("$unreadNotificationsCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "SMS Notifications",
                        tint = if (unreadNotificationsCount > 0) PakGold else PakTextSecondary
                    )
                }
            }

            // Admin switch button
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isAdminMode) PakGoldDark else PakDarkSurfaceVariant)
                    .border(
                        1.dp,
                        if (isAdminMode) PakGold else PakDarkBorder,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onAdminToggleClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("admin_toggle_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Mode",
                        tint = if (isAdminMode) PakBlackBg else PakGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAdminMode) "ADMIN" else "Admin",
                        color = if (isAdminMode) PakBlackBg else PakGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    )
}

@Composable
fun PakBottomNav(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit
) {
    NavigationBar(
        containerColor = PakDarkSurface,
        contentColor = PakTextPrimary,
        tonalElevation = 8.dp,
        modifier = Modifier.border(width = 0.5.dp, color = PakDarkBorder)
    ) {
        val items = listOf(
            Triple(ScreenTab.HOME, "Home", Icons.Default.Home),
            Triple(ScreenTab.PLANS, "Plans", Icons.Default.Subscriptions),
            Triple(ScreenTab.WATCH_EARN, "Watch", Icons.Default.PlayCircle),
            Triple(ScreenTab.WALLET, "Wallet", Icons.Default.AccountBalanceWallet),
            Triple(ScreenTab.REFERRALS, "Referral", Icons.Default.CardGiftcard)
        )

        items.forEach { (tab, label, icon) ->
            val selected = currentTab == tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PakBlackBg,
                    unselectedIconColor = PakTextSecondary,
                    selectedTextColor = PakGold,
                    unselectedTextColor = PakTextMuted,
                    indicatorColor = PakGold
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bg, fg, icon) = when (status.uppercase()) {
        "APPROVED" -> Triple(PakEmeraldGreen.copy(alpha = 0.18f), PakSuccess, Icons.Default.CheckCircle)
        "PENDING" -> Triple(PakGold.copy(alpha = 0.18f), PakGold, Icons.Default.HourglassEmpty)
        "REJECTED" -> Triple(PakError.copy(alpha = 0.18f), PakError, Icons.Default.Cancel)
        else -> Triple(PakDarkSurfaceVariant, PakTextSecondary, Icons.Default.Info)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(0.5.dp, fg.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status,
            tint = fg,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status.uppercase(),
            color = fg,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ComplianceNoticeCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161F18)),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakGoldDark.copy(alpha = 0.3f))))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Transparency Notice",
                tint = PakGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "TRANSPARENCY & TERMS DISCLOSURE",
                    color = PakGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "The Rs. 1,500 cycle return must be treated as a proposed figure, not a guaranteed profit. Actual payouts are backed by legitimate operational video advertising revenue and clearly disclosed user terms.",
                    color = PakTextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

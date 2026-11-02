package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Deposit
import com.example.data.model.User
import com.example.data.model.Withdrawal
import com.example.ui.components.ComplianceNoticeCard
import com.example.ui.components.StatusBadge
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

@Composable
fun HomeScreen(
    user: User?,
    deposits: List<Deposit>,
    withdrawals: List<Withdrawal>,
    onNavigate: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PakBlackBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Welcome Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Assalam-o-Alaikum,",
                        color = PakTextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = user?.fullName ?: "Investor",
                        color = PakWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(PakDarkSurfaceVariant)
                        .border(1.dp, PakEmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "2-Step Verified",
                        tint = PakEmeraldGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "2-Step Active",
                        color = PakEmeraldGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Hero Wallet Balance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_balance_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0F3B20),
                                    Color(0xFF072111),
                                    Color(0xFF1B2A1E)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(PakEmeraldGreen, PakGoldDark.copy(alpha = 0.5f))),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AVAILABLE BALANCE",
                                color = PakTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PakGold.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "PKR (₨)",
                                    color = PakGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Rs. ${String.format("%.2f", user?.accountBalance ?: 0.0)}",
                            color = PakWhite,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats mini row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33000000))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total Earned",
                                    color = PakTextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Rs. ${user?.totalEarned?.toInt() ?: 0}",
                                    color = PakEmeraldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(28.dp)
                                    .background(PakDarkBorder)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total Withdrawn",
                                    color = PakTextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Rs. ${user?.totalWithdrawn?.toInt() ?: 0}",
                                    color = PakGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onNavigate(ScreenTab.WALLET) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("deposit_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PakEmeraldGreen,
                                    contentColor = PakBlackBg
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = "Deposit",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Deposit", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Button(
                                onClick = { onNavigate(ScreenTab.WALLET) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("withdraw_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PakGold,
                                    contentColor = PakBlackBg
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = "Withdraw",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Withdraw", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Plan A & Daily Video Watching Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenTab.WATCH_EARN) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakEmeraldGreen.copy(alpha = 0.3f))))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PakEmeraldGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = "Watch & Earn",
                                    tint = PakEmeraldGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Plan A: Watch 15s Videos",
                                    color = PakWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (user?.isPlanActive == true) "Active: Daily 10 Videos" else "Locked: Activation Rs. 100",
                                    color = if (user?.isPlanActive == true) PakSuccess else PakGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        if (user?.isPlanActive == true) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PakEmeraldGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = PakEmeraldGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PakDarkSurfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = PakGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "UNLOCK",
                                        color = PakGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val watched = user?.videosWatchedToday ?: 0
                    val progress = (watched / 10f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PakGold,
                        trackColor = PakDarkSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Daily limit: $watched / 10 videos watched",
                            color = PakTextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Rs. 15 / video",
                            color = PakGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Quick Feature Grid
        item {
            Text(
                text = "SERVICES & ACTIONS",
                color = PakTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    title = "JazzCash\nDeposit",
                    subtitle = "03242158084",
                    icon = Icons.Default.ArrowDownward,
                    color = PakEmeraldGreen,
                    modifier = Modifier.weight(1f)
                ) { onNavigate(ScreenTab.WALLET) }

                QuickActionItem(
                    title = "EasyPaisa\nWithdraw",
                    subtitle = "Max Rs. 1,000",
                    icon = Icons.Default.ArrowUpward,
                    color = PakGold,
                    modifier = Modifier.weight(1f)
                ) { onNavigate(ScreenTab.WALLET) }

                QuickActionItem(
                    title = "Referral\nBonus",
                    subtitle = "Rs. 75 / friend",
                    icon = Icons.Default.CardGiftcard,
                    color = Color(0xFF64B5F6),
                    modifier = Modifier.weight(1f)
                ) { onNavigate(ScreenTab.REFERRALS) }

                QuickActionItem(
                    title = "Plans &\nReturns",
                    subtitle = "Plan A Rs. 100",
                    icon = Icons.Default.Subscriptions,
                    color = Color(0xFFFFB74D),
                    modifier = Modifier.weight(1f)
                ) { onNavigate(ScreenTab.PLANS) }
            }
        }

        // Recent Activity / Transactions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT TRANSACTIONS",
                    color = PakTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "View Wallet",
                    color = PakGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigate(ScreenTab.WALLET) }
                )
            }
        }

        val allItems = (deposits.map { "DEP" to it } + withdrawals.map { "WTH" to it })
            .sortedByDescending {
                if (it.first == "DEP") (it.second as Deposit).createdAt
                else (it.second as Withdrawal).createdAt
            }.take(3)

        if (allItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No transactions yet. Deposit Rs. 100 via JazzCash to start!",
                            color = PakTextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(allItems) { (type, item) ->
                if (type == "DEP") {
                    val dep = item as Deposit
                    TransactionRow(
                        title = "JazzCash Deposit",
                        sub = "TID: ${dep.transactionId}",
                        amount = "+Rs. ${dep.amount.toInt()}",
                        status = dep.status,
                        isCredit = true
                    )
                } else {
                    val wth = item as Withdrawal
                    TransactionRow(
                        title = "${wth.method} Withdrawal",
                        sub = "To: ${wth.accountNumber}",
                        amount = "-Rs. ${wth.amount.toInt()}",
                        status = wth.status,
                        isCredit = false
                    )
                }
            }
        }

        // Regulatory & Compliance Note
        item {
            ComplianceNoticeCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, color.copy(alpha = 0.3f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    color = PakWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = PakTextSecondary,
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun TransactionRow(
    title: String,
    sub: String,
    amount: String,
    status: String,
    isCredit: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakDarkBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isCredit) PakEmeraldGreen.copy(alpha = 0.15f) else PakGold.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = title,
                        tint = if (isCredit) PakEmeraldGreen else PakGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, color = PakWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(text = sub, color = PakTextSecondary, fontSize = 11.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = amount,
                    color = if (isCredit) PakEmeraldLight else PakGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatusBadge(status = status)
            }
        }
    }
}

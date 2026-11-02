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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Deposit
import com.example.data.model.User
import com.example.data.model.VideoTask
import com.example.data.model.Withdrawal
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.PakWhite

@Composable
fun AdminScreen(
    users: List<User>,
    deposits: List<Deposit>,
    withdrawals: List<Withdrawal>,
    videos: List<VideoTask>,
    currentDepositNumber: String,
    onApproveDeposit: (String) -> Unit,
    onRejectDeposit: (String, String) -> Unit,
    onApproveWithdrawal: (String) -> Unit,
    onRejectWithdrawal: (String, String) -> Unit,
    onAddVideo: (title: String, channel: String, reward: Double, category: String) -> Unit,
    onDeleteVideo: (Long) -> Unit,
    onUpdateDepositNumber: (String) -> Unit,
    onSwitchUser: (String) -> Unit,
    onExitAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    // 0: Overview, 1: Pending Deposits, 2: Pending Withdrawals, 3: Users, 4: Video Manager, 5: App Settings

    val pendingDeposits = deposits.filter { it.status == "PENDING" }
    val pendingWithdrawals = withdrawals.filter { it.status == "PENDING" }
    val activeUsersCount = users.count { it.isPlanActive }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PakBlackBg)
    ) {
        // Admin Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PakDarkSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onExitAdmin) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Exit Admin", tint = PakGold)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = PakGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "ADMINISTRATOR DASHBOARD", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Text(text = "Pak Investment Control System", color = PakTextSecondary, fontSize = 11.sp)
                }
            }

            Button(
                onClick = onExitAdmin,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PakDarkSurfaceVariant, contentColor = PakTextPrimary)
            ) {
                Text("Exit Admin", fontSize = 11.sp)
            }
        }

        // Section Tabs
        val tabs = listOf("Overview", "Deposits (${pendingDeposits.size})", "Withdrawals (${pendingWithdrawals.size})", "Users (${users.size})", "Videos", "Settings")
        ScrollableTabRow(
            selectedTabIndex = selectedSection,
            containerColor = PakBlackBg,
            contentColor = PakGold,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                    color = PakGold,
                    height = 3.dp
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSection == index,
                    onClick = { selectedSection = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (selectedSection == index) PakGold else PakTextSecondary
                        )
                    },
                    modifier = Modifier.testTag("admin_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (selectedSection) {
            0 -> AdminOverviewTab(
                totalUsers = users.size,
                activeUsers = activeUsersCount,
                pendingDepositsCount = pendingDeposits.size,
                pendingWithdrawalsCount = pendingWithdrawals.size,
                totalDepositVolume = deposits.filter { it.status == "APPROVED" }.sumOf { it.amount },
                totalWithdrawnVolume = withdrawals.filter { it.status == "APPROVED" }.sumOf { it.amount }
            )
            1 -> AdminDepositsTab(
                deposits = deposits,
                onApprove = onApproveDeposit,
                onReject = onRejectDeposit
            )
            2 -> AdminWithdrawalsTab(
                withdrawals = withdrawals,
                onApprove = onApproveWithdrawal,
                onReject = onRejectWithdrawal
            )
            3 -> AdminUsersTab(
                users = users,
                onSwitchUser = onSwitchUser
            )
            4 -> AdminVideosTab(
                videos = videos,
                onAddVideo = onAddVideo,
                onDeleteVideo = onDeleteVideo
            )
            5 -> AdminSettingsTab(
                currentNumber = currentDepositNumber,
                onUpdate = onUpdateDepositNumber
            )
        }
    }
}

@Composable
fun AdminOverviewTab(
    totalUsers: Int,
    activeUsers: Int,
    pendingDepositsCount: Int,
    pendingWithdrawalsCount: Int,
    totalDepositVolume: Double,
    totalWithdrawnVolume: Double
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "PLATFORM METRICS", color = PakTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(title = "Registered Users", value = "$totalUsers", sub = "$activeUsers Active Plans", color = PakEmeraldLight, modifier = Modifier.weight(1f))
                AdminStatCard(title = "Active Users", value = "$activeUsers", sub = "Verified Members", color = PakGold, modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(title = "Pending Deposits", value = "$pendingDepositsCount", sub = "Needs Review", color = if (pendingDepositsCount > 0) PakGold else PakTextMuted, modifier = Modifier.weight(1f))
                AdminStatCard(title = "Pending Withdrawals", value = "$pendingWithdrawalsCount", sub = "Needs Approval", color = if (pendingWithdrawalsCount > 0) PakError else PakTextMuted, modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(title = "Approved Deposits", value = "Rs. ${totalDepositVolume.toInt()}", sub = "JazzCash Total", color = PakEmeraldGreen, modifier = Modifier.weight(1f))
                AdminStatCard(title = "Disbursed Payouts", value = "Rs. ${totalWithdrawnVolume.toInt()}", sub = "EasyPaisa / JazzCash", color = PakGold, modifier = Modifier.weight(1f))
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Security & Integrity Notice", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "All administrative actions (approving deposits and payouts) trigger automated real-time SMS notifications to member accounts and instantly commit state changes to the database.",
                        color = PakTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, color.copy(alpha = 0.3f))))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = PakTextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = PakWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = sub, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun AdminDepositsTab(
    deposits: List<Deposit>,
    onApprove: (String) -> Unit,
    onReject: (String, String) -> Unit
) {
    var rejectionDepositId by remember { mutableStateOf<String?>(null) }
    var rejectionReason by remember { mutableStateOf("Invalid TID / Transaction not received") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "DEPOSIT REQUESTS MANAGEMENT", color = PakTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        if (deposits.isEmpty()) {
            item {
                Text(text = "No deposit requests on record.", color = PakTextMuted, fontSize = 12.sp)
            }
        } else {
            items(deposits) { dep ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            if (dep.status == "PENDING") listOf(PakGold, PakGoldDark) else listOf(PakDarkBorder, PakDarkBorder)
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${dep.userName} (${dep.userId})", color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "Sender: ${dep.senderMobile}", color = PakTextSecondary, fontSize = 12.sp)
                            }
                            StatusBadge(status = dep.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(PakDarkSurfaceVariant)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Amount: Rs. ${dep.amount.toInt()}", color = PakEmeraldLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "Method: ${dep.method}", color = PakTextSecondary, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "TID: ${dep.transactionId}", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = "Target: ${dep.depositNumber}", color = PakTextMuted, fontSize = 11.sp)
                            }
                        }

                        if (dep.status == "PENDING") {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onApprove(dep.depositId) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_approve_deposit_${dep.depositId}"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PakEmeraldGreen, contentColor = PakBlackBg)
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Approve (+Rs. ${dep.amount.toInt()})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { onReject(dep.depositId, "Invalid transaction details") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_reject_deposit_${dep.depositId}"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PakError.copy(alpha = 0.2f), contentColor = PakError)
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reject", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminWithdrawalsTab(
    withdrawals: List<Withdrawal>,
    onApprove: (String) -> Unit,
    onReject: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "WITHDRAWAL PAYOUTS MANAGEMENT", color = PakTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        if (withdrawals.isEmpty()) {
            item {
                Text(text = "No withdrawal requests on record.", color = PakTextMuted, fontSize = 12.sp)
            }
        } else {
            items(withdrawals) { wth ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${wth.userName} (${wth.userId})", color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "${wth.method} • ${wth.accountNumber}", color = PakTextSecondary, fontSize = 12.sp)
                                Text(text = "Title: ${wth.accountTitle}", color = PakTextMuted, fontSize = 11.sp)
                            }
                            StatusBadge(status = wth.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Amount: Rs. ${wth.amount.toInt()}", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        if (wth.status == "PENDING") {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onApprove(wth.withdrawalId) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PakGold, contentColor = PakBlackBg)
                                ) {
                                    Text("Disburse Payout", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { onReject(wth.withdrawalId, "Account title mismatch") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PakError.copy(alpha = 0.2f), contentColor = PakError)
                                ) {
                                    Text("Reject & Refund", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminUsersTab(
    users: List<User>,
    onSwitchUser: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "REGISTERED USERS DIRECTORY (${users.size})", color = PakTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        items(users) { usr ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = usr.fullName, color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PakGold.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = usr.userId, color = PakGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(text = "Phone: ${usr.phoneNumber} • PIN: ${usr.securityPin}", color = PakTextSecondary, fontSize = 11.sp)
                        Text(text = "Balance: Rs. ${usr.accountBalance.toInt()} • Plan: ${usr.activePlanName}", color = PakEmeraldLight, fontSize = 11.sp)
                    }

                    Button(
                        onClick = { onSwitchUser(usr.userId) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PakDarkSurfaceVariant, contentColor = PakTextPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simulate", fontSize = 11.sp)
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminVideosTab(
    videos: List<VideoTask>,
    onAddVideo: (String, String, Double, String) -> Unit,
    onDeleteVideo: (Long) -> Unit
) {
    var newTitle by remember { mutableStateOf("") }
    var newChannel by remember { mutableStateOf("") }
    var newReward by remember { mutableStateOf("15") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "ADD NEW 15s VIDEO TASK", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Video Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newChannel,
                        onValueChange = { newChannel = it },
                        label = { Text("Channel Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newReward,
                        onValueChange = { newReward = it },
                        label = { Text("Reward Amount (PKR)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                onAddVideo(newTitle, newChannel.ifBlank { "Pak Finance" }, newReward.toDoubleOrNull() ?: 15.0, "Finance")
                                newTitle = ""
                                newChannel = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PakEmeraldGreen, contentColor = PakBlackBg)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Video Task", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(text = "CURRENT ACTIVE TASKS (${videos.size})", color = PakTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        items(videos) { vid ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = vid.title, color = PakWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1)
                        Text(text = "${vid.channelName} • Rs. ${vid.rewardAmount.toInt()} reward (15s)", color = PakTextSecondary, fontSize = 11.sp)
                    }
                    IconButton(onClick = { onDeleteVideo(vid.id) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = PakError, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminSettingsTab(
    currentNumber: String,
    onUpdate: (String) -> Unit
) {
    var numberText by remember { mutableStateOf(currentNumber) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "SYSTEM SETTINGS", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = numberText,
                    onValueChange = { numberText = it },
                    label = { Text("JazzCash Deposit Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onUpdate(numberText) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PakEmeraldGreen, contentColor = PakBlackBg)
                ) {
                    Text("Save Settings", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

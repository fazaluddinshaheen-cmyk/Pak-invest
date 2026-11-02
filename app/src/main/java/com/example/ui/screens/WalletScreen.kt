package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Deposit
import com.example.data.model.User
import com.example.data.model.Withdrawal
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

@Composable
fun WalletScreen(
    user: User?,
    deposits: List<Deposit>,
    withdrawals: List<Withdrawal>,
    depositNumber: String,
    onSubmitDeposit: (amount: Double, senderPhone: String, tid: String, screenshotUri: String?) -> Unit,
    onSubmitWithdrawal: (method: String, accountNumber: String, accountTitle: String, amount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Deposit, 1 = Withdraw
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PakBlackBg)
    ) {
        // Top Wallet Balance Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PakDarkSurface)
                .border(1.dp, PakDarkBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACCOUNT BALANCE",
                        color = PakTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Rs. ${String.format("%.2f", user?.accountBalance ?: 0.0)}",
                        color = PakWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(PakEmeraldGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Active ID: ${user?.userId ?: "PK-000"}",
                        color = PakEmeraldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Tab Selector (Deposit vs Withdraw)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = PakBlackBg,
            contentColor = PakGold,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = if (selectedTab == 0) PakEmeraldGreen else PakGold,
                    height = 3.dp
                )
            },
            divider = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("JazzCash Deposit", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                },
                selectedContentColor = PakEmeraldGreen,
                unselectedContentColor = PakTextSecondary,
                modifier = Modifier.testTag("tab_deposit")
            )

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Withdrawal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                },
                selectedContentColor = PakGold,
                unselectedContentColor = PakTextSecondary,
                modifier = Modifier.testTag("tab_withdraw")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedTab == 0) {
            DepositContent(
                user = user,
                depositNumber = depositNumber,
                deposits = deposits,
                onSubmitDeposit = onSubmitDeposit,
                onCopyNumber = {
                    clipboardManager.setText(AnnotatedString(depositNumber))
                    Toast.makeText(context, "JazzCash number copied to clipboard!", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            WithdrawContent(
                user = user,
                withdrawals = withdrawals,
                onSubmitWithdrawal = onSubmitWithdrawal
            )
        }
    }
}

@Composable
fun DepositContent(
    user: User?,
    depositNumber: String,
    deposits: List<Deposit>,
    onSubmitDeposit: (amount: Double, senderPhone: String, tid: String, screenshotUri: String?) -> Unit,
    onCopyNumber: () -> Unit
) {
    var amountText by remember { mutableStateOf("100") }
    var senderMobile by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var transactionId by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Official JazzCash Account Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(PakEmeraldGreen, PakEmeraldDark))
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OFFICIAL JAZZCASH DEPOSIT",
                            color = PakEmeraldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PakEmeraldGreen)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "VERIFIED", color = PakBlackBg, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Account Number:", color = PakTextSecondary, fontSize = 11.sp)
                            Text(
                                text = depositNumber,
                                color = PakWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Account Title: Pak Investment Official",
                                color = PakGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = onCopyNumber,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PakEmeraldGreen,
                                contentColor = PakBlackBg
                            ),
                            modifier = Modifier.testTag("copy_deposit_number_button")
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Instructions Steps Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "How to Deposit:",
                        color = PakGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1. Open your JazzCash App & select 'Send Money' -> 'Mobile Account'\n2. Enter Deposit Number: 03242158084\n3. Enter amount (e.g. Rs. 100 for Plan A)\n4. Copy the 11/12-digit Transaction ID (TID) from the SMS & paste below.",
                        color = PakTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Deposit Request Form
        item {
            Text(
                text = "SUBMIT DEPOSIT FORM",
                color = PakTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Amount Quick Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("100", "500", "1000").forEach { chipAmount ->
                    val selected = amountText == chipAmount
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selected) PakEmeraldGreen else PakDarkSurface)
                            .border(1.dp, if (selected) PakEmeraldGreen else PakDarkBorder, RoundedCornerShape(10.dp))
                            .clickable { amountText = chipAmount }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Rs. $chipAmount",
                            color = if (selected) PakBlackBg else PakWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Amount TextField
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Deposit Amount (PKR)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deposit_amount_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Sender Phone
            OutlinedTextField(
                value = senderMobile,
                onValueChange = { senderMobile = it },
                label = { Text("Your JazzCash Sender Number") },
                placeholder = { Text("e.g. 03001234567") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sender_mobile_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Transaction ID (TID)
            OutlinedTextField(
                value = transactionId,
                onValueChange = { transactionId = it },
                label = { Text("Transaction ID (TID from JazzCash)") },
                placeholder = { Text("e.g. JC9928104821") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tid_input"),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Screenshot Upload Button (Android Photo Picker)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PakDarkSurface)
                    .border(1.dp, PakDarkBorder, RoundedCornerShape(12.dp))
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (selectedImageUri != null) Icons.Default.Check else Icons.Default.AddPhotoAlternate,
                        contentDescription = "Upload Screenshot",
                        tint = if (selectedImageUri != null) PakSuccess else PakGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (selectedImageUri != null) "Screenshot Attached" else "Upload Payment Screenshot",
                            color = PakWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (selectedImageUri != null) "Ready for Admin Verification" else "Tap to choose image (Optional)",
                            color = PakTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = if (selectedImageUri != null) "Change" else "Browse",
                    color = PakGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 100.0
                    onSubmitDeposit(amt, senderMobile, transactionId, selectedImageUri?.toString())
                    transactionId = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_deposit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PakEmeraldGreen,
                    contentColor = PakBlackBg
                )
            ) {
                Text("Submit Deposit Request", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        // Deposit History
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "YOUR DEPOSIT HISTORY",
                color = PakTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        if (deposits.isEmpty()) {
            item {
                Text(text = "No deposits yet.", color = PakTextMuted, fontSize = 12.sp)
            }
        } else {
            items(deposits) { dep ->
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
                            Text(text = "JazzCash • Rs. ${dep.amount.toInt()}", color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "TID: ${dep.transactionId}", color = PakTextSecondary, fontSize = 12.sp)
                            Text(text = "Sender: ${dep.senderMobile}", color = PakTextMuted, fontSize = 11.sp)
                        }
                        StatusBadge(status = dep.status)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun WithdrawContent(
    user: User?,
    withdrawals: List<Withdrawal>,
    onSubmitWithdrawal: (method: String, accountNumber: String, accountTitle: String, amount: Double) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("EasyPaisa") } // EasyPaisa or JazzCash
    var accountNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var accountTitle by remember { mutableStateOf(user?.fullName ?: "") }
    var amountText by remember { mutableStateOf("300") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Limits & Balance info card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakGold.copy(alpha = 0.3f))))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "WITHDRAWAL LIMITS", color = PakTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Min: Rs. 100 • Max: Rs. 1,000", color = PakGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Available Balance", color = PakTextSecondary, fontSize = 10.sp)
                        Text(text = "Rs. ${user?.accountBalance?.toInt() ?: 0}", color = PakEmeraldLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Method Selector (EasyPaisa vs JazzCash)
        item {
            Text(
                text = "SELECT WITHDRAWAL METHOD",
                color = PakTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("EasyPaisa", "JazzCash").forEach { method ->
                    val isSelected = selectedMethod == method
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedMethod = method },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) PakDarkSurfaceVariant else PakDarkSurface),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(
                                if (isSelected) listOf(PakGold, PakGold) else listOf(PakDarkBorder, PakDarkBorder)
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) PakGold else PakDarkBorder),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = method,
                                    tint = if (isSelected) PakBlackBg else PakTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = method,
                                color = if (isSelected) PakGold else PakWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Withdrawal Request Form
        item {
            // Quick amount chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("100", "300", "500", "1000").forEach { chip ->
                    val sel = amountText == chip
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (sel) PakGold else PakDarkSurface)
                            .border(1.dp, if (sel) PakGold else PakDarkBorder, RoundedCornerShape(8.dp))
                            .clickable { amountText = chip }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Rs. $chip",
                            color = if (sel) PakBlackBg else PakWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Amount field
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount (Max Rs. 1,000)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("withdraw_amount_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Account Title
            OutlinedTextField(
                value = accountTitle,
                onValueChange = { accountTitle = it },
                label = { Text("Account Holder Title / Name") },
                placeholder = { Text("e.g. Muhammad Ali") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("withdraw_title_input"),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Account Number
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("$selectedMethod Mobile Account Number") },
                placeholder = { Text("e.g. 03001234567") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("withdraw_number_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Submit Button
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 100.0
                    onSubmitWithdrawal(selectedMethod, accountNumber, accountTitle, amt)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_withdraw_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PakGold,
                    contentColor = PakBlackBg
                )
            ) {
                Text("Submit Withdrawal Request", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        // Live Withdrawal Status & History
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "LIVE WITHDRAWAL STATUS",
                color = PakTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        if (withdrawals.isEmpty()) {
            item {
                Text(text = "No withdrawal requests yet.", color = PakTextMuted, fontSize = 12.sp)
            }
        } else {
            items(withdrawals) { wth ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${wth.method} • Rs. ${wth.amount.toInt()}", color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "Account: ${wth.accountNumber} (${wth.accountTitle})", color = PakTextSecondary, fontSize = 12.sp)
                            }
                            StatusBadge(status = wth.status)
                        }

                        if (wth.rejectionReason != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Note: ${wth.rejectionReason}",
                                color = Color(0xFFFF8A80),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PakEmeraldGreen,
    unfocusedBorderColor = PakDarkBorder,
    focusedLabelColor = PakEmeraldGreen,
    unfocusedLabelColor = PakTextSecondary,
    focusedTextColor = PakWhite,
    unfocusedTextColor = PakWhite,
    cursorColor = PakEmeraldGreen
)

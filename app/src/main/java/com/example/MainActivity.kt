package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.PakBottomNav
import com.example.ui.components.PakTopBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PlansScreen
import com.example.ui.screens.ReferralScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.WatchEarnScreen
import com.example.ui.screens.outlinedTextFieldColors
import com.example.ui.theme.PakBlackBg
import com.example.ui.theme.PakDarkSurface
import com.example.ui.theme.PakEmeraldGreen
import com.example.ui.theme.PakEmeraldLight
import com.example.ui.theme.PakGold
import com.example.ui.theme.PakInvestmentTheme
import com.example.ui.theme.PakTextSecondary
import com.example.ui.theme.PakWhite
import com.example.ui.viewmodel.PakInvestViewModel
import com.example.ui.viewmodel.ScreenTab
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel: PakInvestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PakInvestmentTheme {
                PakInvestmentApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PakInvestmentApp(viewModel: PakInvestViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    val currentUser by viewModel.currentUser.collectAsState()
    val userDeposits by viewModel.userDeposits.collectAsState()
    val userWithdrawals by viewModel.userWithdrawals.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState()
    val userReferrals by viewModel.userReferrals.collectAsState()
    val userNotifications by viewModel.userNotifications.collectAsState()
    val videoWatchState by viewModel.videoWatchState.collectAsState()

    val allUsers by viewModel.allUsers.collectAsState()
    val allDeposits by viewModel.allDeposits.collectAsState()
    val allWithdrawals by viewModel.allWithdrawals.collectAsState()
    val currentDepositNumber by viewModel.repository.jazzCashDepositNumber.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var showAdminPinDialog by remember { mutableStateOf(false) }
    var enteredAdminPin by remember { mutableStateOf("") }

    // Listen to snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Predictive back handler
    BackHandler(enabled = currentTab != ScreenTab.HOME || isAdminMode) {
        if (isAdminMode) {
            viewModel.toggleAdminMode(false)
        } else if (currentTab != ScreenTab.HOME) {
            viewModel.selectTab(ScreenTab.HOME)
        }
    }

    if (!isLoggedIn) {
        AuthScreen(
            onLoginSuccess = { phone, pin -> viewModel.loginUser(phone, pin) },
            onRegisterSuccess = { fullName, phone, email, pin, referral ->
                viewModel.registerUser(fullName, phone, email, pin, referral)
            },
            onQuickDemoLogin = { uid -> viewModel.switchUserQuick(uid) }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PakBlackBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (!isAdminMode) {
                PakTopBar(
                    userId = currentUser?.userId,
                    unreadNotificationsCount = userNotifications.count { !it.isRead },
                    isAdminMode = isAdminMode,
                    onNotificationClick = { viewModel.selectTab(ScreenTab.NOTIFICATIONS) },
                    onAdminToggleClick = { showAdminPinDialog = true }
                )
            }
        },
        bottomBar = {
            if (!isAdminMode && currentTab != ScreenTab.NOTIFICATIONS) {
                PakBottomNav(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isAdminMode || currentTab == ScreenTab.ADMIN -> {
                    AdminScreen(
                        users = allUsers,
                        deposits = allDeposits,
                        withdrawals = allWithdrawals,
                        videos = allVideos,
                        currentDepositNumber = currentDepositNumber,
                        onApproveDeposit = { viewModel.approveDeposit(it) },
                        onRejectDeposit = { id, reason -> viewModel.rejectDeposit(id, reason) },
                        onApproveWithdrawal = { viewModel.approveWithdrawal(it) },
                        onRejectWithdrawal = { id, reason -> viewModel.rejectWithdrawal(id, reason) },
                        onAddVideo = { title, channel, reward, cat ->
                            viewModel.launchScoped {
                                viewModel.repository.adminAddVideoTask(title, channel, reward, cat)
                                viewModel.emitMessage("Video task added successfully!")
                            }
                        },
                        onDeleteVideo = {
                            viewModel.launchScoped {
                                viewModel.repository.adminDeleteVideo(it)
                                viewModel.emitMessage("Video task deleted.")
                            }
                        },
                        onUpdateDepositNumber = {
                            viewModel.repository.updateJazzCashNumber(it)
                            viewModel.emitMessage("JazzCash number updated to $it")
                        },
                        onSwitchUser = { uid -> viewModel.switchUserQuick(uid) },
                        onExitAdmin = { viewModel.toggleAdminMode(false) }
                    )
                }

                currentTab == ScreenTab.HOME -> {
                    HomeScreen(
                        user = currentUser,
                        deposits = userDeposits,
                        withdrawals = userWithdrawals,
                        onNavigate = { viewModel.selectTab(it) }
                    )
                }

                currentTab == ScreenTab.PLANS -> {
                    PlansScreen(
                        user = currentUser,
                        onActivatePlan = { name, cost -> viewModel.activatePlan(name, cost) },
                        onNavigate = { viewModel.selectTab(it) }
                    )
                }

                currentTab == ScreenTab.WATCH_EARN -> {
                    WatchEarnScreen(
                        user = currentUser,
                        videos = allVideos,
                        watchState = videoWatchState,
                        onStartWatch = { viewModel.startWatchingVideo(it) },
                        onClosePlayer = { viewModel.closeVideoPlayer() },
                        onNavigate = { viewModel.selectTab(it) }
                    )
                }

                currentTab == ScreenTab.WALLET -> {
                    WalletScreen(
                        user = currentUser,
                        deposits = userDeposits,
                        withdrawals = userWithdrawals,
                        depositNumber = currentDepositNumber,
                        onSubmitDeposit = { amt, sender, tid, screenshot ->
                            viewModel.submitDeposit(amt, sender, tid, screenshot)
                        },
                        onSubmitWithdrawal = { method, number, title, amt ->
                            viewModel.submitWithdrawal(method, number, title, amt)
                        }
                    )
                }

                currentTab == ScreenTab.REFERRALS -> {
                    ReferralScreen(
                        user = currentUser,
                        referrals = userReferrals
                    )
                }

                currentTab == ScreenTab.NOTIFICATIONS -> {
                    NotificationsScreen(
                        notifications = userNotifications,
                        onBack = { viewModel.selectTab(ScreenTab.HOME) }
                    )
                }
            }
        }

        // Admin Access Dialog
        if (showAdminPinDialog) {
            Dialog(onDismissRequest = { showAdminPinDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    color = PakDarkSurface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Administrator Security Portal",
                            color = PakGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enter Administrator PIN to manage deposits, withdrawals, users and videos. (Default PIN: 7860)",
                            color = PakTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = enteredAdminPin,
                            onValueChange = { enteredAdminPin = it },
                            label = { Text("Admin PIN") },
                            placeholder = { Text("7860") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = outlinedTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (enteredAdminPin == "7860" || enteredAdminPin == "admin") {
                                    showAdminPinDialog = false
                                    enteredAdminPin = ""
                                    viewModel.toggleAdminMode(true)
                                } else {
                                    Toast.makeText(context, "Incorrect PIN! Use 7860 for demonstration.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PakGold, contentColor = PakBlackBg)
                        ) {
                            Text("Unlock Admin Dashboard", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                showAdminPinDialog = false
                                viewModel.toggleAdminMode(true)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PakDarkSurface, contentColor = PakEmeraldLight)
                        ) {
                            Text("1-Tap Admin Demo Access", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

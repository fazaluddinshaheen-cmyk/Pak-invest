package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AppNotification
import com.example.data.model.Deposit
import com.example.data.model.Referral
import com.example.data.model.User
import com.example.data.model.VideoTask
import com.example.data.model.Withdrawal
import com.example.data.repository.PakInvestRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    PLANS,
    WATCH_EARN,
    WALLET,
    REFERRALS,
    NOTIFICATIONS,
    ADMIN
}

data class VideoWatchState(
    val currentVideo: VideoTask? = null,
    val isPlaying: Boolean = false,
    val secondsRemaining: Int = 15,
    val isCompleted: Boolean = false,
    val rewardClaimed: Double? = null
)

class PakInvestViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    val repository = PakInvestRepository(database, viewModelScope)

    // Navigation
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Auth screen state
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    // User data
    val currentUser: StateFlow<User?> = repository.getCurrentUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userDeposits: StateFlow<List<Deposit>> = repository.getDepositsForCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userWithdrawals: StateFlow<List<Withdrawal>> = repository.getWithdrawalsForCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVideos: StateFlow<List<VideoTask>> = repository.getAllVideos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userReferrals: StateFlow<List<Referral>> = repository.getReferralsForCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userNotifications: StateFlow<List<AppNotification>> = repository.getNotificationsForCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin feeds
    val allUsers: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDeposits: StateFlow<List<Deposit>> = repository.getAllDeposits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWithdrawals: StateFlow<List<Withdrawal>> = repository.getAllWithdrawals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReferrals: StateFlow<List<Referral>> = repository.getAllReferrals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Feedback UI messages
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    // Video Watch countdown state
    private val _videoWatchState = MutableStateFlow(VideoWatchState())
    val videoWatchState: StateFlow<VideoWatchState> = _videoWatchState.asStateFlow()
    private var watchJob: Job? = null

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun toggleAdminMode(enable: Boolean) {
        _isAdminMode.value = enable
        if (enable) {
            _currentTab.value = ScreenTab.ADMIN
        } else {
            _currentTab.value = ScreenTab.HOME
        }
    }

    fun emitMessage(msg: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(msg)
        }
    }

    // Video Watching Logic
    fun startWatchingVideo(video: VideoTask) {
        val user = currentUser.value
        if (user == null || !user.isPlanActive) {
            emitMessage("Plan A is locked! Please activate Plan A (Rs. 100) first to watch and earn.")
            return
        }

        val today = repository.getTodayDate()
        val count = if (user.lastWatchDate == today) user.videosWatchedToday else 0
        if (count >= 10) {
            emitMessage("Daily video limit reached (10/10). Come back tomorrow!")
            return
        }

        watchJob?.cancel()
        _videoWatchState.value = VideoWatchState(
            currentVideo = video,
            isPlaying = true,
            secondsRemaining = 15,
            isCompleted = false,
            rewardClaimed = null
        )

        watchJob = viewModelScope.launch {
            for (sec in 14 downTo 0) {
                delay(1000)
                _videoWatchState.value = _videoWatchState.value.copy(
                    secondsRemaining = sec
                )
            }
            // Finished 15 seconds
            _videoWatchState.value = _videoWatchState.value.copy(
                isPlaying = false,
                isCompleted = true
            )
            // Automatically credit reward
            val result = repository.recordVideoWatch(video.id)
            result.onSuccess { reward ->
                _videoWatchState.value = _videoWatchState.value.copy(
                    rewardClaimed = reward
                )
                emitMessage("Video verified! +Rs. ${reward.toInt()} credited to your balance.")
            }.onFailure { err ->
                emitMessage(err.message ?: "Could not claim reward")
            }
        }
    }

    fun closeVideoPlayer() {
        watchJob?.cancel()
        _videoWatchState.value = VideoWatchState()
    }

    // Plans
    fun activatePlan(planName: String, cost: Double) {
        viewModelScope.launch {
            val result = repository.activatePlan(planName, cost)
            result.onSuccess {
                emitMessage("$planName activated! You can now watch 10 videos daily.")
            }.onFailure { err ->
                emitMessage(err.message ?: "Failed to activate plan")
            }
        }
    }

    // Deposits
    fun submitDeposit(amount: Double, senderPhone: String, tid: String, screenshotUri: String? = null) {
        viewModelScope.launch {
            val result = repository.submitDeposit(amount, senderPhone, tid, screenshotUri)
            result.onSuccess {
                emitMessage("Deposit request submitted! Admin will verify TID $tid shortly.")
            }.onFailure { err ->
                emitMessage(err.message ?: "Failed to submit deposit")
            }
        }
    }

    // Withdrawals
    fun submitWithdrawal(method: String, accountNumber: String, accountTitle: String, amount: Double) {
        viewModelScope.launch {
            val result = repository.submitWithdrawal(method, accountNumber, accountTitle, amount)
            result.onSuccess {
                emitMessage("Withdrawal of Rs. ${amount.toInt()} requested! Live status: Pending.")
            }.onFailure { err ->
                emitMessage(err.message ?: "Failed to submit withdrawal")
            }
        }
    }

    // Admin Actions
    fun approveDeposit(depositId: String) {
        viewModelScope.launch {
            val res = repository.adminApproveDeposit(depositId)
            res.onSuccess {
                emitMessage("Deposit $depositId approved and balance credited!")
            }.onFailure {
                emitMessage(it.message ?: "Error approving deposit")
            }
        }
    }

    fun rejectDeposit(depositId: String, reason: String) {
        viewModelScope.launch {
            val res = repository.adminRejectDeposit(depositId, reason)
            res.onSuccess {
                emitMessage("Deposit $depositId rejected.")
            }.onFailure {
                emitMessage(it.message ?: "Error rejecting deposit")
            }
        }
    }

    fun approveWithdrawal(withdrawalId: String) {
        viewModelScope.launch {
            val res = repository.adminApproveWithdrawal(withdrawalId)
            res.onSuccess {
                emitMessage("Withdrawal $withdrawalId disbursed and approved!")
            }.onFailure {
                emitMessage(it.message ?: "Error approving withdrawal")
            }
        }
    }

    fun rejectWithdrawal(withdrawalId: String, reason: String) {
        viewModelScope.launch {
            val res = repository.adminRejectWithdrawal(withdrawalId, reason)
            res.onSuccess {
                emitMessage("Withdrawal $withdrawalId rejected and funds refunded.")
            }.onFailure {
                emitMessage(it.message ?: "Error rejecting withdrawal")
            }
        }
    }

    fun registerUser(fullName: String, phone: String, email: String, pin: String, referral: String?) {
        viewModelScope.launch {
            val res = repository.registerUser(fullName, phone, email, pin, referral)
            res.onSuccess {
                _isLoggedIn.value = true
                emitMessage("Account created successfully! Welcome ${it.fullName}")
            }.onFailure {
                emitMessage(it.message ?: "Registration failed")
            }
        }
    }

    fun loginUser(phone: String, pin: String) {
        viewModelScope.launch {
            val res = repository.loginUser(phone, pin)
            res.onSuccess {
                _isLoggedIn.value = true
                emitMessage("Logged in as ${it.fullName}")
            }.onFailure {
                emitMessage(it.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        emitMessage("Logged out successfully")
    }

    fun switchUserQuick(userId: String) {
        repository.switchUser(userId)
        _isLoggedIn.value = true
        emitMessage("Switched active member to $userId")
    }

    fun launchScoped(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}

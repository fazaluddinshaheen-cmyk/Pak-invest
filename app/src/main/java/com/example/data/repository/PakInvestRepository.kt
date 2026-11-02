package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AppNotification
import com.example.data.model.Deposit
import com.example.data.model.Referral
import com.example.data.model.User
import com.example.data.model.VideoTask
import com.example.data.model.Withdrawal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class PakInvestRepository(
    private val db: AppDatabase,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val userDao = db.userDao()
    private val depositDao = db.depositDao()
    private val withdrawalDao = db.withdrawalDao()
    private val videoDao = db.videoDao()
    private val referralDao = db.referralDao()
    private val notificationDao = db.notificationDao()

    private val _currentUserId = MutableStateFlow<String?>("PK-786012")
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    // Configurable platform settings
    val jazzCashDepositNumber = MutableStateFlow("03242158084")
    val jazzCashAccountTitle = MutableStateFlow("Pak Investment Official (JazzCash)")
    val minWithdrawalAmount = MutableStateFlow(100.0)
    val maxWithdrawalAmount = MutableStateFlow(1000.0)
    val referralBonusAmount = MutableStateFlow(75.0)

    init {
        externalScope.launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val count = userDao.getUserByUserId("PK-786012")
        if (count == null) {
            // Primary demo user
            val defaultUser = User(
                userId = "PK-786012",
                fullName = "Muhammad Ali Khan",
                phoneNumber = "03001234567",
                email = "ali.investor@pakinvestment.pk",
                passwordHash = "password123",
                securityPin = "7860",
                accountBalance = 350.0,
                totalEarned = 720.0,
                totalWithdrawn = 300.0,
                isPlanActive = true,
                activePlanName = "Plan A (Rs. 100)",
                planActivatedAt = System.currentTimeMillis() - 86400000L * 3,
                videosWatchedToday = 3,
                lastWatchDate = getTodayDate(),
                referralCode = "PK-786012",
                isVerified = true,
                role = "USER"
            )
            userDao.insertUser(defaultUser)

            // Second user for admin management testing
            val user2 = User(
                userId = "PK-991204",
                fullName = "Hamza Tariq",
                phoneNumber = "03219876543",
                email = "hamza.tariq@gmail.com",
                passwordHash = "pass123",
                securityPin = "1122",
                accountBalance = 120.0,
                totalEarned = 120.0,
                totalWithdrawn = 0.0,
                isPlanActive = false,
                activePlanName = "None",
                referralCode = "PK-991204",
                referredByCode = "PK-786012",
                isVerified = true,
                role = "USER"
            )
            userDao.insertUser(user2)

            // Seed videos (Plan A 10 daily videos)
            val videos = listOf(
                VideoTask(title = "Pakistan Stock Exchange: Beginner Profit Secrets", channelName = "Pak Financial Insights", durationSeconds = 15, rewardAmount = 15.0, category = "Investing", thumbnailKey = "psx"),
                VideoTask(title = "JazzCash & EasyPaisa Merchant Growth Strategy", channelName = "Fintech Pakistan", durationSeconds = 15, rewardAmount = 15.0, category = "Fintech", thumbnailKey = "fintech"),
                VideoTask(title = "Freelance Remittance to Pakistan: Zero Tax Method", channelName = "Digital Pakistan", durationSeconds = 15, rewardAmount = 15.0, category = "Freelancing", thumbnailKey = "remittance"),
                VideoTask(title = "Gold vs National Savings Certificate Comparison", channelName = "Economic Forum PK", durationSeconds = 15, rewardAmount = 15.0, category = "Savings", thumbnailKey = "gold"),
                VideoTask(title = "E-Commerce Sourcing from Faisalabad & Karachi", channelName = "Daraz Pros PK", durationSeconds = 15, rewardAmount = 15.0, category = "Business", thumbnailKey = "business"),
                VideoTask(title = "Real Estate Micro-Investments in Islamabad & Lahore", channelName = "Property Pakistan", durationSeconds = 15, rewardAmount = 15.0, category = "Real Estate", thumbnailKey = "property"),
                VideoTask(title = "High Dividend Yield Companies in PSX 100 Index", channelName = "Stock Master PK", durationSeconds = 15, rewardAmount = 15.0, category = "Stocks", thumbnailKey = "dividend"),
                VideoTask(title = "Smart Budgeting & Inflation Shield in Pakistan", channelName = "Money Wise PK", durationSeconds = 15, rewardAmount = 15.0, category = "Budgeting", thumbnailKey = "budget"),
                VideoTask(title = "IT Exports & Software House Growth in 2026", channelName = "Tech Hive Lahore", durationSeconds = 15, rewardAmount = 15.0, category = "Tech", thumbnailKey = "tech"),
                VideoTask(title = "Mutual Funds & Islamic Sukuk Investment Guide", channelName = "Halal Invest PK", durationSeconds = 15, rewardAmount = 15.0, category = "Islamic Finance", thumbnailKey = "islamic")
            )
            videoDao.insertAll(videos)

            // Seed initial referrals
            referralDao.insertReferral(Referral(referrerUserId = "PK-786012", referredName = "Bilal Ahmed", referredPhone = "0333-8492019", bonusAmount = 75.0, status = "QUALIFIED", dateFormatted = "28 Oct 2026"))
            referralDao.insertReferral(Referral(referrerUserId = "PK-786012", referredName = "Usman Khalid", referredPhone = "0345-1928374", bonusAmount = 75.0, status = "QUALIFIED", dateFormatted = "30 Oct 2026"))
            referralDao.insertReferral(Referral(referrerUserId = "PK-786012", referredName = "Sana Mir", referredPhone = "0312-9988776", bonusAmount = 75.0, status = "PENDING_PLAN", dateFormatted = "01 Nov 2026"))

            // Seed initial deposits (one approved for user, one pending for admin test)
            depositDao.insertDeposit(
                Deposit(
                    depositId = "DEP-9081",
                    userId = "PK-786012",
                    userName = "Muhammad Ali Khan",
                    method = "JazzCash",
                    depositNumber = "03242158084",
                    amount = 500.0,
                    senderMobile = "03001234567",
                    transactionId = "JC8492018490",
                    status = "APPROVED",
                    createdAt = System.currentTimeMillis() - 86400000L * 2
                )
            )
            depositDao.insertDeposit(
                Deposit(
                    depositId = "DEP-9142",
                    userId = "PK-991204",
                    userName = "Hamza Tariq",
                    method = "JazzCash",
                    depositNumber = "03242158084",
                    amount = 100.0,
                    senderMobile = "03219876543",
                    transactionId = "JC9948201842",
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 3600000L * 2
                )
            )

            // Seed initial withdrawals
            withdrawalDao.insertWithdrawal(
                Withdrawal(
                    withdrawalId = "WTH-3021",
                    userId = "PK-786012",
                    userName = "Muhammad Ali Khan",
                    method = "EasyPaisa",
                    accountNumber = "03001234567",
                    accountTitle = "Muhammad Ali",
                    amount = 300.0,
                    status = "APPROVED",
                    createdAt = System.currentTimeMillis() - 86400000L
                )
            )
            withdrawalDao.insertWithdrawal(
                Withdrawal(
                    withdrawalId = "WTH-3088",
                    userId = "PK-991204",
                    userName = "Hamza Tariq",
                    method = "JazzCash",
                    accountNumber = "03219876543",
                    accountTitle = "Hamza Tariq",
                    amount = 100.0,
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 1800000L
                )
            )

            // Seed initial SMS/Push notifications
            notificationDao.insertNotification(
                AppNotification(
                    userId = "PK-786012",
                    title = "SMS: Deposit Approved",
                    message = "Dear Muhammad Ali, your JazzCash deposit of Rs. 500 (TID: JC8492018490) has been approved. Balance updated.",
                    type = "SMS"
                )
            )
            notificationDao.insertNotification(
                AppNotification(
                    userId = "PK-786012",
                    title = "SMS: Plan A Activated",
                    message = "Congratulations! Plan A (Rs. 100) has been activated. You can now watch 10 daily videos of 15 seconds to earn rewards.",
                    type = "SMS"
                )
            )
            notificationDao.insertNotification(
                AppNotification(
                    userId = "PK-786012",
                    title = "SMS: Referral Bonus Received",
                    message = "You received Rs. 75 referral bonus because your friend Bilal Ahmed joined and activated Plan A.",
                    type = "SMS"
                )
            )
        }
    }

    fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Auth & Session
    fun getCurrentUserFlow(): Flow<User?> {
        val uid = _currentUserId.value ?: "PK-786012"
        return userDao.getUserFlow(uid)
    }

    suspend fun getCurrentUser(): User? {
        val uid = _currentUserId.value ?: return null
        return userDao.getUserByUserId(uid)
    }

    fun switchUser(userId: String) {
        _currentUserId.value = userId
    }

    suspend fun registerUser(
        fullName: String,
        phone: String,
        email: String,
        pin: String,
        referredBy: String?
    ): Result<User> {
        val existing = userDao.getUserByPhone(phone)
        if (existing != null) {
            return Result.failure(Exception("Mobile number already registered! Please log in."))
        }
        val randomNum = Random.nextInt(100000, 999999)
        val newUserId = "PK-$randomNum"
        val newUser = User(
            userId = newUserId,
            fullName = fullName.trim(),
            phoneNumber = phone.trim(),
            email = email.trim(),
            passwordHash = "pass_$pin",
            securityPin = pin.trim(),
            accountBalance = 0.0,
            referralCode = newUserId,
            referredByCode = referredBy?.trim()?.ifBlank { null },
            lastWatchDate = getTodayDate(),
            isVerified = true
        )
        userDao.insertUser(newUser)
        _currentUserId.value = newUserId

        // Add welcome notification & SMS
        notificationDao.insertNotification(
            AppNotification(
                userId = newUserId,
                title = "SMS: Welcome to Pak Investment",
                message = "Welcome $fullName! Your User ID is $newUserId. Deposit via JazzCash 03242158084 to activate Plan A and start earning.",
                type = "SMS"
            )
        )

        // If referred by someone, record referral
        if (!referredBy.isNullOrBlank()) {
            val referrer = userDao.getUserByUserId(referredBy.trim())
            if (referrer != null) {
                referralDao.insertReferral(
                    Referral(
                        referrerUserId = referrer.userId,
                        referredName = fullName,
                        referredPhone = phone,
                        bonusAmount = 75.0,
                        status = "PENDING_PLAN",
                        dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                    )
                )
            }
        }

        return Result.success(newUser)
    }

    suspend fun loginUser(phone: String, pin: String): Result<User> {
        val user = userDao.getUserByPhone(phone.trim())
            ?: return Result.failure(Exception("Account not found for this mobile number!"))
        if (user.securityPin != pin.trim()) {
            return Result.failure(Exception("Invalid 2-Step Security PIN!"))
        }
        _currentUserId.value = user.userId
        return Result.success(user)
    }

    // Videos
    fun getAllVideos(): Flow<List<VideoTask>> = videoDao.getAllVideosFlow()

    suspend fun recordVideoWatch(videoId: Long): Result<Double> {
        val user = getCurrentUser() ?: return Result.failure(Exception("User not authenticated"))
        if (!user.isPlanActive) {
            return Result.failure(Exception("Plan A must be activated to earn from video tasks! Deposit Rs. 100 first."))
        }

        val today = getTodayDate()
        var watchedToday = user.videosWatchedToday
        if (user.lastWatchDate != today) {
            watchedToday = 0
        }

        if (watchedToday >= 10) {
            return Result.failure(Exception("Daily video limit reached (10/10). Come back tomorrow for new tasks!"))
        }

        val reward = 15.0
        val newCount = watchedToday + 1
        userDao.updateVideoWatchCount(user.userId, newCount, today)
        userDao.addBalance(user.userId, reward)

        notificationDao.insertNotification(
            AppNotification(
                userId = user.userId,
                title = "Reward Credited (+Rs. $reward)",
                message = "Task completed ($newCount/10). Rs. $reward added to your balance.",
                type = "EARN"
            )
        )

        return Result.success(reward)
    }

    // Plan Activation
    suspend fun activatePlan(planName: String, cost: Double): Result<Unit> {
        val user = getCurrentUser() ?: return Result.failure(Exception("User not authenticated"))
        if (user.accountBalance < cost) {
            return Result.failure(Exception("Insufficient balance! You need Rs. $cost. Please deposit first via JazzCash."))
        }
        userDao.deductBalance(user.userId, cost)
        userDao.activatePlan(user.userId, planName, System.currentTimeMillis())

        // If user was referred, credit referrer Rs. 75
        user.referredByCode?.let { refCode ->
            userDao.addBalance(refCode, 75.0)
            notificationDao.insertNotification(
                AppNotification(
                    userId = refCode,
                    title = "SMS: Referral Bonus Rs. 75",
                    message = "Your friend ${user.fullName} activated a plan! Rs. 75 referral bonus credited to your account.",
                    type = "SMS"
                )
            )
        }

        notificationDao.insertNotification(
            AppNotification(
                userId = user.userId,
                title = "SMS: Plan Activated",
                message = "Your $planName has been activated successfully! Daily 10 videos unlocked.",
                type = "SMS"
            )
        )

        return Result.success(Unit)
    }

    // Deposits
    fun getDepositsForCurrentUser(): Flow<List<Deposit>> {
        val uid = _currentUserId.value ?: "PK-786012"
        return depositDao.getDepositsForUser(uid)
    }

    fun getAllDeposits(): Flow<List<Deposit>> = depositDao.getAllDepositsFlow()

    suspend fun submitDeposit(
        amount: Double,
        senderMobile: String,
        transactionId: String,
        screenshotUri: String?
    ): Result<Deposit> {
        val user = getCurrentUser() ?: return Result.failure(Exception("User not authenticated"))
        if (amount < 100.0) {
            return Result.failure(Exception("Minimum deposit amount is Rs. 100"))
        }
        if (transactionId.length < 8) {
            return Result.failure(Exception("Please enter a valid Transaction ID (TID)"))
        }

        val depId = "DEP-${Random.nextInt(1000, 9999)}"
        val deposit = Deposit(
            depositId = depId,
            userId = user.userId,
            userName = user.fullName,
            method = "JazzCash",
            depositNumber = jazzCashDepositNumber.value,
            amount = amount,
            senderMobile = senderMobile,
            transactionId = transactionId.uppercase(),
            screenshotUri = screenshotUri,
            status = "PENDING"
        )
        depositDao.insertDeposit(deposit)

        notificationDao.insertNotification(
            AppNotification(
                userId = user.userId,
                title = "SMS: Deposit Request Received",
                message = "Pak Investment: Deposit request for Rs. ${amount.toInt()} with TID $transactionId has been received. Admin approval will take 5-15 mins.",
                type = "SMS"
            )
        )

        return Result.success(deposit)
    }

    // Withdrawals
    fun getWithdrawalsForCurrentUser(): Flow<List<Withdrawal>> {
        val uid = _currentUserId.value ?: "PK-786012"
        return withdrawalDao.getWithdrawalsForUser(uid)
    }

    fun getAllWithdrawals(): Flow<List<Withdrawal>> = withdrawalDao.getAllWithdrawalsFlow()

    suspend fun submitWithdrawal(
        method: String,
        accountNumber: String,
        accountTitle: String,
        amount: Double
    ): Result<Withdrawal> {
        val user = getCurrentUser() ?: return Result.failure(Exception("User not authenticated"))
        if (amount < minWithdrawalAmount.value) {
            return Result.failure(Exception("Minimum withdrawal is Rs. ${minWithdrawalAmount.value.toInt()}"))
        }
        if (amount > maxWithdrawalAmount.value) {
            return Result.failure(Exception("Maximum withdrawal is Rs. ${maxWithdrawalAmount.value.toInt()}"))
        }
        if (user.accountBalance < amount) {
            return Result.failure(Exception("Insufficient balance! Your balance is Rs. ${user.accountBalance.toInt()}"))
        }

        userDao.deductBalance(user.userId, amount)
        val wthId = "WTH-${Random.nextInt(1000, 9999)}"
        val withdrawal = Withdrawal(
            withdrawalId = wthId,
            userId = user.userId,
            userName = user.fullName,
            method = method,
            accountNumber = accountNumber,
            accountTitle = accountTitle,
            amount = amount,
            status = "PENDING"
        )
        withdrawalDao.insertWithdrawal(withdrawal)

        notificationDao.insertNotification(
            AppNotification(
                userId = user.userId,
                title = "SMS: Withdrawal Request Submitted",
                message = "Pak Investment: Withdrawal of Rs. ${amount.toInt()} to $method ($accountNumber) submitted. Processing typically takes under 1 hour.",
                type = "SMS"
            )
        )

        return Result.success(withdrawal)
    }

    // Referrals
    fun getReferralsForCurrentUser(): Flow<List<Referral>> {
        val uid = _currentUserId.value ?: "PK-786012"
        return referralDao.getReferralsForUser(uid)
    }

    fun getAllReferrals(): Flow<List<Referral>> = referralDao.getAllReferrals()

    // Notifications
    fun getNotificationsForCurrentUser(): Flow<List<AppNotification>> {
        val uid = _currentUserId.value ?: "PK-786012"
        return notificationDao.getNotificationsForUser(uid)
    }

    // Admin Operations
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsersFlow()

    suspend fun adminApproveDeposit(depositId: String): Result<Unit> {
        val dep = depositDao.getDepositById(depositId) ?: return Result.failure(Exception("Deposit not found"))
        if (dep.status == "APPROVED") return Result.failure(Exception("Deposit already approved"))

        depositDao.updateDepositStatus(depositId, "APPROVED", null)
        userDao.addBalance(dep.userId, dep.amount)

        notificationDao.insertNotification(
            AppNotification(
                userId = dep.userId,
                title = "SMS: Deposit APPROVED",
                message = "Dear ${dep.userName}, your JazzCash deposit of Rs. ${dep.amount.toInt()} (TID: ${dep.transactionId}) has been APPROVED. Your balance has been updated.",
                type = "SMS"
            )
        )
        return Result.success(Unit)
    }

    suspend fun adminRejectDeposit(depositId: String, reason: String): Result<Unit> {
        val dep = depositDao.getDepositById(depositId) ?: return Result.failure(Exception("Deposit not found"))
        depositDao.updateDepositStatus(depositId, "REJECTED", reason)

        notificationDao.insertNotification(
            AppNotification(
                userId = dep.userId,
                title = "SMS: Deposit REJECTED",
                message = "Dear ${dep.userName}, deposit of Rs. ${dep.amount.toInt()} (TID: ${dep.transactionId}) was rejected. Reason: $reason. Contact support for help.",
                type = "SMS"
            )
        )
        return Result.success(Unit)
    }

    suspend fun adminApproveWithdrawal(withdrawalId: String): Result<Unit> {
        val wth = withdrawalDao.getWithdrawalById(withdrawalId) ?: return Result.failure(Exception("Withdrawal not found"))
        withdrawalDao.updateWithdrawalStatus(withdrawalId, "APPROVED", null)

        notificationDao.insertNotification(
            AppNotification(
                userId = wth.userId,
                title = "SMS: Withdrawal DISBURSED",
                message = "Dear ${wth.userName}, payout of Rs. ${wth.amount.toInt()} sent to ${wth.method} (${wth.accountNumber}) - ${wth.accountTitle}.",
                type = "SMS"
            )
        )
        return Result.success(Unit)
    }

    suspend fun adminRejectWithdrawal(withdrawalId: String, reason: String): Result<Unit> {
        val wth = withdrawalDao.getWithdrawalById(withdrawalId) ?: return Result.failure(Exception("Withdrawal not found"))
        withdrawalDao.updateWithdrawalStatus(withdrawalId, "REJECTED", reason)
        // Refund amount back to user's balance
        userDao.addBalance(wth.userId, wth.amount)

        notificationDao.insertNotification(
            AppNotification(
                userId = wth.userId,
                title = "SMS: Withdrawal REJECTED (Refunded)",
                message = "Withdrawal of Rs. ${wth.amount.toInt()} rejected. Reason: $reason. Rs. ${wth.amount.toInt()} refunded to your account balance.",
                type = "SMS"
            )
        )
        return Result.success(Unit)
    }

    suspend fun adminAddVideoTask(title: String, channel: String, reward: Double, category: String) {
        videoDao.insertVideo(
            VideoTask(
                title = title,
                channelName = channel,
                durationSeconds = 15,
                rewardAmount = reward,
                category = category
            )
        )
    }

    suspend fun adminDeleteVideo(id: Long) {
        videoDao.deleteVideo(id)
    }

    fun updateJazzCashNumber(newNumber: String) {
        jazzCashDepositNumber.value = newNumber
    }
}

package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val passwordHash: String,
    val securityPin: String = "1234",
    val accountBalance: Double = 0.0,
    val totalEarned: Double = 0.0,
    val totalWithdrawn: Double = 0.0,
    val isPlanActive: Boolean = false,
    val activePlanName: String = "None",
    val planActivatedAt: Long = 0L,
    val videosWatchedToday: Int = 0,
    val lastWatchDate: String = "",
    val referralCode: String,
    val referredByCode: String? = null,
    val isVerified: Boolean = true,
    val role: String = "USER" // "USER" or "ADMIN"
)

@Entity(tableName = "deposits")
data class Deposit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val depositId: String,
    val userId: String,
    val userName: String,
    val method: String = "JazzCash",
    val depositNumber: String = "03242158084",
    val amount: Double,
    val senderMobile: String,
    val transactionId: String,
    val screenshotUri: String? = null,
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val rejectionReason: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "withdrawals")
data class Withdrawal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val withdrawalId: String,
    val userId: String,
    val userName: String,
    val method: String = "EasyPaisa", // EasyPaisa or JazzCash
    val accountNumber: String,
    val accountTitle: String,
    val amount: Double,
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val rejectionReason: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "video_tasks")
data class VideoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val channelName: String,
    val durationSeconds: Int = 15,
    val rewardAmount: Double = 15.0,
    val category: String = "Finance",
    val thumbnailKey: String = "chart"
)

@Entity(tableName = "referrals")
data class Referral(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val referrerUserId: String,
    val referredName: String,
    val referredPhone: String,
    val bonusAmount: Double = 75.0,
    val status: String = "QUALIFIED", // QUALIFIED, PENDING_PLAN
    val dateFormatted: String
)

@Entity(tableName = "app_notifications")
data class AppNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "SMS", // SMS, DEPOSIT, WITHDRAWAL, REWARD
    val isRead: Boolean = false
)

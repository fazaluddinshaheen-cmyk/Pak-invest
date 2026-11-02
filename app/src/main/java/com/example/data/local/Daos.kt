package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppNotification
import com.example.data.model.Deposit
import com.example.data.model.Referral
import com.example.data.model.User
import com.example.data.model.VideoTask
import com.example.data.model.Withdrawal
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): User?

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserByUserId(userId: String): User?

    @Query("SELECT * FROM users WHERE role = 'USER'")
    fun getAllUsersFlow(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserFlow(userId: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET accountBalance = accountBalance + :amount, totalEarned = totalEarned + :amount WHERE userId = :userId")
    suspend fun addBalance(userId: String, amount: Double)

    @Query("UPDATE users SET accountBalance = accountBalance - :amount WHERE userId = :userId")
    suspend fun deductBalance(userId: String, amount: Double)

    @Query("UPDATE users SET isPlanActive = 1, activePlanName = :planName, planActivatedAt = :activatedAt WHERE userId = :userId")
    suspend fun activatePlan(userId: String, planName: String, activatedAt: Long)

    @Query("UPDATE users SET videosWatchedToday = :count, lastWatchDate = :date WHERE userId = :userId")
    suspend fun updateVideoWatchCount(userId: String, count: Int, date: String)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposits ORDER BY createdAt DESC")
    fun getAllDepositsFlow(): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE userId = :userId ORDER BY createdAt DESC")
    fun getDepositsForUser(userId: String): Flow<List<Deposit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeposit(deposit: Deposit): Long

    @Query("UPDATE deposits SET status = :status, rejectionReason = :reason WHERE depositId = :depositId")
    suspend fun updateDepositStatus(depositId: String, status: String, reason: String? = null)

    @Query("SELECT * FROM deposits WHERE depositId = :depositId LIMIT 1")
    suspend fun getDepositById(depositId: String): Deposit?
}

@Dao
interface WithdrawalDao {
    @Query("SELECT * FROM withdrawals ORDER BY createdAt DESC")
    fun getAllWithdrawalsFlow(): Flow<List<Withdrawal>>

    @Query("SELECT * FROM withdrawals WHERE userId = :userId ORDER BY createdAt DESC")
    fun getWithdrawalsForUser(userId: String): Flow<List<Withdrawal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: Withdrawal): Long

    @Query("UPDATE withdrawals SET status = :status, rejectionReason = :reason WHERE withdrawalId = :withdrawalId")
    suspend fun updateWithdrawalStatus(withdrawalId: String, status: String, reason: String? = null)

    @Query("SELECT * FROM withdrawals WHERE withdrawalId = :withdrawalId LIMIT 1")
    suspend fun getWithdrawalById(withdrawalId: String): Withdrawal?
}

@Dao
interface VideoDao {
    @Query("SELECT * FROM video_tasks")
    fun getAllVideosFlow(): Flow<List<VideoTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoTask>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoTask): Long

    @Query("DELETE FROM video_tasks WHERE id = :id")
    suspend fun deleteVideo(id: Long)
}

@Dao
interface ReferralDao {
    @Query("SELECT * FROM referrals WHERE referrerUserId = :userId ORDER BY id DESC")
    fun getReferralsForUser(userId: String): Flow<List<Referral>>

    @Query("SELECT * FROM referrals ORDER BY id DESC")
    fun getAllReferrals(): Flow<List<Referral>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: Referral): Long
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM app_notifications WHERE userId = :userId OR userId = 'ALL' ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<AppNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification): Long

    @Query("UPDATE app_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)
}

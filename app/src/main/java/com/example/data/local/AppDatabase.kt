package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AppNotification
import com.example.data.model.Deposit
import com.example.data.model.Referral
import com.example.data.model.User
import com.example.data.model.VideoTask
import com.example.data.model.Withdrawal

@Database(
    entities = [
        User::class,
        Deposit::class,
        Withdrawal::class,
        VideoTask::class,
        Referral::class,
        AppNotification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun depositDao(): DepositDao
    abstract fun withdrawalDao(): WithdrawalDao
    abstract fun videoDao(): VideoDao
    abstract fun referralDao(): ReferralDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pak_investment.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

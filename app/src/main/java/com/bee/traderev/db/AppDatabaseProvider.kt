package com.bee.traderev.db

import android.arch.persistence.room.Room
import com.bee.traderev.utils.TradeRevApplication

private const val DB_NAME = "TradeRev"
object AppDatabaseProvider {
    val db = Room.databaseBuilder(TradeRevApplication.application!!,
            AppDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
}
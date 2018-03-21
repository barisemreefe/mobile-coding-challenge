package com.bee.traderev.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.bee.traderev.datatypes.Photo

@Database(entities = [(Photo::class)], version = 1)
@TypeConverters(com.bee.traderev.db.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
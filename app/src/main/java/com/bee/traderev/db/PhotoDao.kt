package com.bee.traderev.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bee.traderev.datatypes.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE pageLocation=:arg0")//there is a problem with kotlin if I use someting other than arg0 it gives error
    fun getPhotos(arg0: Int = 1): List<Photo>

    @Query("SELECT * FROM photos")
    fun getAll(): List<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photos: List<Photo>)

}
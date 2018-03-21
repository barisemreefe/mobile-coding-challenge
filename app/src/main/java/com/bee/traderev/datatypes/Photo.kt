package com.bee.traderev.datatypes

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

//Photo Pojo and PhotoEntity for db shares same class
//In complex applications. these can be separated
//Side effect of this trade off is declaring all variables as var(required by room)
@Entity(tableName = "photos")
data class Photo(@PrimaryKey var id: String="",
                 var pageLocation : Int = 1,
                 var color: String?=null,
                 var likes: Int?=null,
                 @TypeConverters(com.bee.traderev.db.TypeConverters::class)var user: User?=null,
                 @TypeConverters(com.bee.traderev.db.TypeConverters::class)var urls: Urls?=null)
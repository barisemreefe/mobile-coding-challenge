package com.bee.traderev.db

import android.arch.persistence.room.TypeConverter
import com.bee.traderev.datatypes.Urls
import com.bee.traderev.datatypes.User
import com.bee.traderev.db.MoshiProvider.moshi
import java.io.IOException

class TypeConverters{
    @TypeConverter
    fun stringToUser(data: String?): User? {
        var user: User? = null
        if (data!=null) {
            try {
                user = moshi.adapter(User::class.java).fromJson(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return user
    }

    @TypeConverter
    fun userToString(user: User?): String {
        return moshi.adapter(User::class.java).toJson(user)
    }
    @TypeConverter
    fun stringToUrls(data: String?): Urls? {
        var urls: Urls? = null
        if (data!=null) {
            try {
                urls = moshi.adapter(Urls::class.java).fromJson(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return urls
    }

    @TypeConverter
    fun userToString(urls: Urls?): String {
        return moshi.adapter(Urls::class.java).toJson(urls)
    }
}
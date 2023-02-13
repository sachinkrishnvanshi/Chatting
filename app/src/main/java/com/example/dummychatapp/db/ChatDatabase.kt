package com.example.dummychatapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.db.dao.ChatDao

@Database(entities = [ChatData::class], version = 2)
abstract class ChatDatabase : RoomDatabase(){
    abstract fun chatDao(): ChatDao
    companion object {
        @Volatile
        private var INSTANCE : ChatDatabase?= null

        fun getDatabase(context: Context) : ChatDatabase {
            if (INSTANCE == null) {
                synchronized(this) {}
                INSTANCE = Room.databaseBuilder(
                    context,
                    ChatDatabase::class.java, "userDB1"
                )
                    .build()
            }
            return INSTANCE!!
        }
    }
}
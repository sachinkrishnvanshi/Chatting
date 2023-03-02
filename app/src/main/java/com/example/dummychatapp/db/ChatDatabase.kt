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

}
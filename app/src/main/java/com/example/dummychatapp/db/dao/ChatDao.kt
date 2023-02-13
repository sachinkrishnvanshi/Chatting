package com.example.dummychatapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dummychatapp.db.data.ChatData

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ChatData)

    @Query("select * from chat  ")
    fun getAll(): List<ChatData>
}
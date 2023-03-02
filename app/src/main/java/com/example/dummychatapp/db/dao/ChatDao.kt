package com.example.dummychatapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dummychatapp.db.data.ChatData
import java.util.concurrent.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ChatData)

    @Query("select * from chat  ")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<ChatData>>
}
package com.example.dummychatapp.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatData (
    @PrimaryKey(autoGenerate =true)
    val id: Long ?= null,
    val message: String,
    val type: Int? = null,
    val chatTime: Long = System.currentTimeMillis()

    )
package com.example.dummychatapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatData (
    @PrimaryKey(autoGenerate =true)
    val id: Long ?= null,
    val message: String,
)
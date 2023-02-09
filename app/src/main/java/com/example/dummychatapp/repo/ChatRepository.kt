package com.example.dummychatapp.repo

import com.example.dummychatapp.MainApplication
import com.example.dummychatapp.chatDao.ChatDatabase
import com.example.dummychatapp.data.ChatData

class ChatRepository {
    private val chatDatabase by lazy {
        ChatDatabase
    }
    fun addChat(chat: ChatData)
    {
        chatDatabase.getDatabase(MainApplication.instance).chatDao().insertChat(chat)
    }
    fun getChat():List<ChatData>
    {
        return chatDatabase.getDatabase(MainApplication.instance).chatDao().getAll()
    }
}
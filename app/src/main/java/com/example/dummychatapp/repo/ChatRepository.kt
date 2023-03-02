package com.example.dummychatapp.repo


import com.example.dummychatapp.db.dao.ChatDao
import com.example.dummychatapp.db.data.ChatData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatDao:ChatDao) {

    fun addChat(chat: ChatData) {
        chatDao.insertChat(chat)
    }

    fun getChat():Flow<List<ChatData>> {
        return chatDao.getAll()
    }
}
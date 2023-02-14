package com.example.dummychatapp.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.repo.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel:ViewModel() {
    var message= MutableLiveData<String>()
    var messageList= MutableLiveData<List<ChatData>>()
    var messages:LiveData<List<ChatData>> =messageList
    val mainHandler = Handler(Looper.getMainLooper())



    private val chatRepo by lazy {
        ChatRepository()
    }

    fun addMessage() {
        val data = ChatData(null, message.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            chatRepo.addChat(data)
        }
    }
        fun getMsg(){
            viewModelScope.launch(Dispatchers.IO) {
               messageList.postValue(chatRepo.getChat())
            }
        }

    fun receiveMsg(){
        mainHandler.post(object : Runnable {
            override fun run() {
                val data = ChatData(null,"hello..how can i help you?",1)
                addBotMsg(data)
                mainHandler.postDelayed(this, 30000)
            }
        })
    }
    fun addBotMsg( data: ChatData){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepo.addChat(data)
        }
    }

}
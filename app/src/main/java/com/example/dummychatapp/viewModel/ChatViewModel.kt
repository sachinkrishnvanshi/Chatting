package com.example.dummychatapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummychatapp.data.ChatData
import com.example.dummychatapp.repo.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatViewModel:ViewModel() {
    var message= MutableLiveData<String>()
    var messageList= MutableLiveData<List<ChatData>>()
    var messages:LiveData<List<ChatData>> =messageList
    private val chatRepo by lazy {
        ChatRepository()
    }

    fun addMessage() {
        val data = ChatData(null, message.value.toString())
        Log.d("addMessage:", message.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            chatRepo.addChat(data)
        }
    }
        fun getMessage1(){
           GlobalScope.launch(Dispatchers.IO) {
               messageList.postValue(chatRepo.getChat())
                Log.d("huuu",  chatRepo.getChat().toString())
            }

        }

}
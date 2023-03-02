package com.example.dummychatapp.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.repo.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel@Inject constructor(private val chatRepo:ChatRepository) :ViewModel() {
    var message= MutableLiveData<String>()
    var messageList= MutableLiveData<List<ChatData>>()
    var messages:LiveData<List<ChatData>> =messageList


    init {
        chatRepo.getChat().onEach{
            messageList.value = it
        }.launchIn(viewModelScope)
    }


    fun addMessage() {
        val data = ChatData(null, message.value.toString(), 0)
        viewModelScope.launch(Dispatchers.IO) {
            chatRepo.addChat(data)
        }
    }
        fun getMsg(){
            chatRepo.getChat().onEach{
                messageList.value = it
            }.launchIn(viewModelScope)
        }

    fun addBotMsg( data: ChatData){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepo.addChat(data)
        }
    }

}
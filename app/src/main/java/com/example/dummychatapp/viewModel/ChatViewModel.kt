package com.example.dummychatapp.viewModel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummychatapp.data.ChatData
import com.example.dummychatapp.data.ChatReceiveData
import com.example.dummychatapp.repo.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatViewModel:ViewModel() {
    var message= MutableLiveData<String>()
    var messageList= MutableLiveData<List<ChatData>>()
    var messages:LiveData<List<ChatData>> =messageList

    var botMessageList= MutableLiveData<List<ChatReceiveData>>()
    var botMessages:LiveData<List<ChatReceiveData>> =botMessageList

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
        fun getMsg(){
            Log.d("send message1",  "send")

            viewModelScope.launch(Dispatchers.IO) {
               messageList.postValue(chatRepo.getChat())
                Log.d("send message2",  chatRepo.getChat().toString())
            }
        }

       fun receiveMsg(){
           val dataReceive=ChatReceiveData(
               id=null,
               message="hello @all",
//               type = "bot"
           )
           Handler(Looper.getMainLooper()).postDelayed({
               botMessageList.postValue(listOf(dataReceive))
               Log.e("delay message","hello")
           }, 16000)
       }


}
package com.example.dummychatapp.ui

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dummychatapp.R
import com.example.dummychatapp.adapter.MessageList
import com.example.dummychatapp.data.ChatData
import com.example.dummychatapp.data.ChatReceiveData
import com.example.dummychatapp.databinding.ActivityMainBinding
import com.example.dummychatapp.viewModel.ChatViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var chatViewModel: ChatViewModel
    private  var chatData : List<ChatData>?=null
    private var chatDataReceive : List<ChatReceiveData>?=null
    private  lateinit var rvAdapter:MessageList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        mBinding.chatViewModel = chatViewModel
        mBinding.lifecycleOwner = this
        setUpViews()
        setStatusBarColor()
//        chatViewModel.addMessage()
        Log.d("send message1",  "send")

       // chatViewModel.receiveMsg()
    }

    private fun setStatusBarColor() {
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.window.statusBarColor =
            ContextCompat.getColor(this, R.color.light_turquoise)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.window.decorView.windowInsetsController!!
                .setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
        }
        WindowCompat.setDecorFitsSystemWindows(this.window, true)
    }



    private fun setUpObserver() {

        chatViewModel.messages.observe(this, Observer {
             it
//            val messageList = chatDataReceive?.let { it1 -> MessageList(chatData!!, it1,1) }
//            mBinding.rvChat.adapter = messageList
            rvAdapter=MessageList(it,1)
            mBinding.rvChat.adapter=rvAdapter
         //   messageList!!.notifyDataSetChanged()
        })

//        chatViewModel.botMessages.observe(this, Observer {
//            chatDataReceive=it
//            val botMsgList= chatDataReceive?.let { it1 -> chatData?.let { it2 -> MessageList(it2,it,0) } }
//            mBinding.rvChat.adapter=botMsgList
//          //  botMsgList!!.notifyDataSetChanged()
//        })
    }




    private fun setUpViews() {
        supportActionBar?.hide()
        mBinding.etTypeMsg.onFocusChangeListener =
            View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                if (hasFocus) {
                    mBinding.ivSendEnable.visibility = View.VISIBLE

                } else {
                    mBinding.ivSend.visibility = View.GONE
                }
            }
        mBinding.ivSend.setOnClickListener {
        }

    }

    override fun onResume() {
        super.onResume()
        chatViewModel.getMsg()
        setUpObserver()

    }
}



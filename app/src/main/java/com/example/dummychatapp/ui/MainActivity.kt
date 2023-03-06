package com.example.dummychatapp.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dummychatapp.R
import com.example.dummychatapp.adapter.MessageListAdapter
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.databinding.ActivityMainBinding
import com.example.dummychatapp.viewModel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var chatViewModel: ChatViewModel
    private var chatData: List<ChatData>? = null
    private lateinit var rvAdapter: MessageListAdapter
    val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setStatusBarColor()
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        mBinding.chatViewModel = chatViewModel
        mBinding.lifecycleOwner = this
        setUpViews()
        receiveMsg()
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

    private fun receiveMsg() {
        mainHandler.post(object : Runnable {
            override fun run() {
                val data = ChatData(null, "hello..how can i help you?", 1)
                chatViewModel.addBotMsg(data)
                mainHandler.postDelayed(this, 30000)
            }
        })
    }

    private fun setUpViews() {
        supportActionBar?.hide()
        mBinding.etTypeMsg.onFocusChangeListener =
            View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                if (hasFocus) {
                    mBinding.ivSendEnable.visibility = View.VISIBLE
                    mBinding.ivSend.visibility = View.GONE
                } else {
                    mBinding.ivSend.visibility = View.VISIBLE
                    mBinding.ivSendEnable.visibility = View.GONE

                }
            }
        mBinding.ivSendEnable.setOnClickListener {
            chatViewModel.addMessage()
            mBinding.etTypeMsg.text.clear()
            mBinding.etTypeMsg.clearFocus()
        }
    }

    override fun onResume() {
        super.onResume()
        chatViewModel.getMsg()
        setUpObserver()
    }

    private fun setUpObserver() {
        chatViewModel.messages.observe(this) {
            chatData = it
            rvAdapter = MessageListAdapter()
            rvAdapter.differ.submitList(it)
            mBinding.rvChat.adapter = rvAdapter
            mBinding.rvChat.scrollToPosition(rvAdapter.itemCount - 1)
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacksAndMessages(null)
    }
}



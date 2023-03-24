package com.example.dummychatapp.ui

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dummychatapp.location.LocationUtility
import com.example.dummychatapp.location.PermissionUtil
import com.example.dummychatapp.R
import com.example.dummychatapp.SharedPreferenceManager
import com.example.dummychatapp.adapter.MessageListAdapter
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.databinding.ActivityMainBinding
import com.example.dummychatapp.viewModel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var chatViewModel: ChatViewModel
    private var chatData: List<ChatData>? = null
    private lateinit var permissionUtil: PermissionUtil
    private lateinit var locationUtility: LocationUtility
    val mainHandler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var rvAdapter: MessageListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        locationUtility = LocationUtility(this)
        lifecycle.addObserver(locationUtility)
        setStatusBarColor()
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        mBinding.chatViewModel = chatViewModel
        mBinding.lifecycleOwner = this
        setUpViews()
        receiveMsg()
        getLocationDetail()
        observeLocation()
        getUserCountry(this)
    }
    private fun getLocationDetail(){
        permissionUtil = PermissionUtil(this)
        permissionUtil.launch(
            permissions = arrayOfLocationPermission(),
            onGranted = {
                if(!locationUtility.isGpsEnable())
                    locationUtility.startLocationClient()
                        },
            onDenied = { },
            onShouldShowRationale = { }
        )
    }
    private fun getUserCountry(context: Context): String? {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) {
                // SIM country code is available
                return simCountry.lowercase(Locale.getDefault())
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) {
                    // network country code is available
                    return networkCountry.lowercase(Locale.getDefault())
                }
            }
        } catch (e: Exception) {
        }
        return null
    }
    private fun observeLocation(){
            locationUtility.currentLocation.observe( this,Observer {
                val lat = it.first
                val long = it.second
                Log.d("currentLocation","${it?.first} & ${it?.second}")
                SharedPreferenceManager.put("latitude",it?.first.toString())
                SharedPreferenceManager.put("longitude",it?.second.toString())
                Log.d("shared",SharedPreferenceManager.getString("latitude"))
            })
//        for one time location requests.

           /* locationUtility.getOneTime().collect{
                Log.d("currentLocation","${it?.first} & ${it?.second}")
                SharedPreferenceManager.put("latitude",it?.first.toString())
                SharedPreferenceManager.put("longitude",it?.second.toString())
                Log.d("shared",SharedPreferenceManager.getString("latitude"))
            }*/

    }


    private fun arrayOfLocationPermission(): Array<String> {
        return arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
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
            rvAdapter.differ.submitList(it)
            mBinding.rvChat.adapter = rvAdapter
            mBinding.rvChat.scrollToPosition(rvAdapter.itemCount - 1)
        }
    }

    override fun onStop() {
        super.onStop()
        mainHandler.removeCallbacksAndMessages(null)

    }
}



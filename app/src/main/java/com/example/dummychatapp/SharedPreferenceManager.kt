package com.example.dummychatapp

import android.content.SharedPreferences
import android.preference.PreferenceManager


object SharedPreferenceManager {
    private var mInstance: SharedPreferences? = null
    val editor = getInstance().edit()

    private fun getInstance(): SharedPreferences {
        if (mInstance == null)
            mInstance =
                PreferenceManager.getDefaultSharedPreferences(MainApplication.instance)
        return mInstance!!
    }

    fun put(key: String, value: String) {
        editor.putString(key, value)
//        editor.putString(key, value.encrypt())
        editor.apply()
    }
    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
//        editor.putString(key, value.encrypt())
        editor.apply()
    }
    fun getString(key: String): String {
        return getInstance().getString(key, "")!!
    }
    fun getBoolean(key: String): Boolean {
        return getInstance().getBoolean(key, false)!!
    }
    fun clearAllPrefs() {

        editor.clear()
        editor.apply()
    }
}
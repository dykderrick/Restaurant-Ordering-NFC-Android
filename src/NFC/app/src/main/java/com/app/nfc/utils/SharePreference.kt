package com.app.nfc.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharePreference {

    companion object {

        lateinit var mContext: Context
        lateinit var sharedPreferences: SharedPreferences
        val PREF_NAME: String = "DrabaApp"
        val PRIVATE_MODE: Int = 0
        lateinit var editor: SharedPreferences.Editor

        val isLogin:String="isLogin"
        val loginToken:String="logintoken"
        val userId:String="userid"
        val isJoin:String="isJoin"

        fun getIntPref(context: Context,key:String):Int{
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            return pref.getInt(key,-1)
        }

        fun setIntPref(context: Context,key: String,value:Int){
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref.edit().putInt(key,value).apply()
        }

        fun getStringPref(context: Context,key:String): String? {
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            return pref.getString(key,"")
        }

        fun setStringPref(context: Context,key: String,value:String){
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref.edit().putString(key,value).apply()
        }

        fun getBooleanPref(context: Context,key:String): Boolean {
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            return pref.getBoolean(key,false)
        }

        fun setBooleanPref(context: Context,key: String,value:Boolean){
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref.edit().putBoolean(key,value).apply()
        }

    }

    @SuppressLint("CommitPrefEdits")
    constructor(mContext1: Context) {
        mContext = mContext1
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor= sharedPreferences.edit()
    }
    fun mLogout(){
        editor.clear()
        editor.commit()
    }

}
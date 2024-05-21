package com.eagletech.resize.dataUser

import android.content.Context
import android.content.SharedPreferences
class MangerSharedPreferences constructor(context: Context) {
    private val sharedPreferences: SharedPreferences


    init {
        sharedPreferences = context.getSharedPreferences("ManagerPref", Context.MODE_PRIVATE)
    }

    companion object {
        @Volatile
        private var instance: MangerSharedPreferences? = null

        fun getInstance(context: Context): MangerSharedPreferences {
            return instance ?: synchronized(this) {
                instance ?: MangerSharedPreferences(context).also { instance = it }
            }
        }
    }


    fun getBuy(): Int {
        return sharedPreferences.getInt("times", 0)
    }

    fun setBuy(lives: Int) {
        sharedPreferences.edit().putInt("times", lives).apply()
    }

    fun addBuy(buy: Int) {
        val currentBuy = getBuy()
        setBuy(currentBuy + buy)
    }

    fun removeBuy() {
        val currentBuy = getBuy()
        if (currentBuy > 0) {
            setBuy(currentBuy - 1)
        }
    }


    // Lấy thông tin mua premium
    var isPremiumBuy: Boolean?
        get() {
            val userId = sharedPreferences.getString("UserId", "")
            return sharedPreferences.getBoolean("PremiumPlan_\$userId$userId", false)
        }
        set(state) {
            val userId = sharedPreferences.getString("UserId", "")
            sharedPreferences.edit().putBoolean("PremiumPlan_\$userId$userId", state!!).apply()

        }

    // Lưu thông tin người dùng
    fun currentUserIdBuy(userid: String?) {
        sharedPreferences.edit().putString("UserId", userid).apply()
    }

    // Lấy ra thông tin id người dùng
    fun getCurrentUserIdBuy(): String? {
        return sharedPreferences.getString("UserId", null)
    }

}
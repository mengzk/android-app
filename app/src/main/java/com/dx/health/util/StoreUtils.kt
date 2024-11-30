package com.dx.health.util

import android.content.Context
import android.content.SharedPreferences
import com.dx.health.App
import com.google.gson.Gson

/**
 * Author: Meng
 * Date: 2023/06/28
 * Desc:
 */
class StoreUtils {

    companion object {
        private const val ACCOUNT = "app_account"
        private const val TOKEN = "app_token"
        private const val USERID = "app_user_id"

        private const val WAREHOUSE = ""

        private var preference: SharedPreferences? = null

        fun getIns(): SharedPreferences {
            if (preference == null) {
                preference = App.getContext()
                    .getSharedPreferences("QL_Health", Context.MODE_PRIVATE)
            }
            return preference!!
        }

        fun setAccount(account: String?) {
            if (account != null) {
                val edit = getIns().edit()
                val gson = Gson()
                edit.putString(ACCOUNT, gson.toJson(account)).apply()
            }
        }

        fun getAccount(): String? {
            val account = getIns().getString(ACCOUNT, "")
            return if (account != null) {
//                val gson = Gson()
                ""
            } else {
                null
            }
        }
        fun clearAccount() {
            getIns().edit().clear().apply()
        }
    }
}
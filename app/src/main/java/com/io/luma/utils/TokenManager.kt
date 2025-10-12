package com.io.luma.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_TOKEN_FILE = "prefs_token_file"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"

        // Singleton instance
        @Volatile
        private var INSTANCE: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokenManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

    }

    fun saveTokens(accessToken: String, refreshToken: String?) {
        prefs.edit().apply {
            putString(ACCESS_TOKEN, accessToken)
            refreshToken?.let { putString(REFRESH_TOKEN, it) }
            apply()
        }
    }

    fun saveId(userId:String)
    {
        prefs.edit().apply {
            putString("userid", userId)
            apply()
        }

    }
    fun getId(): String?
    {
        return prefs.getString("userid", null)
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }


}

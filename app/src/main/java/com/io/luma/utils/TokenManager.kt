package com.io.luma.utils

import android.content.Context
import android.content.SharedPreferences


class TokenManager private constructor(context: Context) {

    private val prefs =
        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_TOKEN_FILE = "prefs_token_file"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "userid"

        @Volatile
        private var INSTANCE: TokenManager? = null

        /**
         * Option 1: Initialize once in Application class
         */
        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = TokenManager(context.applicationContext)
                    }
                }
            }
        }

        /**
         * Option 2: Get instance directly (auto-create if not initialized)
         */
        fun getInstance(context: Context? = null): TokenManager {
            return if (INSTANCE != null) {
                INSTANCE!!
            } else {
                if (context == null) {
                    throw IllegalStateException(
                        "TokenManager not initialized. Call init(context) or pass context to getInstance(context)"
                    )
                }
                synchronized(this) {
                    INSTANCE ?: TokenManager(context.applicationContext).also {
                        INSTANCE = it
                    }
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

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun saveId(userId: String) {
        prefs.edit().apply {
            putString(USER_ID, userId)
            apply()
        }
    }

    fun getId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}



//class TokenManager private constructor(context: Context) {
//
//    private val prefs: SharedPreferences =
//        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)
//
//
//    fun init(context: Context) {
//        if (INSTANCE == null) {
//            synchronized(this) {
//                if (INSTANCE == null) {
//                    INSTANCE = TokenManager(context.applicationContext)
//                }
//            }
//        }
//    }
//
//    companion object {
//        private const val PREFS_TOKEN_FILE = "prefs_token_file"
//        private const val ACCESS_TOKEN = "access_token"
//        private const val REFRESH_TOKEN = "refresh_token"
//
//        // Singleton instance
//        @Volatile
//        private var INSTANCE: TokenManager? = null
//
//        fun getInstance(context: Context): TokenManager {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: TokenManager(context.applicationContext).also {
//                    INSTANCE = it
//                }
//            }
//        }
//
//    }
//
//
//    fun saveTokens(accessToken: String, refreshToken: String?) {
//        prefs.edit().apply {
//            putString(ACCESS_TOKEN, accessToken)
//            refreshToken?.let { putString(REFRESH_TOKEN, it) }
//            apply()
//        }
//    }
//
//
//
//
//    fun saveId(userId:String)
//    {
//        prefs.edit().apply {
//            putString("userid", userId)
//            apply()
//        }
//
//    }
//    fun getId(): String?
//    {
//        return prefs.getString("userid", null)
//    }
//
//    fun getAccessToken(): String? {
//        return prefs.getString(ACCESS_TOKEN, null)
//    }
//
//    fun getRefreshToken(): String? {
//        return prefs.getString(REFRESH_TOKEN, null)
//    }
//
//    fun clear() {
//        prefs.edit().clear().apply()
//    }
//
//
//}

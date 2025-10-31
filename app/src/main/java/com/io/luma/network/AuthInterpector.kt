package com.io.luma.network


import android.util.Log
import com.io.luma.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(

) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


                 Log.d("Token","Bearer ${TokenManager.getInstance().getAccessToken().toString()}")
        val newRequest = if (TokenManager.getInstance().getAccessToken()!=null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${TokenManager.getInstance().getAccessToken()}")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}

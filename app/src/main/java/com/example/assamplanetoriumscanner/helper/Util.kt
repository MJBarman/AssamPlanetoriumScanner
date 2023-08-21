package com.example.assamplanetoriumscanner.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.assamplanetoriumscanner.ui.LoginScreen
import com.example.assamplanetoriumscanner.model.User
import com.google.gson.Gson

class Util {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    fun getJwtToken(json: String?): String {
        val user: User = Gson().fromJson(json, User::class.java)
        return user.token!!
    }

    fun sessionCheck(context: Context) {

        sharedPreferences = context.getSharedPreferences("ASZCounter", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val alert = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        alert.titleText = "Error"
        alert.contentText = "Session Expired"
        alert.confirmText = "Log out"
        alert.setConfirmClickListener {
            editor.clear()
            editor.apply()
            context.startActivity(
                Intent(
                    context, LoginScreen::class.java
                )
            )
        }
        alert.show()
    }
}
package com.example.assamplanetoriumscanner.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.BuildConfig
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.databinding.ActivityProfileScreenBinding
import com.example.ticketscanner.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ProfileScreen : AppCompatActivity() {
    private lateinit var binding: ActivityProfileScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var user: User
    private lateinit var userString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Profile"

        sharedPreferences = this@ProfileScreen.getSharedPreferences("ASZCounter", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        userString = sharedPreferences.getString("user", "").toString()
        if (userString.isNotEmpty()) {
            user = Gson().fromJson(
                userString,
                object : TypeToken<User>() {}.type
            )
            binding.phone.text = user.mobile_no
        }

        //get application version code and version name to display on UI

        //get application version code and version name to display on UI
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val buildNumber: String = BuildConfig.VERSION_NAME
            val versionName: String = java.lang.String.valueOf(BuildConfig.VERSION_CODE)
            binding.appVersion.text = versionName
            binding.buildVersion.text = buildNumber
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        binding.topAppBar.setNavigationOnClickListener { v -> onBackPressed() }

        binding.btnSettingsLogOut.setOnClickListener {
            showLogoutBottomSheet()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showLogoutBottomSheet() {
        val logoutBottomSheet = BottomSheetDialog(this@ProfileScreen)
        logoutBottomSheet.setContentView(R.layout.bottom_sheet_exit)
        val title = logoutBottomSheet.findViewById<TextView>(R.id.title)
        val header = logoutBottomSheet.findViewById<TextView>(R.id.header)
        val success = logoutBottomSheet.findViewById<Button>(R.id.success)
        val cancel = logoutBottomSheet.findViewById<Button>(R.id.cancel)
        title?.text = "Are you sure you want to logout?"
        header?.text = "LOGOUT?"
        success?.text = "YES"
        cancel?.text = "CANCEL"
        logoutBottomSheet.show()

        success?.setOnClickListener {
            editor.clear().apply()
            startActivity(Intent(this@ProfileScreen, LoginScreen::class.java))
        }
        cancel?.setOnClickListener { logoutBottomSheet.dismiss() }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        startActivity(Intent(this@ProfileScreen, MainScreen::class.java))
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
}
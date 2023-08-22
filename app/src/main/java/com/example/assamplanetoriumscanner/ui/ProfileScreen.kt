package com.example.assamplanetoriumscanner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.BuildConfig
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.databinding.ActivityProfileScreenBinding
import com.example.assamplanetoriumscanner.model.User
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
            binding.name.text = user.name
            binding.roleId.text = user.role_id.toString()
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

        binding.btnLogOut.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_exit, null)
            dialog.setContentView(view)

            val btnLogout = view.findViewById<Button>(R.id.btn_logOut)
            val btnCancel = view.findViewById<Button>(R.id.cancel)

            btnLogout.setOnClickListener {
                editor.clear().apply()
                startActivity(Intent(this@ProfileScreen, LoginScreen::class.java))
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        startActivity(Intent(this@ProfileScreen, MainScreen::class.java))
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
}
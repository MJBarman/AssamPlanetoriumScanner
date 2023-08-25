package com.example.assamplanetoriumscanner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.databinding.ActivityMainBinding
import com.example.assamplanetoriumscanner.helper.NotificationsHelper
import com.example.assamplanetoriumscanner.helper.ResponseHelper
import com.example.assamplanetoriumscanner.helper.Util
import com.example.assamplanetoriumscanner.network.Client
import com.example.assamplanetoriumscanner.network.RetrofitHelper
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(DelicateCoroutinesApi::class)
class MainScreen : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        var fabVisible = false
        sharedPreferences = this.getSharedPreferences("ASZCounter", MODE_PRIVATE)
        editor = sharedPreferences.edit()



        binding.topAppBar.setOnMenuItemClickListener { item ->
            if (item.itemId === R.id.profile) {
                startActivity(Intent(this, ProfileScreen::class.java))
                return@setOnMenuItemClickListener true
            }
            false
        }


        binding.dailyScannedCv.setOnClickListener {
            val intent = Intent(this@MainScreen, DailyScreen::class.java)
            startActivity(intent)
        }


        binding.overallScannedCv.setOnClickListener {
            val intent = Intent(this@MainScreen, OverallScreen::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


        binding.fabAdd.setOnClickListener {
            if (!fabVisible) {

                binding.fabBooking.show()
                binding.fabScanner.show()
                binding.fabBooking.visibility = View.VISIBLE
                binding.fabScanner.visibility = View.VISIBLE


                binding.fabAdd.setImageDrawable(resources.getDrawable(R.drawable.close))
                fabVisible = true
            } else {
                binding.fabBooking.hide()
                binding.fabScanner.hide()
                binding.fabBooking.visibility = View.GONE
                binding.fabScanner.visibility = View.GONE

                binding.fabAdd.setImageDrawable(resources.getDrawable(R.drawable.add))
                fabVisible = false
            }
        }
        binding.fabBooking.setOnClickListener {
            // on below line we are displaying a toast message.
            val bottomSheetFragment = BookingFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.fabScanner.setOnClickListener {
            val intent = Intent(this@MainScreen, ScannerScreen::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        getScannedDetails()
        getOverallScannedDetails()


    }


    private fun getScannedDetails() {
        val api = RetrofitHelper.getInstance().create(Client::class.java)
        GlobalScope.launch {
            val call: Call<JsonObject> = api.getDailyScanData(
                Util().getJwtToken(
                    sharedPreferences.getString("user", "").toString()
                ), 1
            )
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val helper = ResponseHelper()
                        helper.ResponseHelper(response.body())
                        if (helper.isStatusSuccessful()) {
                            val obj = JSONObject(helper.getDataAsString())
                            val ticketListArray = obj.getJSONObject("dailyScanData").getJSONArray("data")
                            editor.putString("ticketList", ticketListArray.toString())
                            editor.apply()
                            val totalDailyTickets = obj.getInt("dailyCount")
                            binding.tvDailyScannedCount.text = totalDailyTickets.toString()
                        } else {
                            if (helper.getErrorMsg() == "440") {
                                Util().sessionCheck(this@MainScreen)
                            }
                        }
                    } else {
                        NotificationsHelper().getErrorAlert(
                            this@MainScreen, "Response Error Code" + response.message()
                        )
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    NotificationsHelper().getErrorAlert(this@MainScreen, "Server Error")
                }
            })
        }
    }


    private fun getOverallScannedDetails() {
        val api = RetrofitHelper.getInstance().create(Client::class.java)
        GlobalScope.launch {
            val call: Call<JsonObject> = api.getOverallScanData(
                Util().getJwtToken(
                    sharedPreferences.getString("user", "").toString()
                ), 1
            )
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val helper = ResponseHelper()
                        helper.ResponseHelper(response.body())
                        if (helper.isStatusSuccessful()) {
                            val obj = JSONObject(helper.getDataAsString())
                            Log.d("TAG", "onResponse: $obj")
                            val ticketListArray = obj.getJSONObject("overallScanData").getJSONArray("data")
                            val overallCount = obj.getInt("overallCount")
                            editor.putString("overallTicketList", ticketListArray.toString())
                            editor.apply()
                            binding.tvOverallScanCount.text = overallCount.toString()
                        }
                    } else {
                        NotificationsHelper().getErrorAlert(
                            this@MainScreen, "Response Error Code" + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    NotificationsHelper().getErrorAlert(this@MainScreen, "Server Error")
                }
            })
        }
    }


    private fun showErrorMessage(message: String) {
        NotificationsHelper().getErrorAlert(this@MainScreen, message)
    }

}



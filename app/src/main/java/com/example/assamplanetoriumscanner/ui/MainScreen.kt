package com.example.assamplanetoriumscanner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi


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

        }

//        getScannedDetails()
//        getOverallScannedDetails()

    }

//    private fun getOverallScannedDetails() {
//        val api = RetrofitHelper.getInstance().create(Api.Client::class.java)
//        GlobalScope.launch {
//            val call: Call<JsonObject> = api.getOverallScanData(
//                Util().getJwtToken(
//                    sharedPreferences.getString("user", "").toString()
//                ), 1
//            )
//            call.enqueue(object : Callback<JsonObject> {
//                override fun onResponse(
//                    call: Call<JsonObject>, response: Response<JsonObject>
//                ) {
//                    if (response.isSuccessful) {
//                        val helper = ResponseHelper()
//                        helper.ResponseHelper(response.body())
//                        if (helper.isStatusSuccessful()) {
//                            val obj = JSONObject(helper.getDataAsString())
//                            val ticketListArray = obj.get("data") as JSONArray
//                            val totalOverallTickets = obj.get("total") as Int
//                            editor.putString("overallTicketList", ticketListArray.toString())
//                            editor.apply()
//                            binding.tvOverallScanCount.text = totalOverallTickets.toString()
//                        }
//                    } else {
//                        NotificationsHelper().getErrorAlert(
//                            this@MainScreen, "Response Error Code" + response.message()
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                    NotificationsHelper().getErrorAlert(this@MainScreen, "Server Error")
//                }
//            })
//        }
//    }


//    private fun getScannedDetails() {
//        val api = RetrofitHelper.getInstance().create(Api.Client::class.java)
//        GlobalScope.launch {
//            val call: Call<JsonObject> = api.getDailyScanData(
//                Util().getJwtToken(
//                    sharedPreferences.getString("user", "").toString()
//                ), 1
//            )
//            call.enqueue(object : Callback<JsonObject> {
//                override fun onResponse(
//                    call: Call<JsonObject>, response: Response<JsonObject>
//                ) {
//                    if (response.isSuccessful) {
//                        val helper = ResponseHelper()
//                        helper.ResponseHelper(response.body())
//                        if (helper.isStatusSuccessful()) {
//                            val obj = JSONObject(helper.getDataAsString())
//                            val ticketListArray = obj.get("data") as JSONArray
//                            val totalDailyTickets = obj.get("total") as Int
//                            editor.putString("ticketList", ticketListArray.toString())
//                            editor.apply()
//                            binding.tvDailyScannedCount.text = totalDailyTickets.toString()
//                        } else {
//                            if (helper.getErrorMsg() == "440") {
//                                Util().sessionCheck(this@MainScreen)
//                            }
//                        }
//                    } else {
//                        NotificationsHelper().getErrorAlert(
//                            this@MainScreen, "Response Error Code" + response.message()
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                    NotificationsHelper().getErrorAlert(this@MainScreen, "Server Error")
//                }
//            })
//        }
//    }


    override fun onBackPressed() {
        if (!shouldAllowBack()) {
            return
        }
        super.onBackPressed()
    }

    private fun shouldAllowBack(): Boolean {
        return false
    }
}

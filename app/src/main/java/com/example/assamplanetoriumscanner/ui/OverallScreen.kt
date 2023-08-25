package com.example.assamplanetoriumscanner.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assamplanetoriumscanner.Adapters.OverallScannedAdapter
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.databinding.ActivityOverallScreenBinding
import com.example.assamplanetoriumscanner.helper.NotificationsHelper
import com.example.assamplanetoriumscanner.helper.ResponseHelper
import com.example.assamplanetoriumscanner.helper.Util
import com.example.assamplanetoriumscanner.model.ScannedTicket
import com.example.assamplanetoriumscanner.network.Client
import com.example.assamplanetoriumscanner.network.RetrofitHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@DelicateCoroutinesApi
class OverallScreen : AppCompatActivity() {
    private lateinit var binding: ActivityOverallScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dailyScannedList: ArrayList<ScannedTicket>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OverallScannedAdapter
    private lateinit var scannedTicketList: ArrayList<ScannedTicket>
    private var currentPage: Int = 2
    private var listSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverallScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Overall Scanned Tickets"

        //date
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        binding.date.text = formattedDate


        sharedPreferences = this@OverallScreen.getSharedPreferences(
            "ASZCounter", MODE_PRIVATE
        )
        val ticketListString = sharedPreferences.getString("overallTicketList", "").toString()
        scannedTicketList = Gson().fromJson(
            ticketListString, object : TypeToken<ArrayList<ScannedTicket>>() {}.type
        )
        listSize = scannedTicketList.size
        adapter = OverallScannedAdapter(scannedTicketList)
        recyclerView = binding.ticketListRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false

        binding.viewMore.setOnClickListener {
            getScannedDetails(currentPage)
        }
    }

    private fun getScannedDetails(count: Int) {
        val api = RetrofitHelper.getInstance().create(Client::class.java)
        GlobalScope.launch {
            val call: Call<JsonObject> = api.getOverallScanData(
                Util().getJwtToken(
                    sharedPreferences.getString("user", "").toString()
                ), currentPage
            )
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val helper = ResponseHelper()
                        helper.ResponseHelper(response.body())
                        if (helper.isStatusSuccessful()) {
                            currentPage += 1
                            val obj = JSONObject(helper.getDataAsString())
                            val ticketListArray = obj.get("data") as JSONArray
                            val addedScannedTicketList: ArrayList<ScannedTicket> = Gson().fromJson(
                                ticketListArray.toString(),
                                object : TypeToken<ArrayList<ScannedTicket>>() {}.type
                            )
                            if (addedScannedTicketList.isEmpty()) {
                                Log.d("data", "nothing to add")
                            } else {
                                Log.d("data", "found")
                            }
                            scannedTicketList.addAll(addedScannedTicketList)
                            adapter.notifyDataSetChanged()
                            recyclerView.layoutManager = LinearLayoutManager(this@OverallScreen)
                            recyclerView.isNestedScrollingEnabled = false
                        } else {
                            if (helper.getErrorMsg() == "440") {
                                Util().sessionCheck(this@OverallScreen)
                            }
                        }
                    } else {
                        NotificationsHelper().getErrorAlert(
                            this@OverallScreen, "Response Error Code" + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    NotificationsHelper().getErrorAlert(this@OverallScreen, "Server Error")
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }
}
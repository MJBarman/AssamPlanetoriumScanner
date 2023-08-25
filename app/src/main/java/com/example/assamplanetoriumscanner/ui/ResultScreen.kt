package com.example.assamplanetoriumscanner.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.assamplanetoriumscanner.databinding.ActivityResultScreenBinding
import com.example.assamplanetoriumscanner.helper.ResponseHelper
import com.example.assamplanetoriumscanner.helper.Util
import com.example.assamplanetoriumscanner.network.Client
import com.example.assamplanetoriumscanner.network.RetrofitHelper
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultScreen : AppCompatActivity() {
    private var TAG: String = "TAG"
    private lateinit var sharedPreferences: SharedPreferences
    private var responseData: String = ""
    private var bookingNo: String = ""
    private lateinit var binding: ActivityResultScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        sharedPreferences = this.getSharedPreferences("ASZCounter", MODE_PRIVATE)



        binding.cancelBtn.setOnClickListener {
            val intent = Intent(applicationContext, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        binding.confirmBtn.setOnClickListener {
            updateBookingDataToServer(bookingNo)
        }

        binding.topAppBar.setOnClickListener {
            val intent = Intent(applicationContext, MainScreen::class.java)
            startActivity(intent)
            finish()
        }


        // Retrieve the response data from the intent extras
        val responseData = intent.getStringExtra("response_data")
        if (responseData != null) {
            // Parse the response data as a JSON object
            val jsonObject = JSONObject(responseData)
            // Extract the required fields

            bookingNo = jsonObject.getString("booking_no")
            val mobileNumber = jsonObject.getString("mobile_no")
            val visitorName = jsonObject.getString("visitor_name")
            val visitingDate = jsonObject.getString("visiting_date")
            val noOfVisitors = jsonObject.getString("t_person")
            val general = jsonObject.getString("general")
            val student = jsonObject.getString("student")
            val showTime = jsonObject.getString("show_time")
            val netAmount = jsonObject.getString("net_amt")
            val serviceCharge = jsonObject.getString("service_amt")
            val gstAmount = jsonObject.getString("gst_amt")
            val totalAmount = jsonObject.getString("total_amt")


            //Set the extracted fields on the respective TextViews
            binding.bookingNo.text = bookingNo
            binding.phoneNumber.text = mobileNumber
            binding.visitorName.text = visitorName.takeIf { it != "null" } ?: ""
            binding.visitingDate.text = visitingDate
            binding.tPerson.text = noOfVisitors
            binding.general.text = general
            binding.student.text = student
            binding.showTime.text = showTime
            binding.totalNetAmount.text = "₹ $netAmount"
            binding.totalServiceCharge.text = "₹ $serviceCharge"
            binding.totalGst.text = "₹ $gstAmount"
            binding.totalPrice.text = "₹ $totalAmount"
        }
    }


    private fun updateBookingDataToServer(bookingNumber: String?) {
        val api = RetrofitHelper.getInstance().create(Client::class.java)

        api.updateBookingDataToServer(
            Util().getJwtToken(
                sharedPreferences.getString("user", "").toString()
            ), bookingNo
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    // Handle successful response from the server
                    val helper = ResponseHelper()
                    helper.ResponseHelper(response.body())
                    if (helper.isStatusSuccessful()) {
                        val obj = JSONObject(helper.getDataAsString())
                        val status = obj.get("status") as Int
                        Log.d("status: ", status.toString())
                        // Create an intent to start the ResultScreen activity and pass the response data
                        val intent = Intent(this@ResultScreen, EndScreenSuccess::class.java)
                        startActivity(intent)
                    } else {
                        showAlertDialog(this@ResultScreen, "Alert", helper.getErrorMsg())
                    }
                } else {
                    showAlertDialog(this@ResultScreen, "Alert", response.message())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showAlertDialog(this@ResultScreen, "Alert", "Server Error!")
            }
        })
    }


    fun showAlertDialog(context: Context, title: String, message: String) {
        val sweetAlertDialog =
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(title)
                .setContentText(message)
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmButton("OK") {
            startActivity(Intent(this@ResultScreen, MainScreen::class.java))
            sweetAlertDialog.dismiss()
        }
        sweetAlertDialog.show()
    }
}
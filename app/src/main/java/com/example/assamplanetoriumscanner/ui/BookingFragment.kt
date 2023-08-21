package com.example.assamplanetoriumscanner.ui

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.assamplanetoriumscanner.databinding.FragmentBookingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookingFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var scannedValue: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("ASZCounter", MODE_PRIVATE)
        binding.btnSubmit.setOnClickListener {
            if (binding.bookingNumber.text.toString().isEmpty()) {
                showAlertDialog(requireActivity(), "Alert", "Please enter booking number!")
            } else {
                scannedValue = binding.bookingNumber.text.toString().uppercase()
//                sendBookingDataToServer(scannedValue)
            }
        }
        return binding.root
    }

//    private fun sendBookingDataToServer(bookingNumber: String?) {
//        val api = RetrofitHelper.getInstance().create(Client::class.java)
//
//        api.sendBookingDataToServer(
//            Util().getJwtToken(
//                sharedPreferences.getString("user", "").toString()
//            ), scannedValue
//        ).enqueue(object : Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                if (response.isSuccessful) {
//                    // Handle successful response from the server
//                    val helper = ResponseHelper()
//                    helper.ResponseHelper(response.body())
//                    if (helper.isStatusSuccessful()) {
//                        val obj = JSONObject(helper.getDataAsString())
//                        val status = obj.get("status") as Int
//                        Log.d("status: ", status.toString())
//                        // Create an intent to start the ResultScreen activity and pass the response data
////                        val intent = Intent(requireActivity(), ResultScreen::class.java)
////                        intent.putExtra("response_data", helper.getDataAsString())
////                        startActivity(intent)
//                    } else {
//                        showAlertDialog(requireActivity(), "Alert", helper.getErrorMsg())
//                    }
//                } else {
//                    showAlertDialog(requireActivity(), "Alert", response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                showAlertDialog(requireActivity(), "Alert", "Server Error!")
//            }
//        })
//    }

    private fun sendResultValue(result: String) {
        val bundle = Bundle()
//        val intent = Intent(requireActivity(), ResultScreen::class.java)
//        bundle.putString("result", result)
//        intent.putExtras(bundle)
//        startActivity(intent)
    }

    fun showAlertDialog(context: Context, title: String, message: String) {
        val sweetAlertDialog =
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(title)
                .setContentText(message)
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmButton("OK") {
            sweetAlertDialog.dismiss()
        }
        sweetAlertDialog.show()
    }
}

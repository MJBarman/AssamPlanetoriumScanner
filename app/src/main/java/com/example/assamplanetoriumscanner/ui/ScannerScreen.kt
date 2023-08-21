package com.example.assamplanetoriumscanner.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtron.zooticket.helper.ResponseHelper
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.databinding.ActivityScannerScreenBinding
import com.example.assamplanetoriumscanner.helper.Util
import com.example.assamplanetoriumscanner.network.Client
import com.example.assamplanetoriumscanner.network.RetrofitHelper
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ScannerScreen : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: ActivityScannerScreenBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        sharedPreferences = this.getSharedPreferences("ASZCounter", MODE_PRIVATE)

        binding.cancelScan.setOnClickListener {
            onBackPressed()
        }

        if (ContextCompat.checkSelfPermission(
                this@ScannerScreen, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@ScannerScreen, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)

    }


    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()

        cameraSource =
            CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920, 720)
                .setAutoFocusEnabled(true) //you should add this feature
                .build()



        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {


            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int, width: Int, height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Log.d("Response", "Value: $scannedValue")

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
//                    binding.scanningProgressBar.visibility = View.VISIBLE
                    scannedValue = barcodes.valueAt(0).rawValue
                    barcodeDetector.release()

                    if (scannedValue.isNotEmpty()) {
                        sendBookingDataToServer(scannedValue)
                    }

                    Log.d("RESPONSE: ", "VALUE: $scannedValue")
                } else {
                    Log.d("RESPONSE", "VALUE: ELSE")
                }
            }
        })
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@ScannerScreen,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(this@ScannerScreen, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sendBookingDataToServer(bookingNumber: String?) {
        val api = RetrofitHelper.getInstance().create(Client::class.java)

        api.sendBookingDataToServer(
            Util().getJwtToken(
                sharedPreferences.getString("user", "").toString()
            ), scannedValue
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
                        val intent = Intent(this@ScannerScreen, ResultScreen::class.java)
                        intent.putExtra("response_data", helper.getDataAsString())
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    } else {
                        showAlertDialog(this@ScannerScreen, "Alert", helper.getErrorMsg())
                    }
                } else {
                    showAlertDialog(this@ScannerScreen, "Alert", response.message())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showAlertDialog(this@ScannerScreen, "Alert", "Server Error!")
            }
        })
    }


    private fun sendResultValue(result: String) {
        val bundle = Bundle()
        val intent = Intent(this, ResultScreen::class.java)
        bundle.putString("result", result)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun stopCamera() {
        cameraSource.stop()
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

    fun showAlertDialog(context: Context, title: String, message: String) {
        val sweetAlertDialog =
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(title)
                .setContentText(message)
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.setConfirmButton("OK") {
            startActivity(Intent(this@ScannerScreen, MainScreen::class.java))
            sweetAlertDialog.dismiss()
        }
        sweetAlertDialog.show()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
    }


}
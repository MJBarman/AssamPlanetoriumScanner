package com.amtron.zooticket.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {
    @SuppressLint("SimpleDateFormat")
    fun changeTimeFormat(time: String?): String {
        val tk = StringTokenizer(time)
        val time = tk.nextToken()

        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date? = sdf.parse(time)
        return sdfs.format(dt!!)
    }

    fun getTimeDifference(endTime: String, startTime: String): String {
        val startTimeDate = SimpleDateFormat("hh:mm a").parse(startTime)
        val endTimeDate = SimpleDateFormat("hh:mm a").parse(endTime)

        val timeDiff = endTimeDate!!.time - startTimeDate!!.time
        val hours = timeDiff / (60 * 60 * 1000)
        val mins = timeDiff / (60 * 1000)

        val result = if (hours == 0L) {
            "$mins mins"
        } else if (mins == 0L) {
            "$hours hrs"
        } else {
            "$hours hrs, $mins mins"
        }
        return result
    }
}
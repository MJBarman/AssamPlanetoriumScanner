package com.example.assamplanetoriumscanner.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assamplanetoriumscanner.R
import com.example.assamplanetoriumscanner.model.ScannedTicket
import com.example.ticketscanner.UI.Adapters.OnRecyclerViewItemClickListener

import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class OverallScannedAdapter(
    private val scannedList: List<ScannedTicket>
) : RecyclerView.Adapter<OverallScannedAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: OnRecyclerViewItemClickListener
    private lateinit var ticket: ScannedTicket

    fun setOnItemClickListener(mItemClickListener: OnRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.scanned_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ticket = scannedList[position]

        holder.mobileNumber.text = ticket.mobile_no
        holder.bookingNumber.text = ticket.booking_no
        holder.price.text = ticket.total_amt.toString()


    }



    override fun getItemCount(): Int {
        return scannedList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mobileNumber: TextView = itemView.findViewById(R.id.tv_mobile_number)
        val bookingNumber: TextView = itemView.findViewById(R.id.tv_booking_no)
        val price: TextView = itemView.findViewById(R.id.tv_total_amount)
        val numberVisitors: Button = itemView.findViewById(R.id.total_visitors)
        val numberCameras: Button = itemView.findViewById(R.id.total_camera)
    }
}
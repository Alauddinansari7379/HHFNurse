package com.example.hhfoundation.IPCMS.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.IPCMS.model.ModelReferralsFollow
import com.example.hhfoundation.IPCMS.model.ModelReferralsHis
import com.example.hhfoundation.IPCMS.model.Prescription
import com.example.hhfoundation.sharedpreferences.SessionManager

class AdapterReferralDocHIs(val context: Context, private val list: ArrayList<Prescription>,) :
    RecyclerView.Adapter<AdapterReferralDocHIs.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_referrals_doc_his, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sessionManager=SessionManager(context)

        //  holder.SrNo.text= "${position+1}"
        holder.dateREF.text = list[position].p_date
        holder.cDateREF.text = list[position].cdate
        holder.preiDREF.text = list[position].p_id
        holder.healthIssueREF.text = list[position].p_hissue
//        holder.doctorNameRef.text = list[position].doctorname
//        holder.referralsDateREF.text = list[position].referfollowd
         holder.departmentREF.text = list[position].p_depart
       // holder.patientNameRef.text = list[position].name
        holder.hospitalNameREF.text = list[position].p_hosp

         //        holder.patientNameRef.text = list[position].patientname
//        holder.hospitalNameRef.text = list[position].refer_hospital
//        holder.doctorNameRef.text = list[position].doctrname
        // holder.statusRef.text = list[position].sugar

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

//        holder.btnInfoRef.setOnClickListener {
////            val intent = Intent(context as Activity, StudentDetailsOne::class.java)
////            studentId=list[position].id
////             context.startActivity(intent)
//        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateREF: TextView = itemView.findViewById(R.id.date)
        val preiDREF: TextView = itemView.findViewById(R.id.preiD)
        val doctorNameRef: TextView = itemView.findViewById(R.id.doctorName)
        val patientNameRef: TextView = itemView.findViewById(R.id.patientName)
        val hospitalNameREF: TextView = itemView.findViewById(R.id.hospitalName)
        val cDateREF: TextView = itemView.findViewById(R.id.cDate)
         val healthIssueREF: TextView = itemView.findViewById(R.id.healthIssue)
        val departmentREF: TextView = itemView.findViewById(R.id.department)


    }
}
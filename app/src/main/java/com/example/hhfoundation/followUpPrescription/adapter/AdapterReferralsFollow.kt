package com.example.hhfoundation.followUpPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.followUpPrescription.model.ModelReferralsFollow

class AdapterReferralsFollow(val context: Context, private val list: ModelReferralsFollow,) :
    RecyclerView.Adapter<AdapterReferralsFollow.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_referrals_follow_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        holder.dateREF.text = list.prescriptions[position].p_date
        holder.cDateREF.text = list.prescriptions[position].cdate
        holder.preiDREF.text = list.prescriptions[position].p_id
        holder.healthIssueREF.text = list.prescriptions[position].p_hissue
        holder.doctorNameRef.text = list.prescriptions[position].doctorname
        holder.referralsDateREF.text = list.prescriptions[position].referfollowd
        holder.statusRef.text = list.prescriptions[position].status
        holder.departmentREF.text = list.prescriptions[position].p_depart
        holder.patientNameRef.text = list.patients[position].name
        holder.hospitalNameREF.text = list.prescriptions[position].p_hosp
         holder.exteriorDateREF.text = list.doctors[position].name
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
        return list.prescriptions.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateREF: TextView = itemView.findViewById(R.id.dateREF)
        val preiDREF: TextView = itemView.findViewById(R.id.preiDREF)
        val doctorNameRef: TextView = itemView.findViewById(R.id.doctorNameREF)
        val patientNameRef: TextView = itemView.findViewById(R.id.patientNameREF)
        val hospitalNameREF: TextView = itemView.findViewById(R.id.hospitalNameREF)
        val cDateREF: TextView = itemView.findViewById(R.id.cDateREF)
        val statusRef: TextView = itemView.findViewById(R.id.statusREF)
        val healthIssueREF: TextView = itemView.findViewById(R.id.healthIssueREF)
        val departmentREF: TextView = itemView.findViewById(R.id.departmentREF)
        val exteriorDateREF: TextView = itemView.findViewById(R.id.exteriorDateREF)
        val referralsDateREF: TextView = itemView.findViewById(R.id.referralsDateREF)

    }
}
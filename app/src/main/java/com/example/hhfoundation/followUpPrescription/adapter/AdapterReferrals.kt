package com.example.hhfoundation.followUpPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.followUpPrescription.model.Prescriptiondetail

class AdapterReferrals(val context: Context, val list: List<Prescriptiondetail>) :
    RecyclerView.Adapter<AdapterReferrals.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_referrals_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.idRef.text = list[position].patientid
        holder.dateRef.text = list[position].date
        holder.patientNameRef.text = list[position].patientname
        holder.hospitalNameRef.text = list[position].refer_hospital
        holder.doctorNameRef.text = list[position].doctrname
       // holder.statusRef.text = list[position].sugar

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

        holder.btnInfoRef.setOnClickListener {
//            val intent = Intent(context as Activity, StudentDetailsOne::class.java)
//            studentId=list[position].id
//             context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
    var studentId=""
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idRef: TextView = itemView.findViewById(R.id.idRef)
        val hospitalNameRef: TextView = itemView.findViewById(R.id.hospitalNameRef)
        val doctorNameRef: TextView = itemView.findViewById(R.id.doctorNameRef)
        val patientNameRef: TextView = itemView.findViewById(R.id.patientNameRef)
        val dateRef: TextView = itemView.findViewById(R.id.dateRef)
        val statusRef: TextView = itemView.findViewById(R.id.statusRef)
        val btnInfoRef: Button = itemView.findViewById(R.id.btnInfoRef)

    }
}
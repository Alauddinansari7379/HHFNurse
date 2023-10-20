package com.example.hhfoundation.clinicalManagement.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.labReport.activity.ViewLabReport
import com.example.hhfoundation.labReport.model.Prescriptiondetail

class AdapterPrescription(val context: Context, val list: List<Prescriptiondetail>) :
    RecyclerView.Adapter<AdapterPrescription.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_priscription_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.preId.text = list[position].pid
        holder.date.text = list[position].follow_date
        holder.patientName.text = list[position].patientname
        holder.patientIdPre.text = list[position].patientid
        holder.doctorPre.text = list[position].doctrname

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

        holder.btnViewPre.setOnClickListener {
            val intent = Intent(context as Activity, ViewLabReport::class.java)
                .putExtra("pid", list[position].pid)
                .putExtra("prescription", "1")
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val preId: TextView = itemView.findViewById(R.id.preIdPre)
        val date: TextView = itemView.findViewById(R.id.datePre)
        val patientName: TextView = itemView.findViewById(R.id.patientPre)
        val patientIdPre: TextView = itemView.findViewById(R.id.patirntIdPre)
        val doctorPre: TextView = itemView.findViewById(R.id.doctorPre)
        val btnViewPre: Button = itemView.findViewById(R.id.btnViewPre)

    }
}
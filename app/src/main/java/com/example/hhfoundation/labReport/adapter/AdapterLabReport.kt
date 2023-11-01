package com.example.hhfoundation.labReport.adapter

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
import com.example.hhfoundation.labReport.model.Labreport

class AdapterLabReport(val context: Context, val list: List<Labreport>) :
    RecyclerView.Adapter<AdapterLabReport.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_lab_report_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.prescriptionIdLAB.text = list[position].prescription_id
        holder.dateLAB.text = list[position].created_at.subSequence(0,10)
        holder.patientNameLAB.text = list[position].name
        holder.patientIdLAB.text = list[position].patient_id
        holder.descriptionLAB.text = list[position].description
       // holder.doctorPre.text = list[position].doctrname

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

        holder.btnViewPre.setOnClickListener {
            val intent = Intent(context as Activity, ViewLabReport::class.java)
                .putExtra("image", list[position].img_url)
                .putExtra("prescription_id", list[position].prescription_id)
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prescriptionIdLAB: TextView = itemView.findViewById(R.id.prescriptionIdLAB)
        val patientNameLAB: TextView = itemView.findViewById(R.id.patientNameLAB)
        val patientIdLAB: TextView = itemView.findViewById(R.id.patientIdLAB)
        val descriptionLAB: TextView = itemView.findViewById(R.id.descriptionLAB)
        val dateLAB: TextView = itemView.findViewById(R.id.dateLAB)
        val btnViewPre: Button = itemView.findViewById(R.id.btnViewLAB)

    }
}
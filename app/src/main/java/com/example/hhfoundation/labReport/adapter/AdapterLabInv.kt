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
import com.example.hhfoundation.labReport.activity.UploadReport
import com.example.hhfoundation.labReport.activity.ViewLabReport
import com.example.hhfoundation.labReport.model.Labadvidetail
import com.example.hhfoundation.labReport.model.Labreport

class AdapterLabInv(val context: Context, val list: List<Labadvidetail>) :
    RecyclerView.Adapter<AdapterLabInv.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_lab_inv_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.idLABINV.text = list[position].id
        holder.dateLABINV.text = list[position].date
        holder.patientNameLABINV.text = list[position].patient
        holder.doctorLABINV.text = list[position].doctor
        holder.statusLABINV.text = list[position].status
        holder.reportNameLABINV.text = list[position].rname
       // holder.doctorPre.text = list[position].doctrname

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

        holder.btnUploadLABINV.setOnClickListener {
            val intent = Intent(context as Activity, UploadReport::class.java)
                .putExtra("id", list[position].id)
                .putExtra("patientid", list[position].patientid)
                .putExtra("prescription_id", list[position].prescription_id)
                .putExtra("rname", list[position].rname)
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idLABINV: TextView = itemView.findViewById(R.id.idLABINV)
        val dateLABINV: TextView = itemView.findViewById(R.id.dateLABINV)
        val reportNameLABINV: TextView = itemView.findViewById(R.id.ReportNameLABINV)
        val patientNameLABINV: TextView = itemView.findViewById(R.id.patientNameLABINV)
        val doctorLABINV: TextView = itemView.findViewById(R.id.doctorLABINV)
        val statusLABINV: TextView = itemView.findViewById(R.id.statusLABINV)
        val btnUploadLABINV: Button = itemView.findViewById(R.id.btnUploadLABINV)

    }

}
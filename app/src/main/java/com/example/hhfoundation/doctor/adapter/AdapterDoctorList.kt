package com.example.hhfoundation.doctor.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.doctor.model.Doctor
import com.example.hhfoundation.medicalHistory.activity.ViewMedicalHis
import com.example.hhfoundation.medicalHistory.model.Medicalhistory
import com.squareup.picasso.Picasso

class AdapterDoctorList(val context: Context, val list: List<Doctor>) :
    RecyclerView.Adapter<AdapterDoctorList.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_doctor_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.doctorIdSP.text = list[position].id
        holder.regNO.text = list[position].reg_no
        holder.studentNameSP.text = list[position].name
        holder.emailSp.text = list[position].email
        holder.phomeSP.text = list[position].phone
        holder.departmentSP.text = list[position].department
        holder.profileSP.text = list[position].profile


//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewML)
//
//        holder.btnCaseML.setOnClickListener {
//            val intent = Intent(context as Activity, ViewMedicalHis::class.java)
//                .putExtra("patient_id",list[position].patient_id)
//                .putExtra("birthdate",list[position].birthdate)
//            context.startActivity(intent)
//        }

    }


    override fun getItemCount(): Int {
        return list.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorIdSP: TextView = itemView.findViewById(R.id.doctorIdSP)
        val regNO: TextView = itemView.findViewById(R.id.regNO)
        val studentNameSP: TextView = itemView.findViewById(R.id.studentNameSP)
        val emailSp: TextView = itemView.findViewById(R.id.emailSp)
        val phomeSP: TextView = itemView.findViewById(R.id.phomeSP)
        val departmentSP: TextView = itemView.findViewById(R.id.DepartmentSP)
        val profileSP: TextView = itemView.findViewById(R.id.profileSP)
     }
}
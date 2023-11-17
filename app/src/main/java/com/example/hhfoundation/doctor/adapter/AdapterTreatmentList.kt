package com.example.hhfoundation.doctor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.doctor.model.Appointment

class AdapterTreatmentList(val context: Context, val list: List<Appointment>) :
    RecyclerView.Adapter<AdapterTreatmentList.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_treatment_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.doctorID.text = list[position].doctor_id
        holder.doctor.text = list[position].doctor_name
        holder.numberOfPatient.text = list[position].appointment_total



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
        val doctorID: TextView = itemView.findViewById(R.id.DoctorID)
        val doctor: TextView = itemView.findViewById(R.id.Doctor)
        val numberOfPatient: TextView = itemView.findViewById(R.id.NumberOfPatient)

     }
}
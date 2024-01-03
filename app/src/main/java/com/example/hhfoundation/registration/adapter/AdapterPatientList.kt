package com.example.hhfoundation.registration.adapter

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
import com.bumptech.glide.Glide
import com.example.hhfoundation.R
import com.example.hhfoundation.registration.model.Patient
import com.example.hhfoundation.sharedpreferences.SessionManager
import com.example.hhfoundation.studenDetails.StudentDetails
import com.example.hhfoundation.studenDetails.StudentDetailsOne
import com.squareup.picasso.Picasso

class AdapterPatientList(val context: Context, val list: List<Patient>) :
    RecyclerView.Adapter<AdapterPatientList.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_patient_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {
            sessionManager = SessionManager(context)

            //  holder.SrNo.text= "${position+1}"
            //  holder.refrencecode.text= list[position].referenceCode
            holder.schoolIdPL.text = list[position].hospital_id
            holder.schoolNamePL.text = list[position].schl
            holder.distPL.text = list[position].schl_dist
            holder.studentIdPL.text = list[position].id
            holder.studentNamePL.text = list[position].name
            holder.genderPL.text = list[position].sex
            holder.dobPL.text = list[position].birthdate
            holder.bloodGPl.text = list[position].bloodgroup

            Glide
                .with(context)
                .load("https://schoolhms.thedemostore.in/" + list[position].img_url)
                .error(R.drawable.error_placeholder)
                .placeholder(R.drawable.placeholder_n)
                .into(holder.imageViewPL)

//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

            if (sessionManager.group == "Pharmacist") {
                holder.btnAdMedicALHPL.visibility = View.GONE
            }
            holder.btnAdMedicALHPL.setOnClickListener {
                val intent = Intent(context as Activity, StudentDetailsOne::class.java)
                intent.putExtra("birthdate", list[position].birthdate)
                studentId = list[position].id
                context.startActivity(intent)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        var studentId = ""
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schoolIdPL: TextView = itemView.findViewById(R.id.schoolIdPL)
        val schoolNamePL: TextView = itemView.findViewById(R.id.schoolNamePL)
        val distPL: TextView = itemView.findViewById(R.id.distPL)
        val studentIdPL: TextView = itemView.findViewById(R.id.studentIdPL)
        val studentNamePL: TextView = itemView.findViewById(R.id.studentNamePL)
        val genderPL: TextView = itemView.findViewById(R.id.genderPL)
        val dobPL: TextView = itemView.findViewById(R.id.dobPL)
        val bloodGPl: TextView = itemView.findViewById(R.id.bloodGPl)
        val imageViewPL: ImageView = itemView.findViewById(R.id.imageViewPL)
        val btnAdMedicALHPL: Button = itemView.findViewById(R.id.btnAdMedicALHPL)
    }
}
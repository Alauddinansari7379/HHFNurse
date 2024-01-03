package com.example.hhfoundation.medicalHistory.adapter

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
import com.example.hhfoundation.medicalHistory.activity.ViewMedicalHis
import com.example.hhfoundation.medicalHistory.model.Medicalhistory
import com.squareup.picasso.Picasso

class AdapterMedicalHis(val context: Context, val list: List<Medicalhistory>) :
    RecyclerView.Adapter<AdapterMedicalHis.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_medical_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {

            //  holder.SrNo.text= "${position+1}"
            //  holder.refrencecode.text= list[position].referenceCode
            holder.schoolIdML.text = list[position].hospital_id
            holder.studentIdML.text = list[position].patient_id
            holder.studentNameML.text = list[position].patient_name
            holder.genderML.text = list[position].sex
            holder.dobML.text = list[position].birthdate
            holder.bloodGML.text = list[position].bloodgroup

            // if (list[position].img_url!=null) {
            Glide
                .with(context)
                .load("https://schoolhms.thedemostore.in/" + list[position].img_url)
                .error(R.drawable.error_placeholder)
                .placeholder(R.drawable.placeholder_n)
                .into(holder.imageViewML)
            // }

//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewML)

            holder.btnCaseML.setOnClickListener {
                val intent = Intent(context as Activity, ViewMedicalHis::class.java)
                    .putExtra("patient_id", list[position].patient_id)
                    .putExtra("birthdate", list[position].birthdate)
                context.startActivity(intent)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schoolIdML: TextView = itemView.findViewById(R.id.schoolIdML)
        val studentIdML: TextView = itemView.findViewById(R.id.studentIdML)
        val studentNameML: TextView = itemView.findViewById(R.id.studentNameML)
        val genderML: TextView = itemView.findViewById(R.id.genderML)
        val dobML: TextView = itemView.findViewById(R.id.dobML)
        val bloodGML: TextView = itemView.findViewById(R.id.bloodGML)
        val btnCaseML: Button = itemView.findViewById(R.id.btnCaseML)
        val imageViewML: ImageView = itemView.findViewById(R.id.imageViewML)
    }
}
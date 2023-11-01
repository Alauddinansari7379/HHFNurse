package com.example.hhfoundation.followUpPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.followUpPrescription.model.Vitalddetail

class AdapterDailyVital(val context: Context, val list: List<Vitalddetail>) :
    RecyclerView.Adapter<AdapterDailyVital.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_vital_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.patientIdV.text = list[position].pid
        holder.pRV.text = list[position].pr
        holder.temperatureV.text = list[position].temp
        holder.saturation.text = list[position].spo
        holder.sugarV.text = list[position].sugar
        holder.bloodPressureV.text = list[position].bp
        holder.dateV.text = list[position].date
        holder.patientV.text = list[position].patientname

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

//        holder.btnRequestAppFL.setOnClickListener {
////            val intent = Intent(context as Activity, StudentDetailsOne::class.java)
////            studentId=list[position].id
////             context.startActivity(intent)
//        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientIdV: TextView = itemView.findViewById(R.id.patientIdV)
        val patientV: TextView = itemView.findViewById(R.id.patientV)
         val pRV: TextView = itemView.findViewById(R.id.pRV)
        val temperatureV: TextView = itemView.findViewById(R.id.TemperatureV)
        val saturation: TextView = itemView.findViewById(R.id.Saturation)
        val sugarV: TextView = itemView.findViewById(R.id.SugarV)
        val bloodPressureV: TextView = itemView.findViewById(R.id.BloodPressureV)
        val dateV: TextView = itemView.findViewById(R.id.dateV)

    }
}
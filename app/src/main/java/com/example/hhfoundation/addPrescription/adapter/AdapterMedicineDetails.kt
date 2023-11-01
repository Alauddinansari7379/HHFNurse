package com.example.hhfoundation.addPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
 import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.model.ModelMedicineDetails

class AdapterMedicineDetails (val context:Context, val list: List<ModelMedicineDetails>):
    RecyclerView.Adapter<AdapterMedicineDetails.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_medicine,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.medicineName.text= list[position].medicineName
        holder.medicineTiming.text= list[position].dosage
        holder.frequency.text= list[position].frequency
        holder.duration.text= list[position].days
        holder.tvInstraction.text= list[position].instraction

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val medicineName:TextView=itemView.findViewById(R.id.tvMedicineName)
        val medicineTiming:TextView=itemView.findViewById(R.id.tvMedicinetiming)
        val frequency:TextView=itemView.findViewById(R.id.Frequency)
        val duration:TextView=itemView.findViewById(R.id.tvDuration)
        val tvInstraction:TextView=itemView.findViewById(R.id.tvInstraction)
    }
}
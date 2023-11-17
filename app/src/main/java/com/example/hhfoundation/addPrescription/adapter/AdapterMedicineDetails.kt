package com.example.hhfoundation.addPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
 import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.model.ModelMedicineDetails
import com.example.hhfoundation.addPrescription.model.ModelSubCatList
import okhttp3.internal.assertThreadDoesntHoldLock

class AdapterMedicineDetails (val context:Context, val list: List<ModelMedicineDetails>):
    RecyclerView.Adapter<AdapterMedicineDetails.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_medicine,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.medicineName.text= list[position].medicineName
        holder.brand.setText(list[position].brand)
        holder.medicineTiming.setText(list[position].dosage)
        holder.frequency.setText(list[position].frequency)
        holder.duration.setText(list[position].days)
        holder.tvInstraction.setText(list[position].instraction)

        holder.imgRemove.setOnClickListener {
            removeAt(position)

        }

    }

    private fun removeAt(position: Int) {
        var listData: MutableList<ModelMedicineDetails> = list as MutableList<ModelMedicineDetails>
        listData.removeAt(position)
        notifyDataSetChanged()
        notifyItemRangeChanged(position, list.size)
    }



    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val medicineName:TextView=itemView.findViewById(R.id.tvMedicineName)
        val medicineTiming:EditText=itemView.findViewById(R.id.tvMedicinetiming)
        val frequency:EditText=itemView.findViewById(R.id.Frequency)
        val duration:EditText=itemView.findViewById(R.id.tvDuration)
        val brand:EditText=itemView.findViewById(R.id.tvBrand)
        val tvInstraction:EditText=itemView.findViewById(R.id.tvInstraction)
        val imgRemove:ImageView=itemView.findViewById(R.id.imgRemove)
    }
}
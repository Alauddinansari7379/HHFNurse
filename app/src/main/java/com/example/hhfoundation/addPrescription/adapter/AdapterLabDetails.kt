package com.example.hhfoundation.addPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
 import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.model.ModelLabDetails
import com.example.hhfoundation.addPrescription.model.ModelMedicineDetails
import com.example.hhfoundation.addPrescription.model.ModelSubCatList

class AdapterLabDetails (val context:Context, val list: List<ModelLabDetails>):
    RecyclerView.Adapter<AdapterLabDetails.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_lab,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.tvLabName.text= list[position].testName

        holder.imgRemove.setOnClickListener {
            removeAt(position)

         }
    }
    private fun removeAt(position: Int) {
        var listData: MutableList<ModelLabDetails> = list as MutableList<ModelLabDetails>
        listData.removeAt(position)
        notifyDataSetChanged()
        notifyItemRangeChanged(position, list.size)
    }
    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvLabName:TextView=itemView.findViewById(R.id.tvLabName)
        val imgRemove:ImageView=itemView.findViewById(R.id.imgRemove)
    }
}
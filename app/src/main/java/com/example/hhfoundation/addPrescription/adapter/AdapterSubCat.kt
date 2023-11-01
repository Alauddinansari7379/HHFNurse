package com.example.hhfoundation.addPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
 import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.model.ModelLabDetails
import com.example.hhfoundation.addPrescription.model.ModelLabSubCat
import com.example.hhfoundation.addPrescription.model.ModelMedicineDetails

class AdapterSubCat (val context:Context, val list: List<ModelLabSubCat>):
    RecyclerView.Adapter<AdapterSubCat.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_sub,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.tvLabName.text= list[position].subCatName


    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvLabName:TextView=itemView.findViewById(R.id.tvSubName)
    }
}
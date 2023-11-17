package com.example.hhfoundation.addPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.AddPrescription
import com.example.hhfoundation.addPrescription.model.ModelLabSubCat
import com.example.hhfoundation.addPrescription.model.ModelSubCatList

class AdapterSubCat(
    val context: Context,
    val list: List<ModelLabSubCat>,
 ) :
    RecyclerView.Adapter<AdapterSubCat.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_add_sub, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.tvLabName.text = list[position].subCatName

        holder.imgRemove.setOnClickListener {
            removeAt(position)

            // remove.onRemoveItem(position,list[position].subCatName)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun removeAt(position: Int) {
        var listData: MutableList<ModelSubCatList> = list as MutableList<ModelSubCatList>
        listData.removeAt(position)
        notifyDataSetChanged()
        notifyItemRangeChanged(position, list.size)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvLabName: TextView = itemView.findViewById(R.id.tvSubName)
        val imgRemove: ImageView = itemView.findViewById(R.id.imgRemove)
    }

}
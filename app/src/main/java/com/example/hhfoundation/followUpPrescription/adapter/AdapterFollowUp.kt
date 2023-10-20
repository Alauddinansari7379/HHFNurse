package com.example.hhfoundation.followUpPrescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.followUpPrescription.model.Prescriptiondetail

class AdapterFollowUp(val context: Context, val list: List<Prescriptiondetail>,val addVital: AddVital) :
    RecyclerView.Adapter<AdapterFollowUp.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_follow_up_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.idFL.text = list[position].patientid
        holder.dateFL.text = list[position].date
        holder.patientNameFL.text = list[position].patientname
        holder.schoolNameFL.text = list[position].follow_school
        holder.doctorNameFL.text = list[position].doctrname

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

//        holder.btnAavitalFL.setOnClickListener {
////            val intent = Intent(context as Activity, StudentDetailsOne::class.java)
////            studentId=list[position].id
////             context.startActivity(intent)
//        }

        holder.btnAavitalFL.setOnClickListener {
            addVital.addVital()
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idFL: TextView = itemView.findViewById(R.id.idFL)
        val schoolNameFL: TextView = itemView.findViewById(R.id.schoolNameFL)
        val doctorNameFL: TextView = itemView.findViewById(R.id.doctorNameFL)
        val patientNameFL: TextView = itemView.findViewById(R.id.patientNameFL)
        val dateFL: TextView = itemView.findViewById(R.id.dateFL)
        val btnRequestAppFL: Button = itemView.findViewById(R.id.btnRequestAppFL)
        val btnAavitalFL: Button = itemView.findViewById(R.id.btnAavitalFL)

    }
    interface AddVital{
        fun addVital()
    }
}
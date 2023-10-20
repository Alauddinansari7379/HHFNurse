package com.example.hhfoundation.clinicalManagement.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList

class AdapterAppointmentList(val context: Context, val list: List<PrescriptionList>) :
    RecyclerView.Adapter<AdapterAppointmentList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_appointment_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.reqDateApp.text = list[position].request
        holder.schoolIdApp.text = list[position].hospital_id

        holder.studentIDApp.text = list[position].patient
        holder.typeApp.text = list[position].appotype
        holder.studentNameApp.text = list[position].patient
        holder.dateApp.text = list[position].date
        holder.doctorNameApp.text = list[position].doctor
        holder.remarkApp.text = list[position].remarks

        when (list[position].status) {
            "Confirmed" -> {
                holder.statusApp.text = list[position].status
                 holder.statusApp.setTextColor(Color.parseColor("#e9c368"))
                 holder.tvStatus.setTextColor(Color.parseColor("#e9c368"))
                holder.layoutStatus.setBackgroundResource(R.drawable.top_curve_heading_yellow)
            }

            "Pending Confirmation" -> {
                holder.statusApp.text = list[position].status
                holder.statusApp.setTextColor(Color.parseColor("#fa3435"))
                holder.tvStatus.setTextColor(Color.parseColor("#fa3435"))
                holder.layoutStatus.setBackgroundResource(R.drawable.top_curve_heading_red)
            }
            "Treated" -> {
                holder.statusApp.text = list[position].status
                holder.statusApp.setTextColor(Color.parseColor("#59ba8e"))
                holder.tvStatus.setTextColor(Color.parseColor("#59ba8e"))
                holder.layoutStatus.setBackgroundResource(R.drawable.top_curve_heading_green)
            }
        }


//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

        holder.btnInfoApp.setOnClickListener {
//            val intent = Intent(context as Activity, StudentDetailsOne::class.java)
//            studentId=list[position].id
//             context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reqDateApp: TextView = itemView.findViewById(R.id.reqDateApp)
        val schoolIdApp: TextView = itemView.findViewById(R.id.schoolIdApp)
        val statusApp: TextView = itemView.findViewById(R.id.statusApp)
        val studentIDApp: TextView = itemView.findViewById(R.id.studentIDApp)
        val typeApp: TextView = itemView.findViewById(R.id.typeApp)
        val studentNameApp: TextView = itemView.findViewById(R.id.studentNameApp)
        val dateApp: TextView = itemView.findViewById(R.id.dateApp)
        val doctorNameApp: TextView = itemView.findViewById(R.id.doctorNameApp)
        val remarkApp: TextView = itemView.findViewById(R.id.remarkApp)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnInfoApp: Button = itemView.findViewById(R.id.btnInfoApp)
        val btnViewPreApp: Button = itemView.findViewById(R.id.btnViewPreApp)
        val layoutStatus: LinearLayout = itemView.findViewById(R.id.layoutStatus)

    }
}
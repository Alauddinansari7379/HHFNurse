package com.example.hhfoundation.clinicalManagement.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.AddPrescription
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.sharedpreferences.SessionManager

class AdapterAppointmentList(
    val context: Context,
    val list: List<PrescriptionList>,
    val info: Information
) :

    RecyclerView.Adapter<AdapterAppointmentList.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_appointment_list, parent, false)
        )


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sessionManager = SessionManager(context)

        if (sessionManager.group == "Doctor" && list[position].status=="Treated") {
            holder.btnAddPrescription.visibility = View.VISIBLE
        }

        if (list[position].status=="Confirmed") {
            holder.btnAddPrescription.visibility = View.GONE
        }

        if (list[position].status=="Pending Confirmation") {
            holder.btnAddPrescription.visibility = View.GONE
        }


        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.reqDateApp.text = list[position].add_date
        holder.schoolIdApp.text = list[position].hospital_id
        holder.studentIDApp.text = list[position].patient
        holder.typeApp.text = list[position].appotype
        holder.studentNameApp.text = list[position].patientname
        holder.dateApp.text = list[position].date
        holder.doctorNameApp.text = list[position].doctorname
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
            else -> {
                holder.statusApp.text = "NA"
                holder.statusApp.setTextColor(Color.parseColor("#59ba8e"))
                holder.tvStatus.setTextColor(Color.parseColor("#59ba8e"))
                holder.layoutStatus.setBackgroundResource(R.drawable.top_curve_heading_none)
            }
        }


//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)


        holder.btnInfoApp.setOnClickListener {
            info.info(list[position].id)
        }
        holder.btnAddPrescription.setOnClickListener {
            val intent = Intent(context as Activity, AddPrescription::class.java)
                .putExtra("date", list[position].date)
                .putExtra("patientname", list[position].patientname)
                .putExtra("doctorname", list[position].doctorname)
                .putExtra("patient", list[position].patient)
                .putExtra("id", list[position].id)

                .putExtra("birthdate", list[position].birthdate)
                .putExtra("doctor", list[position].doctor)
                .putExtra("bloodgroup", list[position].bloodgroup)
                .putExtra("sex", list[position].sex)
                .putExtra("schl", list[position].schl)
                .putExtra("schl_addr", list[position].schl_addr)
                .putExtra("hospital_id", list[position].hospital_id)
                .putExtra("appotype", list[position].appotype)
            context.startActivity(intent)
        }

        holder.btnViewPreApp.setOnClickListener {
//            val httpIntent = Intent(Intent.ACTION_VIEW)
//            httpIntent.data = Uri.parse("https://schoolhms.thedemostore.in/auth/prespdf?id=${list[position].id}")
//            context.startActivity(httpIntent)
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
        val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescription)
        val layoutStatus: LinearLayout = itemView.findViewById(R.id.layoutStatus)

    }

    interface Information {
        fun info(id: String)
    }
}
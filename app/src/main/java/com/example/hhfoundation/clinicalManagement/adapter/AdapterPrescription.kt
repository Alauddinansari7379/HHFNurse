package com.example.hhfoundation.clinicalManagement.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.IPCMS.adapter.AdapterFollowUp
import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.AddPrescription
import com.example.hhfoundation.labReport.activity.ViewLabReport
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.sharedpreferences.SessionManager


class AdapterPrescription(
    val context: Context,
    val list: List<Prescriptiondetail>,
    val favourite: Favourite
) :
    RecyclerView.Adapter<AdapterPrescription.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_priscription_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sessionManager = SessionManager(context)

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.preId.text = list[position].pid
        holder.date.text = list[position].follow_date

        holder.patientName.text = list[position].patientname

        holder.patientIdPre.text = list[position].patientid
        holder.doctorPre.text = list[position].doctrname

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)
        if (list[position].favourite.toString() != "null") {
            holder.imHeartRed.visibility = View.VISIBLE
            holder.imHeart.visibility = View.GONE
        }

        if (list[position].favourite.toString() == "null") {
            holder.imHeart.visibility = View.VISIBLE
            holder.imHeartRed.visibility = View.GONE
        }

        if (sessionManager.group == "Doctor") {
            holder.btnEditPre.visibility = View.VISIBLE
        }
        if (sessionManager.group != "Doctor") {
            holder.imHeart.visibility = View.GONE
            holder.imHeartRed.visibility = View.GONE
        }

        holder.btnEditPre.setOnClickListener {
//            val intent = Intent(context as Activity, AddPrescription::class.java)
//                .putExtra("date", list[position].date)
//                .putExtra("patientname", list[position].patientname)
//                .putExtra("doctorname", list[position].doctorname)
//                .putExtra("patient", list[position].patient)
//                .putExtra("id", list[position].id)
//                .putExtra("birthdate", list[position].birthdate)
//                .putExtra("doctor", list[position].doctor)
//                .putExtra("bloodgroup", list[position].bloodgroup)
//                .putExtra("sex", list[position].sex)
//                .putExtra("schl", list[position].schl)
//                .putExtra("schl_addr", list[position].schl_addr)
//                .putExtra("hospital_id", list[position].hospital_id)
//                .putExtra("appotype", list[position].appotype)
//            context.startActivity(intent)
        }

        holder.btnViewPre.setOnClickListener {
//            val intent = Intent(context as Activity, ViewLabReport::class.java)
//                .putExtra("pid", list[position].pid)
//                .putExtra("prescription", "1")
//            context.startActivity(intent)

            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data =
                Uri.parse("https://schoolhms.thedemostore.in/auth/prespdf?id=${list[position].pid}")
            context.startActivity(httpIntent)
        }
        holder.imHeart.setOnClickListener {
            favourite.addFav(list[position].pid)
        }

        holder.imHeartRed.setOnClickListener {
            favourite.removeFav(list[position].pid)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val preId: TextView = itemView.findViewById(R.id.preIdPre)
        val date: TextView = itemView.findViewById(R.id.datePre)
        val patientName: TextView = itemView.findViewById(R.id.patientPre)
        val patientIdPre: TextView = itemView.findViewById(R.id.patirntIdPre)
        val doctorPre: TextView = itemView.findViewById(R.id.doctorPre)
        val btnViewPre: Button = itemView.findViewById(R.id.btnViewPre)
        val btnEditPre: Button = itemView.findViewById(R.id.btnEditPre)
        val imHeart: ImageView = itemView.findViewById(R.id.imHeart)
        val imHeartRed: ImageView = itemView.findViewById(R.id.imHeartRed)

    }
    interface Favourite {
        fun addFav(pid: String)
        fun removeFav(pid: String)

    }

}
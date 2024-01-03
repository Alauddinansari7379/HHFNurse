package com.example.hhfoundation.IPCMS.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.example.hhfoundation.R
import com.example.hhfoundation.IPCMS.activtiy.*
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.sharedpreferences.SessionManager

class AdapterReferrals(
    val context: Context,
    val list: List<Prescriptiondetail>,
    val info: Information
) :
    RecyclerView.Adapter<AdapterReferrals.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_referrals_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sessionManager = SessionManager(context)

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.idRef.text = list[position].patientid
        holder.dateRef.text = list[position].follow_date
        holder.patientNameRef.text = list[position]!!.patientname
        holder.hospitalNameRef.text = list[position].refer_hospital
        holder.doctorNameRef.text = list[position].doctrname
        // holder.statusRef.text = list[position].sugar

        //   if ($prescription->patient_reach === NULL)    to confirm button dikhega

        // if ($prescription->patient_reach !== NULL && $prescription->ref_status === 'In Patient') { to add vital dikhega


        if (sessionManager.group == "Receptionist" && list[position].patient_reach != null && list[position].ref_status == "In Patient") {
            holder.addVitals.visibility = View.VISIBLE
            holder.uploadDetails.visibility = View.VISIBLE
            holder.dischargeDetails.visibility = View.VISIBLE
            holder.lAMA.visibility = View.VISIBLE
            holder.addExpired.visibility = View.VISIBLE

            holder.btnInfoRef.visibility = View.GONE
            holder.confirm.visibility = View.GONE
        }

        if (sessionManager.group == "Receptionist" && list[position].discharge_count > 0 || list[position].lama_count > 0 || list[position].expired_count > 0) {
            holder.addVitals.visibility = View.VISIBLE
            holder.uploadDetails.visibility = View.VISIBLE
            holder.dischargeDetails.visibility = View.GONE
            holder.lAMA.visibility = View.GONE
            holder.addExpired.visibility = View.GONE

            holder.btnInfoRef.visibility = View.GONE
            holder.confirm.visibility = View.GONE
        }
        if (sessionManager.group == "Receptionist" && list[position].patient_reach != null && list[position].ref_status == "In Patient") {
            holder.addVitals.visibility = View.VISIBLE
            holder.uploadDetails.visibility = View.VISIBLE
            holder.btnInfoRef.visibility = View.VISIBLE
//
//            holder.dischargeDetails.visibility = View.GONE
//            holder.lAMA.visibility = View.GONE
//            holder.addExpired.visibility = View.GONE
//            holder.confirm.visibility = View.GONE
        }


        if (sessionManager.group == "Receptionist" && list[position].patient_reach == null) {
            holder.confirm.visibility = View.VISIBLE

            holder.addVitals.visibility = View.GONE
            holder.uploadDetails.visibility = View.GONE
            holder.dischargeDetails.visibility = View.GONE
            holder.lAMA.visibility = View.GONE
            holder.addExpired.visibility = View.GONE
            holder.btnInfoRef.visibility = View.GONE
        }

        if (sessionManager.group=="Pharmacist"){
            holder.confirm.visibility = View.GONE
            holder.addVitals.visibility = View.GONE
            holder.uploadDetails.visibility = View.GONE
            holder.dischargeDetails.visibility = View.GONE
            holder.lAMA.visibility = View.GONE
            holder.addExpired.visibility = View.GONE
            holder.btnInfoRef.visibility = View.VISIBLE
        }
//
//        if (sessionManager.group == "Receptionist" && list[position].patient_reach == null && list[position].ref_status == null) {
//            holder.addVitals.visibility = View.VISIBLE
//            holder.uploadDetails.visibility = View.VISIBLE
//            holder.dischargeDetails.visibility = View.VISIBLE
//            holder.lAMA.visibility = View.VISIBLE
//            holder.addExpired.visibility = View.VISIBLE
//
//            holder.btnInfoRef.visibility = View.GONE
//            holder.confirm.visibility = View.GONE
//        }

        if (list[position].favourite.toString() != "null") {
            holder.imHeartRed.visibility = View.VISIBLE
            holder.imHeart.visibility = View.GONE
        }

        if (list[position].favourite.toString() == "null") {
            holder.imHeart.visibility = View.VISIBLE
            holder.imHeartRed.visibility = View.GONE
        }
        if (sessionManager.group!="Receptionist"){
            holder.imHeart.visibility = View.GONE
            holder.imHeartRed.visibility = View.GONE
        }

        holder.addVitals.setOnClickListener {
            val intent = Intent(context as Activity, AddVital::class.java)
                .putExtra("pid", list[position].pid)
                .putExtra("patientid", list[position].patientid)
                .putExtra("presc", list[position].presc)
                .putExtra("patientname", list[position].patientname)
                .putExtra("date", list[position].follow_date)
                .putExtra("created_at", list[position].created_at)
            context.startActivity(intent)
        }

        holder.uploadDetails.setOnClickListener {
            val intent = Intent(context as Activity, UploadDetails::class.java)
                .putExtra("pid", list[position].pid)
                .putExtra("patientid", list[position].patientid)
                .putExtra("presc", list[position].presc)
                .putExtra("patientname", list[position].patientname)
                .putExtra("date", list[position].follow_date)
                .putExtra("refer_hospital", list[position].refer_hospital)
                .putExtra("department_name", list[position].department_name)
                .putExtra("created_at", list[position].created_at)
                .putExtra("health_issue", list[position].health_issue)
                .putExtra("appoitment_id", list[position].appoitment_id)
            context.startActivity(intent)
        }

        holder.dischargeDetails.setOnClickListener {
            val intent = Intent(context as Activity, DischargeDetails::class.java)
                .putExtra("pid", list[position].pid)
                .putExtra("patientid", list[position].patientid)
                .putExtra("presc", list[position].presc)
                .putExtra("patientname", list[position].patientname)
                .putExtra("date", list[position].follow_date)
                .putExtra("refer_hospital", list[position].refer_hospital)
                .putExtra("department_name", list[position].department_name)
                .putExtra("created_at", list[position].created_at)
                .putExtra("health_issue", list[position].health_issue)
                .putExtra("appoitment_id", list[position].appoitment_id)
            context.startActivity(intent)
        }

        holder.lAMA.setOnClickListener {
            val intent = Intent(context as Activity, Lama::class.java)
                .putExtra("pid", list[position].pid)
            context.startActivity(intent)
        }

        holder.confirm.setOnClickListener {
            val intent = Intent(context as Activity, PatientConfirmation::class.java)
                .putExtra("pid", list[position].pid)
            context.startActivity(intent)
        }

        holder.addExpired.setOnClickListener {
            val intent = Intent(context as Activity, Expired::class.java)
                .putExtra("pid", list[position].pid)
            context.startActivity(intent)
        }

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)


        holder.btnInfoRef.setOnClickListener {
            //info.info(list[position].pid)

            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data =
                Uri.parse("https://schoolhms.thedemostore.in/auth/prespdf?id=${list[position].pid}")
            context.startActivity(httpIntent)
//            val intent = Intent(context as Activity, StudentDetailsOne::class.java)
//            studentId=list[position].id
//             context.startActivity(intent)
        }
        holder.imHeart.setOnClickListener {
            info.addFav(list[position].pid)
        }

        holder.imHeartRed.setOnClickListener {
            info.removeFav(list[position].pid)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idRef: TextView = itemView.findViewById(R.id.idRef)
        val hospitalNameRef: TextView = itemView.findViewById(R.id.hospitalNameRef)
        val doctorNameRef: TextView = itemView.findViewById(R.id.doctorNameRef)
        val patientNameRef: TextView = itemView.findViewById(R.id.patientNameRef)
        val dateRef: TextView = itemView.findViewById(R.id.dateRef)
        val statusRef: TextView = itemView.findViewById(R.id.statusRef)
        val btnInfoRef: Button = itemView.findViewById(R.id.btnInfoRef)
        val addVitals: Button = itemView.findViewById(R.id.AddVitals)
        val uploadDetails: Button = itemView.findViewById(R.id.UploadDetails)
        val dischargeDetails: Button = itemView.findViewById(R.id.DischargeDetails)
        val confirm: Button = itemView.findViewById(R.id.Confirm)
        val lAMA: Button = itemView.findViewById(R.id.LAMA)
        val addExpired: Button = itemView.findViewById(R.id.AddExpired)
        val imHeart: ImageView = itemView.findViewById(R.id.imHeart)
        val imHeartRed: ImageView = itemView.findViewById(R.id.imHeartRed)

    }

    interface Information {
        fun info(id: String)
        fun addFav(pid: String)
        fun removeFav(pid: String)
    }
}
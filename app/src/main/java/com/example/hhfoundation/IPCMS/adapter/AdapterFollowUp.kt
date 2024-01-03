package com.example.hhfoundation.IPCMS.adapter

import android.annotation.SuppressLint
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
import com.example.hhfoundation.IPCMS.activtiy.AddVital
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.sharedpreferences.SessionManager

class AdapterFollowUp(
    val context: Context,
    val list: List<Prescriptiondetail>,
    val favourite: Favourite
) :
    RecyclerView.Adapter<AdapterFollowUp.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_follow_up_list, parent, false)
        )
    }

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sessionManager = SessionManager(context)

        //  holder.SrNo.text= "${position+1}"
        //  holder.refrencecode.text= list[position].referenceCode
        holder.idFL.text = list[position].pid
        holder.dateFL.text = list[position].follow_date
        holder.patientNameFL.text = list[position].patientname
        holder.schoolNameFL.text = list[position].follow_school
        holder.doctorNameFL.text = list[position].doctrname





        if (list[position].favourite.toString() != "null") {
            holder.imHeartRed.visibility = View.VISIBLE
            holder.imHeart.visibility = View.GONE
        }

        if (list[position].favourite.toString() == "null") {
            holder.imHeart.visibility = View.VISIBLE
            holder.imHeartRed.visibility = View.GONE
        }

        if (list[position].follow == "Not Following the Treatment" || list[position].follow_reason == "Cured" || list[position].follow_reason == "Stable") {
            holder.btnAavitalFL.visibility = View.GONE
        }

//
//        Picasso.get().load("https://schoolhms.thedemostore.in/" + list[position].img_url)
//            .placeholder(R.drawable.placeholder_n)
//            .error(R.drawable.error_placeholder)
//            .into(holder.imageViewPL)

        if (sessionManager.group == "Pharmacist") {
            holder.btnAavitalFL.visibility = View.GONE
        }

        holder.btnAavitalFL.setOnClickListener {
            val intent = Intent(context as Activity, AddVital::class.java)
                .putExtra("pid", list[position].pid)
                .putExtra("patientid", list[position].patientid)
                .putExtra("presc", list[position].presc)
                .putExtra("patientname", list[position].patientname)
                .putExtra("date", list[position].follow_date)
                .putExtra("created_at", list[position].created_at)
            context.startActivity(intent)
        }

        holder.btnRequestAppFL.setOnClickListener {
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
        val idFL: TextView = itemView.findViewById(R.id.idFL)
        val schoolNameFL: TextView = itemView.findViewById(R.id.schoolNameFL)
        val doctorNameFL: TextView = itemView.findViewById(R.id.doctorNameFL)
        val patientNameFL: TextView = itemView.findViewById(R.id.patientNameFL)
        val dateFL: TextView = itemView.findViewById(R.id.dateFL)
        val btnRequestAppFL: Button = itemView.findViewById(R.id.btnRequestAppFL)
        val btnAavitalFL: Button = itemView.findViewById(R.id.btnAavitalFL)
        val imHeart: ImageView = itemView.findViewById(R.id.imHeart)
        val imHeartRed: ImageView = itemView.findViewById(R.id.imHeartRed)

    }

    interface Favourite {
        fun addFav(pid: String)
        fun removeFav(pid: String)

    }

}
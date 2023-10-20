package com.example.hhfoundation.dasboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.activity.PreviousAppointment
import com.example.hhfoundation.clinicalManagement.activity.NewAppointment
import com.example.hhfoundation.dasboard.model.ModelDashboard
import com.example.hhfoundation.databinding.ActivityDashboardBinding
import com.example.hhfoundation.followUpPrescription.FollowUpPrescription
import com.example.hhfoundation.followUpPrescription.Referrals
import com.example.hhfoundation.followUpPrescription.ReferralsFollowUps
import com.example.hhfoundation.login.Login
import com.example.hhfoundation.login.model.ModelLogin
import com.example.hhfoundation.medicalHistory.activity.AllMedicalHistory
import com.example.hhfoundation.labReport.activity.LabReport
import com.example.hhfoundation.clinicalManagement.activity.Prescription
import com.example.hhfoundation.clinicalManagement.activity.TodayAppointment
import com.example.hhfoundation.followUpPrescription.DailyVitalUpdate
import com.example.hhfoundation.labReport.activity.LabInvestigations
import com.example.hhfoundation.profile.Profile
import com.example.hhfoundation.registration.activity.StudentBonoFileld
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    lateinit var sessionManager: SessionManager
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@Dashboard)

        apiCallDashboardCount()
        binding.drawerClick.setOnClickListener {
            binding.drawerlayout1.openDrawer(GravityCompat.START)
            binding.includedrawar1.tvDashboard.setOnClickListener {
                startActivity(Intent(this, Dashboard::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.bonafideLayout.setOnClickListener {
                startActivity(Intent(this, StudentBonoFileld::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.medicalHLayout.setOnClickListener {
                startActivity(Intent(this, AllMedicalHistory::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.newAppointmentlayout.setOnClickListener {
                startActivity(Intent(this, NewAppointment::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutPrevious.setOnClickListener {
                startActivity(Intent(this, PreviousAppointment::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.todayAppointmentLayout.setOnClickListener {
                startActivity(Intent(this, TodayAppointment::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutPrescription.setOnClickListener {
                startActivity(Intent(this, Prescription::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutFollowUp.setOnClickListener {
                startActivity(Intent(this, FollowUpPrescription::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutReferrals.setOnClickListener {
                startActivity(Intent(this, Referrals::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutReferralsFollow.setOnClickListener {
                startActivity(Intent(this, ReferralsFollowUps::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvLoginProifle.setOnClickListener {
                startActivity(Intent(this, Profile::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutAdvisedAttach.setOnClickListener {
                startActivity(Intent(this, LabReport::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.layoutAdvisedLabIn.setOnClickListener {
                startActivity(Intent(this, LabInvestigations::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.layoutDaliVital.setOnClickListener {
                startActivity(Intent(this, DailyVitalUpdate::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvLogout.setOnClickListener {
                SweetAlertDialog(this@Dashboard, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure want to Logout?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        logOut()
                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.PreventiveScreening.setOnClickListener {
                if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
//                    binding.includedrawar1.reportLayout.setBackgroundColor(resources.getColor(R.color.white))
//                    binding.includedrawar1.Report.setBackgroundColor(resources.getColor(R.color.white))
                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
                } else {
                    binding.includedrawar1.PreventiveSLayout.visibility = View.VISIBLE
                    // binding.includedrawar1.PreventiveScreening.setBackgroundColor(resources.getColor(R.color.gray))
                    binding.includedrawar1.PreventiveSLayout.setBackgroundColor(resources.getColor(R.color.main_color))
                    binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_remove_24)
                }
            }
            binding.includedrawar1.ClinicalManagement.setOnClickListener {
                if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
                } else {
                    binding.includedrawar1.ClinicalMLayout.visibility = View.VISIBLE
                    binding.includedrawar1.ClinicalMLayout.setBackgroundColor(resources.getColor(R.color.main_color))
                    binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_remove_24)
                }
            }

            binding.includedrawar1.RefferalsAFU.setOnClickListener {
                if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
                } else {
                    binding.includedrawar1.RefferalsAFULayout.visibility = View.VISIBLE
                    binding.includedrawar1.RefferalsAFULayout.setBackgroundColor(
                        resources.getColor(
                            R.color.main_color
                        )
                    )
                    binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_remove_24)
                }
            }

            binding.includedrawar1.LabReports.setOnClickListener {
                if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.LabReportsLayout.visibility = View.GONE
                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
                } else {
                    binding.includedrawar1.LabReportsLayout.visibility = View.VISIBLE
                    binding.includedrawar1.LabReportsLayout.setBackgroundColor(resources.getColor(R.color.main_color))
                    binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_remove_24)
                }
            }
            if (binding.includedrawar1.PreventiveSLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.PreventiveSLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowPS.setImageResource(R.drawable.baseline_add_24)
            }

            if (binding.includedrawar1.ClinicalMLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.ClinicalMLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowCM.setImageResource(R.drawable.baseline_add_24)
            }
            if (binding.includedrawar1.RefferalsAFULayout.visibility == View.VISIBLE) {
                binding.includedrawar1.RefferalsAFULayout.visibility = View.GONE
                binding.includedrawar1.reportArrowRAFU.setImageResource(R.drawable.baseline_add_24)
            }

            if (binding.includedrawar1.LabReportsLayout.visibility == View.VISIBLE) {
                binding.includedrawar1.LabReportsLayout.visibility = View.GONE
                binding.includedrawar1.reportArrowLabR.setImageResource(R.drawable.baseline_add_24)
            }

        }
        drawerLayout = binding.drawerlayout1

    }

    private fun logOut() {

        AppProgressBar.showLoaderDialog(this@Dashboard)
        ApiClient.apiService.logOut(sessionManager.ionId.toString()).enqueue(object :
            Callback<ModelLogin> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelLogin>,
                response: Response<ModelLogin>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Dashboard, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@Dashboard, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.message == "ok") {
                        myToast(this@Dashboard, "Successfully Logout")
                        AppProgressBar.hideLoaderDialog()
                        sessionManager.logout()
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    } else {
                        AppProgressBar.hideLoaderDialog()
                        myToast(this@Dashboard, response.body()!!.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Dashboard, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {
                myToast(this@Dashboard, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }
    private fun apiCallDashboardCount() {

        //AppProgressBar.showLoaderDialog(this@Dashboard)
        ApiClient.apiService.getDashboard(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
        ).enqueue(object :
            Callback<ModelDashboard> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelDashboard>,
                response: Response<ModelDashboard>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Dashboard, "Server Error")
                    } else if (response.code() == 404) {
                        myToast(this@Dashboard, "Something went wrong")
                    } else if (response.body()!!.message=="token mismatch") {
                        myToast(this@Dashboard,"User Logged in other Device")
                        sessionManager.logout()
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)

                       // AppProgressBar.hideLoaderDialog()
                    } else {
                        binding.medicalHis.text=response.body()!!.medicalhistory.toString()
                        binding.todaySick.text=response.body()!!.todaysick.toString()
                        binding.totalSick.text=response.body()!!.totalsick.toString()
                        binding.treated.text=response.body()!!.treated.toString()
                        binding.pendingApp.text=response.body()!!.pending.toString()
                        binding.generalCase.text=response.body()!!.general.toString()
                        binding.emergencyCase.text=response.body()!!.emergency.toString()
                        binding.advisesLab.text=response.body()!!.adivise.toString()
                        binding.pendingLabIn.text=response.body()!!.pendinglab.toString()
                        binding.completeLabInv.text=response.body()!!.completlab.toString()
                        binding.referrals.text=response.body()!!.refer.toString()
                        binding.followUp.text=response.body()!!.follow.toString()
                      //  AppProgressBar.hideLoaderDialog()


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Dashboard, "Something went wrong")
                   // AppProgressBar.hideLoaderDialog()

                }

            }

            override fun onFailure(call: Call<ModelDashboard>, t: Throwable) {
                apiCallDashboardCount()
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

}
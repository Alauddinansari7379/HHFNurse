package com.example.hhfoundation.retrofit


import com.example.hhfoundation.addPrescription.model.ModelLabList
import com.example.hhfoundation.addPrescription.model.ModelMainCatList
import com.example.hhfoundation.addPrescription.model.ModelMedicineList
import com.example.hhfoundation.addPrescription.model.ModelSubCatList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.dasboard.model.ModelDashboard
import com.example.hhfoundation.followUpPrescription.model.ModelFollowUp
import com.example.hhfoundation.followUpPrescription.model.ModelReferralsFollow
import com.example.hhfoundation.followUpPrescription.model.ModelRefrreals
import com.example.hhfoundation.followUpPrescription.model.ModelVitalList
import com.example.hhfoundation.labReport.model.ModelLabInv
import com.example.hhfoundation.login.model.ModelLogin
import com.example.hhfoundation.labReport.model.ModelLabReport
import com.example.hhfoundation.labReport.model.ModelPrescription
import com.example.hhfoundation.labReport.model.ModelUpload
import com.example.hhfoundation.medicalHistory.model.ModelMedLIst
import com.example.hhfoundation.medicalHistory.model.ModelMedicalHis
import com.example.hhfoundation.profile.model.ModelProfile
import com.example.hhfoundation.registration.model.ModelPatientList
import com.example.hhfoundation.registration.model.ModelRegister
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    @GET("authenticateNew")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
     ): Call<ModelLogin>

    @GET("logoutapp")
    fun logOut(
        @Query("id") id: String,
        @Query("group") group: String,
    ): Call<ModelLogin>


    @GET("getDashboard")
    fun getDashboard(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
    ): Call<ModelDashboard>


    @GET("viewmedicalinfo")
    fun viewMedicalInfo(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("patient_id") patient_id: String
    ): Call<ModelMedLIst>

    @Multipart
    @POST("AddPatient")
    fun registerPatient(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("name") name: String,
        @Query("password") password: String,
        @Query("doctor") doctor: String,
        @Query("address") address: String,
        @Query("phone") phone: String,
        @Query("sex") sex: String,
        @Query("schl") schl: String,
        @Query("schl_email") schl_email: String,
        @Query("schl_addr") schl_addr: String,
        @Query("sch_phone") sch_phone: String,
        @Query("schl_dist") schl_dist: String,
        @Query("districtma") districtma: String,
        @Query("birthdate") birthdate: String,
        @Query("bloodgroup") bloodgroup: String,
        @Query("section") section: String,
        @Query("initialf") initialf: String,
        @Query("f_name") f_name: String,
        @Query("initialm") initialm: String,
        @Query("m_name") m_name: String,
        @Query("admissionnumber") admissionnumber: String,
        @Query("aadharnumber") aadharnumber: String,
        @Query("district") district: String,
        @Query("persing") persing: String,
         @Part img_url: MultipartBody.Part,
    ): Call<ModelRegister>

    //  @Multipart
    @GET("addAppoitment")
    fun addAppointment(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("appotype") appotype: String,
        @Query("patient") patient: String,
        @Query("sdate") sdate: String,
        @Query("ranbl") ranbl: String,
        @Query("satur") satur: String,
        @Query("issuewiith") issuewiith: String,
        @Query("medhos") medhos: String,
        @Query("doctor") doctor: String,
        @Query("time_slot") time_slot: String,
        @Query("status") status: String,
        @Query("dsname") dsname: String,
        @Query("tostrgt") tostrgt: String,
        @Query("howmany") howmany: String,
        @Query("sendho") sendho: String,
        @Query("remarks") remarks: String,
        @Query("request") request: String,
        @Query("p_name") p_name: String,
        @Query("p_email") p_email: String,
        @Query("p_phone") p_phone: String,
        @Query("p_age") p_age: String,
        @Query("p_gender") p_gender: String,
        @Query("vital") vital: String,
        @Query("pr") pr: String,
        @Query("bp") bp: String,
        @Query("temp") temp: String,
        @Query("complain") complain: String,
        @Query("hosp") hosp: String,
        @Query("medicine") medicine: String,
//        @Part img_url: MultipartBody.Part? = null,
//        @Part img_url2: MultipartBody.Part? = null,
//        @Part img_url3: MultipartBody.Part? = null,
//        @Part img_url4: MultipartBody.Part? = null,
//        @Part img_url5: MultipartBody.Part? = null,
    ): Call<ModelNewAppoint>

    @GET("AddPatientmedical")
    fun addPatientMedical(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("patient_id") patient_id: String,
        @Query("date") date: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("heartinaa") heartinaa: String,
        @Query("phis") phis: String,
        @Query("phissda") phissda: String,
        @Query("phissef") phissef: String,
        @Query("height") height: String,
        @Query("weight") weight: String,
        @Query("visual") visual: String,
        @Query("dental") dental: String,
        @Query("ent") ent: String,
        @Query("family") family: String,
        @Query("behavior") behavior: String,
        @Query("socio") socio: String,
        @Query("relation") relation: String,
        @Query("medhos") medhos: String,
        @Query("heartin") heartin: String,
        @Query("hyperthyroidin") hyperthyroidin: String,
        @Query("anemoio") anemoio: String,
        @Query("bitooo") bitooo: String,
        @Query("rickss") rickss: String,
        @Query("acutess") acutess: String,
        @Query("goitrdd") goitrdd: String,
        @Query("skinss") skinss: String,
        @Query("ottyss") ottyss: String,
        @Query("heartss") heartss: String,
        @Query("breathasa") breathasa: String,
        @Query("dntels") dntels: String,
        @Query("episss") episss: String,
        @Query("visions") visions: String,
        @Query("hearrss") hearrss: String,
        @Query("nerosdss") nerosdss: String,
        @Query("motrsds") motrsds: String,
        @Query("cogsa") cogsa: String,
        @Query("lagsa") lagsa: String,
        @Query("begaasds") begaasds: String,
        @Query("learnsds") learnsds: String,
        @Query("disorsd") disorsd: String,
        @Query("difdsds") difdsds: String,
        @Query("drnksd") drnksd: String,
        @Query("trds") trds: String,
        @Query("cyclesd") cyclesd: String,
        @Query("periodsdas") periodsdas: String,
        @Query("painds") painds: String,
        @Query("areasds") areasds: String,
        @Query("feelsd") feelsd: String,
        @Query("bhhiiosds") bhhiiosds: String,
    ): Call<ModelNewAppoint>


    @GET("patientlist")
    fun patientList(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelPatientList>

    @GET("getmedical")
    fun getmedical(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelMedicalHis>

    @GET("prescriptionlist")
    fun prescriptionlist(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelPrescription>

    @GET("previousappoitments")
    fun previousappoitments(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("status") status: String,
    ): Call<ModelAppointList>

    @GET("todayappoitments")
    fun todayAppointments(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelAppointList>

    @GET("getProfile")
    fun getProfile(
        @Query("nurse_id") id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelProfile>

    @GET("followplist")
    fun followUpList(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelFollowUp>

    @GET("getreferpres")
    fun getReferrals(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelRefrreals>

    @GET("referfollow")
    fun getReferralsFollow(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelReferralsFollow>

    @GET("attachlabreport")
    fun attachlabreport(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelLabReport>

    @GET("adviselabdetails")
    fun adviseLabDetails(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelLabInv>

    @GET("vitaldetail")
    fun vitaldetail(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        ): Call<ModelVitalList>

    @GET("appoitmentinfo")
    fun appoitmentinfo(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("appoitment_id") appoitment_id: String,
    ): Call<ModelNewAppoint>

    @GET("addNewvita")
    fun addNewvita(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("pid") pid: String,
        @Query("date") date: String,
        @Query("bp") bp: String,
        @Query("pr") pr: String,
        @Query("temp") temp: String,
        @Query("spo") spo: String,
        @Query("sugar") sugar: String,
        @Query("prgrs") prgrs: String,
        @Query("medical_condition") medical_condition: String,
        @Query("treatment_res") treatment_res: String,
        @Query("prgrs_note") prgrs_note: String,
        @Query("follow") follow: String,
        @Query("follow_reason") follow_reason: String,
        @Query("prd") prd: String,
        @Query("presc_id") presc_id: String,
        @Query("presc") presc: String,
        @Query("freasonnote") freasonnote: String,
    ): Call<ModelRegister>

    @Multipart
    @POST("addLabReport")
    fun addLabReport(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("labid") labid: String,
        @Query("prescription") prescription: String,
        @Query("patient") patient: String,
        @Query("reportname") reportname: String,
        @Query("desc") desc: String,
        @Part img_url: MultipartBody.Part?,
        ): Call<ModelUpload>

    @GET("addPrescr")
    fun addPrescr(
        @Query("nurse_id") nurse_id: String,
        @Query("idToken") idToken: String,
        @Query("group") group: String,
        @Query("patient") patient: String,
        @Query("doctor") doctor: String,
        @Query("pid") pid: String,
        @Query("id") id: String,
        @Query("mypid") mypid: String,
        @Query("birthdate") birthdate: String,
        @Query("date") date: String,
        @Query("age") age: String,
        @Query("blood_group") blood_group: String,
        @Query("sex") sex: String,
        @Query("dist") dist: String,
        @Query("schl") schl: String,
        @Query("email") email: String,
        @Query("address") address: String,
        @Query("f_name") f_name: String,
        @Query("m_name") m_name: String,
        @Query("education") education: String,
        @Query("schl_email") schl_email: String,
        @Query("schl_addr") schl_addr: String,
        @Query("schl_dist") schl_dist: String,
        @Query("phistiry") phistiry: String,
        @Query("strts") strts: String,
        @Query("follow_date") follow_date: String,
        @Query("refer_hospital") refer_hospital: String,
        @Query("health_issue") health_issue: String,
        @Query("department_name") department_name: String,
        @Query("symptom") symptom: String,
        @Query("ltype") ltype: String,
        @Query("fdiagno") fdiagno: String,
        @Query("category") category: String,
        @Query("admin") admin: String,
        @Query("hidden_total_rows") hidden_total_rows: String,
        @Query("note") note: String,
        @Query("submit") submit: String,
        @Query("hypertension") hypertension: String,
        @Query("hypertensionin") hypertensionin: String,
        @Query("diabetes") diabetes: String,
        @Query("diabetescx") diabetescx: String,
        @Query("hypothyroid") hypothyroid: String,
        @Query("hypothyroidin") hypothyroidin: String,
        @Query("heart") heart: String,
        @Query("heartin") heartin: String,
        @Query("allergic_med") allergic_med: String,
        @Query("other_allergic") other_allergic: String,
        @Query("other_allergicin") other_allergicin: String,
        @Query("dcategory") dcategory: String,

        @Query("mainsubc[]") mainsubc:  List<String>,
        @Query("dcategorylab[]") dcategorylab:  List<String>,
        @Query("med_id[]") med_id: List<String>,
        @Query("medicine[]") medicine: List<String>,
        @Query("brand") brand: String,
        @Query("frequency[]") frequency: List<String>,
        @Query("days[]") days: List<String>,
        @Query("instruction[]") instruction: List<String>,
        @Query("dosage[]") dosage: List<String>,

        ): Call<ModelUpload>

//    @GET("addPrescr")
//    fun addPrescr(
//        @Query("nurse_id") nurse_id: String,
//        @Query("idToken") idToken: String,
//        @Query("group") group: String,
//        @Query("patient") patient: String,
//        @Query("doctor") doctor: String,
//        @Query("pid") pid: String,
//        @Query("id") id: String,
//        @Query("mypid") mypid: String,
//        @Query("birthdate") birthdate: String,
//        @Query("date") date: String,
//        @Query("age") age: String,
//        @Query("blood_group") blood_group: String,
//        @Query("sex") sex: String,
//        @Query("dist") dist: String,
//        @Query("schl") schl: String,
//        @Query("email") email: String,
//        @Query("address") address: String,
//        @Query("f_name") f_name: String,
//        @Query("m_name") m_name: String,
//        @Query("education") education: String,
//        @Query("schl_email") schl_email: String,
//        @Query("schl_addr") schl_addr: String,
//        @Query("schl_dist") schl_dist: String,
//        @Query("phistiry") phistiry: String,
//        @Query("strts") strts: String,
//        @Query("follow_date") follow_date: String,
//        @Query("refer_hospital") refer_hospital: String,
//        @Query("health_issue") health_issue: String,
//        @Query("department_name") department_name: String,
//        @Query("symptom") symptom: String,
//        @Query("ltype") ltype: String,
//        @Query("fdiagno") fdiagno: String,
//        @Query("category") category: String,
//        @Query("admin") admin: String,
//        @Query("hidden_total_rows") hidden_total_rows: String,
//        @Query("note") note: String,
//        @Query("submit") submit: String,
//        @Query("hypertension") hypertension: String,
//        @Query("hypertensionin") hypertensionin: String,
//        @Query("diabetes") diabetes: String,
//        @Query("diabetescx") diabetescx: String,
//        @Query("hypothyroid") hypothyroid: String,
//        @Query("hypothyroidin") hypothyroidin: String,
//        @Query("heart") heart: String,
//        @Query("heartin") heartin: String,
//        @Query("allergic_med") allergic_med: String,
//        @Query("other_allergic") other_allergic: String,
//        @Query("other_allergicin") other_allergicin: String,
//        @Query("dcategory") dcategory: String,

//        @Query("mainsubc") mainsubc: Array<String>,
//        @Query("dcategorylab") dcategorylab: Array<String>,
//        @Query("med_id") med_id: Array<String>,
//        @Query("medicine") medicine: Array<String>,
//        @Query("brand") brand: Array<String>,
//        @Query("frequency") frequency: Array<String>,
//        @Query("days") days: Array<String>,
//        @Query("instruction") instruction: Array<String>,
//        @Query("dosage") dosage: Array<String>,
//
//        ): Call<ModelUpload>

    @GET("gemed")
    fun medicineName(
    ): Call<ModelMedicineList>

    @GET("gemain")
    fun gemainCategory(
        @Query("doctor_id") doctor_id:String
    ): Call<ModelMainCatList>

    @GET("getsubCat")
    fun getsubCat(
        @Query("id") id:String
    ): Call<ModelSubCatList>

    @GET("getlab")
    fun labTestName(
    ): Call<ModelLabList>
//
//    @POST("getProfile")
//     fun getProfile(@Body dataModal: DataModal?): Call<ModelProfile>


//    @POST("getProfile")
//     fun getProfile(@Body body: DataModal?): Call<ResponseBody>
}
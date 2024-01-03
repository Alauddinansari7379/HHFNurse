package com.example.hhfoundation.addPrescription

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.example.hhfoundation.Helper.*
import com.example.hhfoundation.R
import com.example.hhfoundation.addPrescription.adapter.AdapterLabDetails
import com.example.hhfoundation.addPrescription.adapter.AdapterMainCat
import com.example.hhfoundation.addPrescription.adapter.AdapterMedicineDetails
import com.example.hhfoundation.addPrescription.adapter.AdapterSubCat
import com.example.hhfoundation.addPrescription.model.*
import com.example.hhfoundation.clinicalManagement.activity.TodaysAppointment
import com.example.hhfoundation.databinding.ActivityAddPrescriptionBinding
import com.example.hhfoundation.labReport.model.ModelUpload
import com.example.hhfoundation.registration.model.ModelSpinner
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.retrofit.ApiInterface
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddPrescription : AppCompatActivity(){
   // MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {
     private lateinit var binding: ActivityAddPrescriptionBinding
    lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    private var followDate = ""
    var diganosticsCat = ""
    var referralHos = ""
    var diagSubCatONe = ""
    var date = ""
    var patientName = ""
    var doctorName = ""
    var studenrId = ""
    var birthdate = ""
    var doctorId = ""
    var strts = ""
    var bloodgroup = ""
    var sex = ""
    var schl = ""
    var schl_addr = ""
    var id = ""
    var hospital_id = ""
    var appotype = ""
    var t = "0"
    var Lab = "0"
    var age = ""
    var edit = ""
    private var medicineListNew = ModelMedicineList()
    private var labTestList = ModelLabList()
    private var mainCatList = ModelMainCatList()
    private val medicineList = ArrayList<ModelMedicineDetails>()
    private val labListNew = ArrayList<ModelLabDetails>()
    private val sucCategory = ArrayList<ModelLabSubCat>()
    private val mainCategory = ArrayList<ModelMainCat>()
    var referralHosList = ArrayList<ModelSpinner>()
    var diagSubCatList = ModelSubCatList()
    var diagSubCatListGen = ModelSubCatList()


    // var mainSucCat = arrayListOf("")
    private val contentList: MutableList<String> = ArrayList()
    private val contentListLab: MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        //List for storing text content for displaying it in the Spinner.
        //Setting Multi Selection Spinner without image.

        //Setting Multi Selection Spinner with image.
        //  multiSpinner.setAdapterWithImage(this,urlList,contentList,this)

        //Setting Multi Selection Spinner with out image.



        date = intent.getStringExtra("date").toString()
        patientName = intent.getStringExtra("patientname").toString()
        doctorName = intent.getStringExtra("doctorname").toString()
        studenrId = intent.getStringExtra("patient").toString()
        birthdate = intent.getStringExtra("birthdate").toString()
        doctorId = intent.getStringExtra("doctor").toString()
        bloodgroup = intent.getStringExtra("bloodgroup").toString()
        sex = intent.getStringExtra("sex").toString()
        schl = intent.getStringExtra("schl").toString()
        schl_addr = intent.getStringExtra("schl_addr").toString()
        id = intent.getStringExtra("id").toString()
        hospital_id = intent.getStringExtra("hospital_id").toString()
        appotype = intent.getStringExtra("appotype").toString()

        val dob = intent.getStringExtra("birthdate").toString()
        edit = intent.getStringExtra("edit").toString()
        Log.e("Before", dob.toString())

        if (edit == "1") {
            binding.appCompatTextView2.text = "Edit Prescription"
        }

        if (dob != null) {
            try {
                var fDOb = ""
                fDOb = if (dob.contains("-", ignoreCase = true)) {
                    changeDateFormat5(dob)
                } else {
                    changeDateFormat6(dob)
                }
                //dd/MM/yyyy
                Log.e("after", dob.toString())

                age = DateUtils.getAgeFromDOB(fDOb)
                Log.e("dob", dob.toString())
                Log.e("Age", age)
                binding.tvAge.text = age

//                if (age.toInt() < 6) {
//                    // binding.btnNext.text = "Submit"
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        apiCallMedicineNameNew()
        apiCallLabTest()
        apiCallDigMainCat()

        with(binding) {


            tvDate.text = date
            tvPatient.text = patientName
            tvDoctor.text = doctorName
            tvStudentId.text = studenrId
            tvDOB.text = birthdate
            tvBloodGroup.text = bloodgroup
            tvGender.text = sex
            tvSchoolName.text = schl
            tvSchoolDis.text = schl_addr

            imgBack.setOnClickListener {
                onBackPressed()
            }

//            btnAddLAb.setOnClickListener {
//
//                edtLab.text.clear()
//                edtLab.requestFocus()
//
//            }

            radioFollowUp.setOnCheckedChangeListener { _, _ ->
                if (radioFollowUp.isChecked) {
                    strts = "Follow"
                    layoutFollowUp.visibility = View.GONE
                    layoutFollowUpDate.visibility = View.VISIBLE
                }
            }

            radioReferralA.setOnCheckedChangeListener { _, _ ->
                if (radioReferralA.isChecked) {
                    strts = "Refer"
                    layoutFollowUp.visibility = View.VISIBLE
                    layoutFollowUpDate.visibility = View.GONE
                }
            }

            radioNone.setOnCheckedChangeListener { _, _ ->
                if (radioNone.isChecked) {
                    strts = ""
                    followDate = ""
                    referralHos = ""
                    binding.edtTypeHealthIssue.text!!.clear()
                    binding.edtTypeDepartment.text!!.clear()
                    binding.tvFollowDate.text = ""
                    layoutFollowUp.visibility = View.GONE
                    layoutFollowUpDate.visibility = View.GONE
                }
            }


            radioHypertensionNo.isChecked = true
            radioHypertensionNo.setOnCheckedChangeListener { _, _ ->
                if (radioHypertensionNo.isChecked) {
                    edtHypertension.visibility = View.GONE
                    edtHypertension.text!!.clear()
                }
            }

            radioHypertensionYes.setOnCheckedChangeListener { _, _ ->
                if (radioHypertensionYes.isChecked) {
                    edtHypertension.visibility = View.VISIBLE
                }
            }

            radioDiabetesMellitusNo.isChecked = true
            radioDiabetesMellitusNo.setOnCheckedChangeListener { _, _ ->
                if (radioDiabetesMellitusNo.isChecked) {
                    edtDiabetesMellitus.visibility = View.GONE
                    edtDiabetesMellitus.text!!.clear()

                }
            }
            radioDiabetesMellitusYes.setOnCheckedChangeListener { _, _ ->
                if (radioDiabetesMellitusYes.isChecked) {
                    edtDiabetesMellitus.visibility = View.VISIBLE
                }
            }

            radioHypothyroidNo.isChecked = true
            radioHypothyroidNo.setOnCheckedChangeListener { _, _ ->
                if (radioHypothyroidNo.isChecked) {
                    edtHypothyroid.visibility = View.GONE
                    edtHypothyroid.text!!.clear()
                }
            }

            radioHypothyroidYes.setOnCheckedChangeListener { _, _ ->
                if (radioHypothyroidYes.isChecked) {
                    edtHypothyroid.visibility = View.VISIBLE
                }
            }

            radioHyperthyroidNo.isChecked = true
            radioHyperthyroidNo.setOnCheckedChangeListener { _, _ ->
                if (radioHyperthyroidNo.isChecked) {
                    edtHyperthyroid.visibility = View.GONE
                    edtHyperthyroid.text!!.clear()
                }
            }

            radioHyperthyroidYes.setOnCheckedChangeListener { _, _ ->
                if (radioHyperthyroidYes.isChecked) {
                    edtHyperthyroid.visibility = View.VISIBLE
                }
            }

            radioHeartDiseaseNo.isChecked = true
            radioHeartDiseaseNo.setOnCheckedChangeListener { _, _ ->
                if (radioHeartDiseaseNo.isChecked) {
                    edtHeartDisease.visibility = View.GONE
                    edtHeartDisease.text!!.clear()
                }
            }

            radioHeartDiseaseYes.setOnCheckedChangeListener { _, _ ->
                if (radioHeartDiseaseYes.isChecked) {
                    edtHeartDisease.visibility = View.VISIBLE
                }
            }

            radioAnyAllergicNo.isChecked = true
            radioAnyAllergicNo.setOnCheckedChangeListener { _, _ ->
                if (radioAnyAllergicNo.isChecked) {
                    edtAnyAllergic.visibility = View.GONE
                    edtAnyAllergic.text!!.clear()
                }
            }

            radioAnyAllergicYes.setOnCheckedChangeListener { _, _ ->
                if (radioAnyAllergicYes.isChecked) {
                    edtAnyAllergic.visibility = View.VISIBLE
                }
            }

            radioOtherAllergicNo.isChecked = true
            radioOtherAllergicNo.setOnCheckedChangeListener { _, _ ->
                if (radioOtherAllergicNo.isChecked) {
                    edtOtherAllergic.visibility = View.GONE
                    edtOtherAllergic.text!!.clear()
                }
            }

            radioOtherAllergicYes.setOnCheckedChangeListener { _, _ ->
                if (radioOtherAllergicYes.isChecked) {
                    edtOtherAllergic.visibility = View.VISIBLE
                }
            }


            mydilaog?.setCanceledOnTouchOutside(false)
            mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val newCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this@AddPrescription,
                { _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    DateFormat.getDateInstance().format(newDate.time)
                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                    followDate =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
                    tvFollowDate.text =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)

                    Log.e(ContentValues.TAG, "dOB:>>$followDate")
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

            tvFollowDate.setOnClickListener {
                datePicker.show()
            }




            referralHosList.add(ModelSpinner("OGH", "1"))


            spinnerReferralHos!!.adapter = ArrayAdapter<ModelSpinner>(
                this@AddPrescription,
                R.layout.simple_list_item_1,
                referralHosList
            )


            spinnerReferralHos.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (referralHosList.size > 0) {
                            referralHos = referralHosList[i].text

                            Log.e(ContentValues.TAG, "referralHos: $referralHos")
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }



            btnSubmit.setOnClickListener {
                if (edit == "1") {
                    apiCallEditPrescription()
                } else {
                    apiCallAddPrescription()
                }
            }
        }
    }

    private fun setRecyclerDataMedicine(
        medicineName: String,
        brand: String,
        dosage: String,
        frequency: String,
        days: String,
        instraction: String,
    ) {

        if (!medicineList.contains(
                ModelMedicineDetails(
                    medicineName,
                    brand,
                    dosage,
                    frequency,
                    days,
                    instraction
                )
            )
        ) {
            medicineList.add(
                ModelMedicineDetails(
                    medicineName,
                    brand,
                    dosage,
                    frequency,
                    days,
                    instraction
                )
            )
            binding.rvrecyclerView.adapter = AdapterMedicineDetails(this, medicineList)
        }


    }

    private fun setRecyclerDataLab(
        medicineName: String,
    ) {
        if (!labListNew.contains(
                ModelLabDetails(
                    medicineName
                )
            )
        ) {
            labListNew.add(ModelLabDetails(medicineName))
            binding.rvrecyclerViewLab.adapter = AdapterLabDetails(this, labListNew)
        }
    }

    private fun setRecyclerDataSub(
        subCat: String,
    ) {
        if (!sucCategory.contains(
                ModelLabSubCat(
                    subCat
                )
            )
        ) {
            sucCategory.add(ModelLabSubCat(subCat))
            binding.rvrecyclerViewSucCat.adapter = AdapterSubCat(this, sucCategory)

        }
    }

    private fun setRecyclerDataMainCat(
        subCat: String,
        id: String,
    ) {
        if (!mainCategory.contains(
                ModelMainCat(
                    subCat,id
                )
            )
        ) {
            mainCategory.add(ModelMainCat(subCat,id))
            binding.rvrecyclerMainCat.adapter = AdapterMainCat(this, mainCategory)

        }
    }

    private fun apiCallMedicineNameNew() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://schoolhms.thedemostore.in/auth/")
            .build()
            .create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.medicineName()
        retrofitData.enqueue(object : Callback<ModelMedicineList> {
            override fun onResponse(
                call: Call<ModelMedicineList>,
                response: Response<ModelMedicineList>
            ) {
                val responseBody = response.body()!!

                try {
                  //  val multiSpinner: MultiSpinner = findViewById(R.id.spinnerMultiSpinner)
                    var itemsNew: MutableList<SearchableItem> = ArrayList()

                    medicineListNew = response.body()!!;
                    if (medicineListNew != null) {

                        //spinner code start
                        val items = arrayOfNulls<String>(medicineListNew.result!!.size)

                        for (i in medicineListNew.result!!.indices) {
                            items[i] = medicineListNew.result!![i].name
                            //contentList.add(medicineListNew.result!![i].name.toString())
                            itemsNew.add(SearchableItem(medicineListNew.result!![i].name.toString(), i.toString()))

                        }
                        binding.spinnerMultiSpinner.setOnClickListener {
                            SearchableMultiSelectSpinner.show(this@AddPrescription, "Select Items","Done", itemsNew, object :
                                SelectionCompleteListener {
                                override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                                    Log.e("data", selectedItems.toString())
                                    for (i in selectedItems) {
                                        setRecyclerDataMedicine(
                                            i.text,
                                            "brand",
                                            "100mg",
                                            "1+0+1",
                                            "7 days",
                                            "After Food",
                                        )
                                        binding.llayout3.visibility = View.VISIBLE

                                        Log.e("chosenItems",i.text)
                                    }



                                }

                            })
                        }
/*//                        val adapter: ArrayAdapter<String?> =
//                            ArrayAdapter(this@ProfileSetting, android.R.layout.simple_list_item_1, items)
//                        binding.spinnerSpecialist.adapter = adapter
//                        progressDialog!!.dismiss()

                        // contentList
                        multiSpinner.setAdapterWithOutImage(
                            this@AddPrescription,
                            contentList,
                            this@AddPrescription
                        )
                        //multiSpinner.setAdapterWithOutImage(this,contentList,this)
                        multiSpinner.initMultiSpinner(this@AddPrescription, multiSpinner)

//                        binding.spinnerMultiSpinner.setOnClickListener {
//                            medicineClick="1"
//                        }
//
//                        val autoSuggestAdapter = AutoSuggestMedicineAdapter(
//                            this@AddPrescription,
//                            android.R.layout.simple_list_item_1,
//                            items.toMutableList()
//                         )
//
//                        binding.edtMedicine.setAdapter(autoSuggestAdapter)
//
//                        binding.edtMedicine.threshold = 1*/


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@AddPrescription, "Something went wrong")

                }
            }


            override fun onFailure(call: Call<ModelMedicineList>, t: Throwable) {
                myToast(this@AddPrescription, "Something went wrong")
            }
        })
    }

/*
    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {
        for (i in chosenItems!!.indices) {
//
//            if (labClick=="1"){
//                labClick=""
//                setRecyclerDataLab(chosenItems[i])
//                binding.llayoutLab.visibility = View.VISIBLE
//            }
            setRecyclerDataMedicine(
                chosenItems[i],
                "brand",
                "100mg",
                "1+0+1",
                "7 days",
                "After Food",
            )
            binding.llayout3.visibility = View.VISIBLE


            Log.e("chosenItems", chosenItems[i])
        }
    }
*/


    private fun apiCallLabTest() {


        ApiClient.apiService.labTestName()
            .enqueue(object : Callback<ModelLabList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelLabList>, response: Response<ModelLabList>
                ) {
                    try {
                        //  val multiSpinner: MultiSpinner = findViewById(R.id.spinnerMultiSpinnerLabTest)
                        var itemsNew: MutableList<SearchableItem> = ArrayList()

                        labTestList = response.body()!!;
                        if (labTestList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(labTestList.result!!.size)

                            for (i in labTestList.result!!.indices) {
                                items[i] = labTestList.result!![i].category
                                itemsNew.add(SearchableItem(labTestList.result!![i].category.toString(), i.toString()))
                                // contentListLab.add(labTestList.result!![i].category.toString())
                            }

                            binding.spinnerLAb.setOnClickListener {
                                SearchableMultiSelectSpinner.show(this@AddPrescription, "Select Items","Done", itemsNew, object :
                                    SelectionCompleteListener {
                                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                                        Log.e("data", selectedItems.toString())
                                        for (i in selectedItems) {
                                              setRecyclerDataLab(i.text)
                                             binding.llayoutLab.visibility = View.VISIBLE
                                            Log.e("chosenItems",i.text)
                                        }



                                    }

                                })
                            }

/*//                            multiSpinner.setAdapterWithOutImage(
//                                this@AddPrescription,
//                                contentListLab,
//                                this@AddPrescription
//                            )
//                            //multiSpinner.setAdapterWithOutImage(this,contentList,this)
//                            multiSpinner.initMultiSpinner(this@AddPrescription, multiSpinner)
//
//
//                            binding.spinnerMultiSpinnerLabTest.setOnClickListener {
//                                 labClick="1"
//                            }

//                            val autoSuggestAdapter = AutoSuggestMedicineAdapter(
//                                this@AddPrescription,
//                                android.R.layout.simple_list_item_1,
//                                items.toMutableList(),
                            binding.edtLab.setAdapter(autoSuggestAdapter)
                            binding.edtLab.threshold = 1
//                            )*/



  /*                          val selectedItem = -1

                            var adapter: ArrayAdapter<String?> =
                                object : ArrayAdapter<String?>(this@AddPrescription, android.R.layout.simple_list_item_1, items) {
                                    override fun getDropDownView(
                                        position: Int,
                                        convertView: View?,
                                        parent: ViewGroup
                                    ): View? {
                                        var v: View? = null
                                        v = super.getDropDownView(position, null, parent)
                                        // If this is the selected item position
//
//                                        if(position== binding.spinnerLAb.selectedItem){
//                                            v.setBackgroundColor(ContextCompat.getColor(this@AddPrescription,R.color.main_color));
//                                        } else {
//                                            v.setBackgroundColor(ContextCompat.getColor(this@AddPrescription,R.color.gray_text_color));
//                                        }
//                                        if (position == selectedItem) {
//                                            v.setBackgroundColor(Color.BLUE)
//                                        } else {
//                                            // for other views
//                                            v.setBackgroundColor(Color.WHITE)
//                                        }
                                        return v
                                    }
                                }*/
/*                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@AddPrescription,
                                    android.R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerLAb.adapter = adapter

                            binding.spinnerLAb.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {

//                                        if(i== binding.spinnerLAb.selectedItem){
//                                            view!!.setBackgroundColor(ContextCompat.getColor(this@AddPrescription,R.color.main_color));
//                                        } else {
//                                            view!!.setBackgroundColor(ContextCompat.getColor(this@AddPrescription,R.color.gray_text_color));
//                                        }
                                        if (Lab == "1") {
                                            setRecyclerDataLab(labTestList.result!![i].category.toString())
                                            // myToast(this@AddPrescription, "Added")
                                            binding.llayoutLab.visibility = View.VISIBLE
                                        }
                                        Lab = "1"
                                        Log.e(ContentValues.TAG, "mainSucCat: $sucCategory")
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }
                                }*/


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")

                    }
                }

                override fun onFailure(call: Call<ModelLabList>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")

                }

            })
    }

    private fun apiCallDigMainCat() {


        ApiClient.apiService.gemainCategory(doctorId)
            .enqueue(object : Callback<ModelMainCatList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMainCatList>, response: Response<ModelMainCatList>
                ) {
                    try {
                        mainCatList = response.body()!!;
                        if (mainCatList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(mainCatList.result!!.size)
                            var itemsNew: MutableList<SearchableItem> = ArrayList()

                            for (i in mainCatList.result!!.indices) {
                                items[i] = mainCatList.result!![i].departmentname
                                itemsNew.add(SearchableItem(mainCatList.result!![i].departmentname.toString(), mainCatList.result!![i].departid.toString()))

                            }

                            binding.spinnerDiagnosticsCategory.setOnClickListener {
                                SearchableMultiSelectSpinner.show(this@AddPrescription, "Select Items","Done", itemsNew, object :
                                    SelectionCompleteListener {
                                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                                        Log.e("data", selectedItems.toString())
                                        for (i in selectedItems) {
                                            binding.spinnerDiagnosticsCategory.text=i.text
                                            binding.llayoutMainCat.visibility = View.VISIBLE
                                            apiCallDigSubCat(i.code)
                                            setRecyclerDataMainCat(i.text,i.code)

                                            Log.e("chosenItems",i.text)
                                        }



                                    }

                                })
                            }
/*                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@AddPrescription,
                                    android.R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerDiagnosticsCategory.adapter = adapter

                            binding.spinnerDiagnosticsCategory.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {

                                        val departmentName = mainCatList.result!![i].departmentname
                                        val departid = mainCatList.result!![i].departid
                                        apiCallDigSubCat(departid.toString())


//                                        if (mainCatList.result!![i].departmentname == "Gynae & Obstetrics") {
//                                            binding.layoutSpinnerGynae.visibility = View.VISIBLE
//                                            binding.layoutSpinnerGeneral.visibility = View.GONE
//                                        }
//                                        if (mainCatList.result!![i].departmentname == "General Medicine") {
//                                            binding.layoutSpinnerGynae.visibility = View.GONE
//                                            binding.layoutSpinnerGeneral.visibility = View.VISIBLE
//                                        }

                                        Log.e(ContentValues.TAG, "departmentName: $departmentName")
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }
                                }*/


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")

                    }
                }

                override fun onFailure(call: Call<ModelMainCatList>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")

                }

            })
    }

    private fun apiCallDigSubCat(departid: String) {


        ApiClient.apiService.getsubCat(departid)
            .enqueue(object : Callback<ModelSubCatList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelSubCatList>, response: Response<ModelSubCatList>
                ) {
                    try {
                        diagSubCatList = response.body()!!;
                        if (diagSubCatList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(diagSubCatList.result!!.size)
                            var itemsNew: MutableList<SearchableItem> = ArrayList()

                            //  var listData: MutableList<ModelSubCatList> = itemsNew as MutableList<ModelSubCatList>

                            for (i in diagSubCatList.result!!.indices) {
                                items[i] = diagSubCatList.result!![i].name
                                 itemsNew.add(SearchableItem(diagSubCatList.result!![i].name.toString(), "$i"))

                            }
/*                            val selectedItem = -1

                            var adapter: ArrayAdapter<String?> =
                                object : ArrayAdapter<String?>(this@AddPrescription, android.R.layout.simple_list_item_1, items) {
                                    override fun getDropDownView(
                                        position: Int,
                                        convertView: View?,
                                        parent: ViewGroup
                                    ): View? {
                                        var v: View? = null
                                        v = super.getDropDownView(position, null, parent)
                                        // If this is the selected item position

                                        if(position== selectedItem){
                                            v.setBackgroundColor(ContextCompat.getColor(this@AddPrescription,R.color.main_color));
                                        } else {
                                            v.setBackgroundColor(ContextCompat.getColor(this@AddPrescription,R.color.gray_text_color));
                                        }
                                        if (position == selectedItem) {
                                            v.setBackgroundColor(Color.BLUE)
                                        } else {
                                            // for other views
                                            v.setBackgroundColor(Color.WHITE)
                                        }
                                        return v
                                    }
                                }*/
                        /*    val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@AddPrescription,
                                    android.R.layout.simple_list_item_1,
                                    items
                                )*/

                            binding.spinnerDiagnosticsSubCategory.setOnClickListener {
                                SearchableMultiSelectSpinner.show(this@AddPrescription, "Select Items","Done", itemsNew, object :
                                    SelectionCompleteListener {
                                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                                        Log.e("data", selectedItems.toString())
                                        for (i in selectedItems) {
                                             setRecyclerDataSub(i.text)
                                            binding.llayoutSubCat.visibility = View.VISIBLE

                                            Log.e("chosenItems",i.text)
                                        }



                                    }

                                })
                            }

/*
                            binding.spinnerDiagnosticsSubCategory.adapter = adapter
                            binding.spinnerDiagnosticsSubCategory.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {


                                        val name = diagSubCatList.result!![i].name
                                        if (t == "1") {
                                            setRecyclerDataSub(diagSubCatList.result!![i].name.toString())
                                            // myToast(this@AddPrescription, "Added")
                                            binding.llayoutSubCat.visibility = View.VISIBLE
                                        }
                                        t = "1"
                                        Log.e(ContentValues.TAG, "mainSucCat: $mainSucCat")
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }
                                }*/


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")

                    }
                }

                override fun onFailure(call: Call<ModelSubCatList>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")

                }

            })
    }

  //  val spinner = Spinner(this@AddPrescription)


//
//    val adapter = object: ArrayAdapter<MutableSet?>(this@AddPrescription, android.R.layout.simple_spinner_item, diagSubCatListGen) {
//        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//            return super.getDropDownView(position, convertView, parent).also { view ->
//                if(position ==  binding.spinnerDiagnosticsSubCategory.selectedItemPosition)
//                    view.setBackgroundColor(Color.rgb(204, 255, 255))
//            }
//        }
//    }
/*
    private fun apiCallDigSubCatGen() {

        ApiClient.apiService.getsubCat("68")
            .enqueue(object : Callback<ModelSubCatList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelSubCatList>, response: Response<ModelSubCatList>
                ) {
                    try {
                        diagSubCatListGen = response.body()!!;
                        if (diagSubCatListGen != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(diagSubCatListGen.result!!.size)

                            for (i in diagSubCatListGen.result!!.indices) {
                                items[i] = diagSubCatListGen.result!![i].name
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@AddPrescription,
                                    android.R.layout.simple_list_item_1,
                                    items
                                )

                            binding.spinnerDiagnosticsSubCategoryGen.adapter = adapter
                            binding.spinnerDiagnosticsSubCategoryGen.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    @SuppressLint("SuspiciousIndentation")
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {

                                        val name = diagSubCatListGen.result!![i].name
                                      //  if (t == "1") {
                                            setRecyclerDataSub(diagSubCatListGen.result!![i].name.toString())
                                            // myToast(this@AddPrescription, "Added")
                                            binding.llayoutSubCat.visibility = View.VISIBLE
                                       // }
                                      //  t = "1"
                                        Log.e(ContentValues.TAG, "mainSucCat: $mainSucCat")
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }
                                }


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")

                    }
                }

                override fun onFailure(call: Call<ModelSubCatList>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")

                }

            })
    }
*/



    private fun apiCallAddPrescription() {
        AppProgressBar.showLoaderDialog(this)

        var hypertension = ""
        var diabetesMellitus = ""
        var hypothyroid = ""
        var hyperthyroid = ""
        var heartDisease = ""
        var anyAllergic = ""
        var otherAllergic = ""

        hypertension = if (binding.edtHypertension.text!!.isNotEmpty()) {
            binding.edtHypertension.text.toString().trim()
        } else {
            "No"
        }

        if (binding.edtDiabetesMellitus.text!!.isNotEmpty()) {
            diabetesMellitus = binding.edtDiabetesMellitus.text.toString().trim()
        } else {
            diabetesMellitus = "No"
        }
        if (binding.edtHypothyroid.text!!.isNotEmpty()) {
            hypothyroid = binding.edtHypothyroid.text.toString().trim()
        } else {
            hypothyroid = "No"
        }

        if (binding.edtHyperthyroid.text!!.isNotEmpty()) {
            hyperthyroid = binding.edtHyperthyroid.text.toString().trim()
        } else {
            hyperthyroid = "No"
        }

        if (binding.edtHeartDisease.text!!.isNotEmpty()) {
            heartDisease = binding.edtHeartDisease.text.toString().trim()
        } else {
            heartDisease = "No"
        }

        if (binding.edtAnyAllergic.text!!.isNotEmpty()) {
            anyAllergic = binding.edtAnyAllergic.text.toString().trim()
        } else {
            anyAllergic = "No"
        }

        if (binding.edtOtherAllergic.text!!.isNotEmpty()) {
            otherAllergic = binding.edtOtherAllergic.text.toString().trim()
        } else {
            otherAllergic = "No"
        }


        var medicine = arrayListOf("")
        var labTest = arrayListOf("")
        var fraquency = arrayListOf("")
        var days = arrayListOf("")
        var instraction = arrayListOf("")
        var doasge = arrayListOf("")
        var subCatList = arrayListOf("")
        var mainCategoryNew = arrayListOf("")


        for (i in mainCategory) {
            mainCategoryNew.add(i.id)
        }
        for (i in sucCategory) {
            subCatList.add(i.subCatName)
        }

        for (i in medicineList) {
            medicine.add(i.medicineName)
        }

        for (i in labListNew) {
            labTest.add(i.testName)
        }

        for (i in medicineList) {
            fraquency.add(i.frequency)
            days.add(i.days)
            instraction.add(i.instraction)
            doasge.add(i.dosage)
        }

        Log.e("medicine", medicine.toString())
        Log.e("labTest", labTest.toString())
        Log.e("fraquency", fraquency.toString())
        Log.e("days", days.toString())
        Log.e("instraction", instraction.toString())
        Log.e("doasge", doasge.toString())

        val currentTime = SimpleDateFormat("dd-MM-yyyy HH:m:ss", Locale.getDefault()).format(Date())

        val a = "_"
        ApiClient.apiService.addPrescr(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            "$studenrId$a$id",
            doctorId,
            "",
            id,
            "",
            birthdate,
            date,
            "",
            bloodgroup,
            sex,
            "Adilabad",
            schl,
            "",
            schl_addr,
            patientName,
            "",
            "",
            "",
            schl_addr,
            "",
            "",
            strts,
            followDate,
            referralHos,
            binding.edtTypeHealthIssue.text.toString(),
            binding.edtTypeDepartment.text.toString(),
            "",
            "Provisional",
            "",
            "",
            "admin",
            "",
            binding.edtNote.text.toString(),
            "",
            "",
            hypertension,
            "",
            diabetesMellitus,
            hypothyroid,
            hyperthyroid,
            "",
            heartDisease,
            anyAllergic,
            "",
            otherAllergic,
            mainCategoryNew,
            currentTime,
            subCatList,
            labTest,
            medicine,
            medicine,
            "",
            fraquency,
            days,
            instraction,
            doasge,

            ).enqueue(object : Callback<ModelUpload> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelUpload>, response: Response<ModelUpload>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@AddPrescription, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@AddPrescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(this@AddPrescription, "prescription Added")
                        startActivity(Intent(this@AddPrescription, TodaysAppointment::class.java))
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@AddPrescription, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@AddPrescription, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                myToast(this@AddPrescription, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
        //}
    }

    private fun apiCallEditPrescription() {
        AppProgressBar.showLoaderDialog(this)

        var hypertension = ""
        var diabetesMellitus = ""
        var hypothyroid = ""
        var hyperthyroid = ""
        var heartDisease = ""
        var anyAllergic = ""
        var otherAllergic = ""

        hypertension = if (binding.edtHypertension.text!!.isNotEmpty()) {
            binding.edtHypertension.text.toString().trim()
        } else {
            "No"
        }

        if (binding.edtDiabetesMellitus.text!!.isNotEmpty()) {
            diabetesMellitus = binding.edtDiabetesMellitus.text.toString().trim()
        } else {
            diabetesMellitus = "No"
        }
        if (binding.edtHypothyroid.text!!.isNotEmpty()) {
            hypothyroid = binding.edtHypothyroid.text.toString().trim()
        } else {
            hypothyroid = "No"
        }

        if (binding.edtHyperthyroid.text!!.isNotEmpty()) {
            hyperthyroid = binding.edtHyperthyroid.text.toString().trim()
        } else {
            hyperthyroid = "No"
        }

        if (binding.edtHeartDisease.text!!.isNotEmpty()) {
            heartDisease = binding.edtHeartDisease.text.toString().trim()
        } else {
            heartDisease = "No"
        }

        if (binding.edtAnyAllergic.text!!.isNotEmpty()) {
            anyAllergic = binding.edtAnyAllergic.text.toString().trim()
        } else {
            anyAllergic = "No"
        }

        if (binding.edtOtherAllergic.text!!.isNotEmpty()) {
            otherAllergic = binding.edtOtherAllergic.text.toString().trim()
        } else {
            otherAllergic = "No"
        }


        var medicine = arrayListOf("")
        var labTest = arrayListOf("")
        var fraquency = arrayListOf("")
        var days = arrayListOf("")
        var instraction = arrayListOf("")
        var doasge = arrayListOf("")
        var subCatList = arrayListOf("")
        var mainCategoryNew = arrayListOf("")


        for (i in mainCategory) {
            mainCategoryNew.add(i.id)
        }
        for (i in sucCategory) {
            subCatList.add(i.subCatName)
        }

        for (i in medicineList) {
            medicine.add(i.medicineName)
        }

        for (i in labListNew) {
            labTest.add(i.testName)
        }

        for (i in medicineList) {
            fraquency.add(i.frequency)
            days.add(i.days)
            instraction.add(i.instraction)
            doasge.add(i.dosage)
        }

        Log.e("medicine", medicine.toString())
        Log.e("labTest", labTest.toString())
        Log.e("fraquency", fraquency.toString())
        Log.e("days", days.toString())
        Log.e("instraction", instraction.toString())
        Log.e("doasge", doasge.toString())

        val currentTime = SimpleDateFormat("dd-MM-yyyy HH:m:ss", Locale.getDefault()).format(Date())

        val a = "_"
        ApiClient.apiService.editPrescr(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            "$studenrId$a$id",
            doctorId,
            id,
            "",
            id,
            "",
            birthdate,
            date,
            "",
            bloodgroup,
            sex,
            "Adilabad",
            schl,
            "",
            schl_addr,
            patientName,
            "",
            "",
            "",
            schl_addr,
            "",
            "",
            strts,
            followDate,
            referralHos,
            binding.edtTypeHealthIssue.text.toString(),
            binding.edtTypeDepartment.text.toString(),
            "",
            "Provisional",
            "",
            "",
            "admin",
            "",
            binding.edtNote.text.toString(),
            "",
            "",
            hypertension,
            "",
            diabetesMellitus,
            hypothyroid,
            hyperthyroid,
            "",
            heartDisease,
            anyAllergic,
            "",
            otherAllergic,
            mainCategoryNew,
            currentTime,
            subCatList,
            labTest,
            medicine,
            medicine,
            "",
            fraquency,
            days,
            instraction,
            doasge,

            ).enqueue(object : Callback<ModelUpload> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelUpload>, response: Response<ModelUpload>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@AddPrescription, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@AddPrescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(this@AddPrescription, "prescription Added")
                        startActivity(Intent(this@AddPrescription, TodaysAppointment::class.java))
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@AddPrescription, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@AddPrescription, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                myToast(this@AddPrescription, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
        //}
    }


}
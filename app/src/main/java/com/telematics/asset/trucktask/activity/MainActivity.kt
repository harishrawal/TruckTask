package com.telematics.asset.trucktask.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assettelematics.utils.Status
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.telematics.asset.trucktask.R
import com.telematics.asset.trucktask.adapter.*
import com.telematics.asset.trucktask.databinding.ActivityMainBinding
import com.telematics.asset.trucktask.model.*
import com.telematics.asset.trucktask.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var rcvAdapter: CustomAdapter
    private var vehicleTypeList = arrayListOf<VehicleType>()
    private lateinit var binding: ActivityMainBinding
    private var isMore = false
    private var vehicleMake = ""
    private var vehicleModel = ""
    private var vehicleMfgYear = ""
    private var vehicleFuelType = ""
    private var vehicleCapacity = ""
    private var selectedVehicleType = ""
    private var ownerShip = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupImeiNumberInputs()

        binding.IvScan.setOnClickListener {
            clearExistingIMEIData()
            scanCode()
        }


        binding.IvMore.setOnClickListener {
            if (isMore) {
                isMore = false
                binding.IvMore.setImageResource(R.drawable.less)
                binding.tvMoreLess.setText(R.string.less)
                if (vehicleTypeList.isNotEmpty()) {
                    rcvAdapter.updateData(vehicleTypeList)
                }
            } else {
                if (vehicleTypeList.isNotEmpty()) {
                    setupLessRCVData(vehicleTypeList)
                }
            }
        }

        viewModel.callVehicleDataApi()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        rcvAdapter =
            CustomAdapter(vehicleTypeList as ArrayList<VehicleType>) { vehicleType, position ->
                selectedVehicleType = vehicleType.text
            }
        binding.recyclerView.adapter = rcvAdapter


        binding.reload.setOnClickListener {
            resetAllValues()
        }

        binding.btnAdd.setOnClickListener {
            addVehicleData()
        }

        binding.radioBtnOwn.isChecked = true
        binding.radioGroug.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            ownerShip = radioButton.text.toString()
        }

        setObserver()
    }

    private fun setObserver() {
        /*viewModel.liveVehicleDataResponse.observe(this, Observer { vehicleDataResponse ->
            vehicleDataResponse?.let {
                //loadData(it)}
        })*/

        viewModel.liveVehicleDataResponse.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                  //  progressBar.visibility = View.GONE
                 //   recyclerView.visibility = View.VISIBLE
                    it.data?.let { loadData(it)
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
                Status.LOADING -> {
                  //  progressBar.visibility = View.VISIBLE
                  //  recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                  //  progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupImeiNumberInputs() {

        binding.etCOne.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.etCTwo.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etCTwo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.etCThree.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etCThree.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.etCFour.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etCFour.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.etCFive.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etCFive.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.etCSix.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.captureActivity = CaptureActivity::class.java
        qrCodeLauncher.launch(options)
    }

    private var qrCodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Result")

            builder.setMessage(result.contents)
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()

                    if (result.contents.length >= 6) {
                        val lastSixChars = result.contents.toString().takeLast(6)
                        if (isNumeric(lastSixChars)) {
                            binding.etImei.setText(result.contents)
                            val ls = result.contents.toString()
                            if (ls.length >= 6) {
                                binding.etCSix.setText(ls[ls.length - 1].toString())
                                binding.etCFive.setText(ls[ls.length - 2].toString())
                                binding.etCFour.setText(ls[ls.length - 3].toString())
                                binding.etCThree.setText(ls[ls.length - 4].toString())
                                binding.etCTwo.setText(ls[ls.length - 5].toString())
                                binding.etCOne.setText(ls[ls.length - 6].toString())
                            }
                        }
                    }
                })
                .show()
        }
    }

    private fun isNumeric(input: String): Boolean {
        return try {
            input.toLong()
            true // It's a number
        } catch (e: NumberFormatException) {
            false // It's not a number
        }
    }

    private fun clearExistingIMEIData() {
        binding.etImei.setText("")
        binding.etCOne.setText("")
        binding.etCTwo.setText("")
        binding.etCThree.setText("")
        binding.etCFour.setText("")
        binding.etCFive.setText("")
        binding.etCSix.setText("")
    }


    private fun loadData(data: VehicleDataResponse) {

        try {
            if (data.fuelType.isNotEmpty()) {
                setupFuelTypeSpinner(data.fuelType)
            }

            if (data.vehicleMake.isNotEmpty()) {
                setupVehicleMakeSpinner(data.vehicleMake)
            }

            if (data.vehicleCapacity.isNotEmpty()) {
                setupVehicleCapacitySpinner(data.vehicleCapacity)
            }

            if (data.manufactureYear.isNotEmpty()) {
                setupVehicleMFYearSpinner(data.manufactureYear)
            }

            if (data.vehicleType.isNotEmpty()) {
                vehicleTypeList.clear()
                vehicleTypeList = data.vehicleType as ArrayList<VehicleType>
                setupLessRCVData(data.vehicleType)
            }

            setupVehicleNameSpinner()
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }

    }

    private fun setupLessRCVData(vehicleTypeList: ArrayList<VehicleType>) {

        try {
            isMore = true
            binding.IvMore.setImageResource(R.drawable.ic_add)
            binding.tvMoreLess.setText(R.string.more)
            if (vehicleTypeList.isNotEmpty()) {
                val sublist = vehicleTypeList.subList(0, 2)
                val sList = arrayListOf<VehicleType>()
                for (item in sublist) {
                    sList.add(item)
                }
                rcvAdapter.updateData(sList)
            }
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }


    private fun setupFuelTypeSpinner(fuelDataList: List<FuelType>) {
        try {
            val adapter = FuelTypeSpinnerAdapter(this, fuelDataList)
            binding.fuelTypeSpinner.adapter = adapter

            binding.fuelTypeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        vehicleFuelType = parent!!.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }

    private fun setupVehicleMakeSpinner(vehicleMakeDataList: List<VehicleMake>) {
        try {
            val adapter = VehicleMakeSpinnerAdapter(this, vehicleMakeDataList)
            binding.vehicleMakeSpinner.adapter = adapter

            binding.vehicleMakeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        vehicleMake = parent!!.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }

    private fun setupVehicleMFYearSpinner(mfyList: List<ManufactureYear>) {
        try {
            val adapter = VehicleYearMFSpinnerAdapter(this, mfyList)
            binding.vehicleYearSpinner.adapter = adapter

            binding.vehicleYearSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        vehicleMfgYear = parent!!.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }

    private fun setupVehicleCapacitySpinner(capacityList: List<VehicleCapacity>) {
        try {
            val adapter = VehicleCapacitySpinnerAdapter(this, capacityList)
            binding.vehicleCapacitySpinner.adapter = adapter

            binding.vehicleCapacitySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        vehicleCapacity = parent!!.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }

    private fun setupVehicleNameSpinner() {
        try {
            val nameList = viewModel.getVehicleNameData()
            val adapter = VehicleNameSpinnerAdapter(this, nameList)
            binding.vehicleModelSpinner.adapter = adapter

            binding.vehicleModelSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        vehicleModel = parent!!.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }

    private fun resetAllValues() {
        try {
            clearExistingIMEIData()
            binding.etImei.setText("")
            binding.etTagName.setText("")
            binding.etRegNumber.setText("")
            binding.etDriver.setText("")
            binding.vehicleMakeSpinner.setSelection(0)
            binding.vehicleModelSpinner.setSelection(0)
            binding.vehicleYearSpinner.setSelection(0)
            binding.fuelTypeSpinner.setSelection(0)
            binding.vehicleCapacitySpinner.setSelection(0)
            binding.radioBtnOwn.isChecked = true
        } catch (e: Exception) {
            Log.i("Exception===>", "${e.message}")
        }
    }

    private fun addVehicleData() {

        if (!validateSixDigitImei()) {
            return
        }
        if (binding.etImei.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter IMEI Number", Toast.LENGTH_SHORT).show()
            binding.etImei.requestFocus()
            return
        }
        if (binding.etTagName.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter TagName", Toast.LENGTH_SHORT).show()
            binding.etTagName.requestFocus()
            return
        }
        if (binding.etRegNumber.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Registration Number", Toast.LENGTH_SHORT).show()
            binding.etRegNumber.requestFocus()
            return
        }
        if (binding.etDriver.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Driver Name", Toast.LENGTH_SHORT).show()
            binding.etDriver.requestFocus()
            return
        }

    }

    fun validateSixDigitImei(): Boolean {

        return if (binding.etCOne.text.toString().trim().isEmpty() ||
            binding.etCTwo.text.toString().trim().isEmpty() ||
            binding.etCThree.text.toString().trim().isEmpty() ||
            binding.etCFour.text.toString().trim().isEmpty() ||
            binding.etCFive.text.toString().trim().isEmpty() ||
            binding.etCSix.text.toString().trim().isEmpty()

        ) {
            Toast.makeText(this, "Please Enter 6 Digit IMEI field", Toast.LENGTH_SHORT).show()
            false

        } else {
            true
        }

    }


    /*private fun fetchDataFromJsonFile(): VehicleDataResponse {
        var json: String? = null
        val inputStream: InputStream = assets.open("VehicleData.json")
        json = inputStream.bufferedReader().use { it.readText() }.toString()

        val data = Gson().fromJson(json, VehicleDataResponse::class.java)
        Log.i("dataResponse===>", "$data")
        return data
    }*/
}
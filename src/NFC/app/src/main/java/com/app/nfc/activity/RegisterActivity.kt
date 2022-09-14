package com.app.nfc.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.*
import com.app.nfc.R
import com.app.nfc.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.bson.types.ObjectId


class RegisterActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var dinerQuantity: Int = 0
    private var diningMethod: String = ""
    private val dinerNumbers = ArrayList<String>()

    companion object {
        var dinerName: String = ""
        var dinerPhone: String = ""
        var registeredTableNo: String = ""
        val orderObjectId: ObjectId = ObjectId()
    }

    override fun setLayout(): Int {
        return R.layout.activity_register
    }

    @SuppressLint("ShowToast")
    override fun InitView() {
        supportActionBar!!.hide()

        // Specify table number
        specifyTableNo()

        // Creating the ArrayAdapter instance having the dinerNumbers list
        val tableCapacity = com.app.nfc.model.getTableCapacity(registeredTableNo)
        if (tableCapacity == 0) {
            dinerNumbers.add("999")
        }
        for (i in 0 until tableCapacity) {
            dinerNumbers.add((i + 1).toString())
        }
        val spinnerAdapter = ArrayAdapter<Any?>(
            this, android.R.layout.simple_spinner_item,
            dinerNumbers as List<Any?>
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Setting the ArrayAdapter data on the Spinner
        dinerQuantitySpinner.adapter = spinnerAdapter
        dinerQuantitySpinner.onItemSelectedListener = this


        // Submit-button listener
        if (com.app.nfc.model.getTableStatus(registeredTableNo) == "close") {
            tvSubmit.setBackgroundColor(Color.GRAY)
            tvSubmit.setOnClickListener {
                val toast = Toast.makeText(
                    applicationContext,
                    "Sorry! The table is now closed.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        } else {
            tvSubmit.setOnClickListener {
                specifyDinerInfo()
                com.app.nfc.model.setTableStatus(registeredTableNo, "seated")
                com.app.nfc.model.openOrder(registeredTableNo, diningMethod, dinerQuantity, dinerName)
                openActivity(HomeActivity::class.java)
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinner: Spinner = findViewById(R.id.dinerQuantitySpinner)
        dinerQuantity = spinner.selectedItem.toString().toInt()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun onRadioButtonClicked(view: View?) {
        val checked = (view as RadioButton).isChecked

        when (view.getId()) {
            R.id.radioEatIn -> if (checked) diningMethod = "eat-in"
            R.id.radioTakeAway -> if (checked) diningMethod = "take-away"
        }
    }

    private fun specifyDinerInfo() {
        val edYourName = findViewById<EditText>(R.id.edYourName)
        val edPhone = findViewById<EditText>(R.id.edPhone)
        dinerName = edYourName.text.toString()
        dinerPhone = edPhone.text.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun specifyTableNo() {
        val intent = super.getIntent()
        if (intent.hasExtra("registeredTableNo")) {
            registeredTableNo = intent.getStringExtra("registeredTableNo").toString()
            val registrationTableNoTextView: TextView = findViewById(R.id.registrationTableNoTextView)
            registrationTableNoTextView.text = getString(R.string.you_are_registering_for) + " " + registeredTableNo
        }
    }
}
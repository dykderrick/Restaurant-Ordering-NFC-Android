package com.app.nfc.activity

import android.widget.TextView
import com.app.nfc.R
import com.app.nfc.base.BaseActivity
import kotlin.system.exitProcess

class PaymentResultActivity : BaseActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_payment_result
    }

    override fun InitView() {
        val tvExit = findViewById<TextView>(R.id.tvExit)
        tvExit.setOnClickListener {
            exitProcess(0)
        }
    }
}
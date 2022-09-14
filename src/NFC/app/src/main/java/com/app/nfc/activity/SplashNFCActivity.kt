package com.app.nfc.activity

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.app.nfc.R
import com.app.nfc.base.BaseActivity
import java.nio.charset.StandardCharsets

class SplashNFCActivity : BaseActivity() {

    override fun setLayout(): Int {
        return R.layout.activity_splash
    }

    override fun InitView() {
        //super.registeredTableNo = extractTableNo()

        supportActionBar!!.hide()

        Handler().postDelayed({
            val registeredTableNo = "registeredTableNo"
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra(registeredTableNo, extractTableNo())

            //openActivity(RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

//        -----------------------------------------------------------------------------for tran parent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    private fun extractTableNo(): String {
        val intent: Intent = this.intent
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                return String(messages[0].records[0].payload, StandardCharsets.UTF_8).substring(17)
            }
        }
        return ""
    }
}
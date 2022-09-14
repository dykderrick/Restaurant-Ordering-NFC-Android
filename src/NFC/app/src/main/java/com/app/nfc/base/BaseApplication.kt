package com.app.nfc.base

import android.app.Application
import androidx.multidex.MultiDex
import com.app.nfc.BuildConfig
import com.app.nfc.R
import com.app.nfc.activity.MyOrderActivity
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class BaseApplication : Application() {
    private fun initPayPalEnv() {
        val config = CheckoutConfig(
            application = this,
            clientId = getString(R.string.paypal_client_id),
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.GBP,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)
    }

    private fun initCalligraphy() {
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SF-PRO-DISPLAY-REGULAR.OTF")
                .setFontAttrId(R.attr.fontPath)
                .disableCustomViewInflation()
                .build()
        )
    }

    override fun onCreate() {
        super.onCreate()

        initPayPalEnv()
        initCalligraphy()
        MultiDex.install(this)
    }
}
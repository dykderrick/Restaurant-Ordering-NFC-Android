package com.app.nfc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.nfc.R
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PayPalButton

class PayPalCheckout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paypal_checkout)

        setupPayPalButton()
    }

    private fun setupPayPalButton() {
        val payPalButton = findViewById<PayPalButton>(R.id.payPalButton)
        payPalButton.setup(
            createOrder = CreateOrder { createOrderActions ->
                val order = Order(
                    intent = OrderIntent.CAPTURE,
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            description = "NFC Restaurant Order #${RegisterActivity.orderObjectId.toString()}",
                            amount = Amount(
                                currencyCode = CurrencyCode.GBP,
                                value = intent.getStringExtra("Money")!!
                            )
                        )
                    )
                )

                createOrderActions.create(order)
            },
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                }

                // Set the order finished
                com.app.nfc.model.setOrderFinished(RegisterActivity.orderObjectId)

                // Set the table finished
                com.app.nfc.model.setTableStatus(RegisterActivity.registeredTableNo, "finished")

                // Start the last activity
                startActivity(Intent(this, PaymentResultActivity::class.java))

            },

            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },

            onError = OnError {errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }
        )
    }
}
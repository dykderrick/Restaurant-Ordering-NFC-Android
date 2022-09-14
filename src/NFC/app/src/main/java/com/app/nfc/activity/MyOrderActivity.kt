package com.app.nfc.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nfc.R
import com.app.nfc.adapter.listFoodMyOrderAdapter
import com.app.nfc.base.BaseActivity
import com.app.nfc.model.PromoCode
import kotlinx.android.synthetic.main.activity_cart.ivBack
import kotlinx.android.synthetic.main.activity_myorder.*
import kotlinx.android.synthetic.main.activity_myorder.lyOrderBY
import kotlinx.android.synthetic.main.dlg_bottom.*
import java.util.*


class MyOrderActivity : BaseActivity() {

    override fun setLayout(): Int {
        return R.layout.activity_myorder
    }


    override fun InitView() {
        supportActionBar!!.hide()

        rcMyOrderList.adapter = listFoodMyOrderAdapter(this, HomeActivity.cartArrayList)
        rcMyOrderList.layoutManager = LinearLayoutManager(this)
        ivBack.setOnClickListener {
            finish()
        }

        lyOrderBY.setOnClickListener {
            showBottomDialog()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)



        val window = dialog.window
        val wlp = window!!.attributes
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dlg_bottom)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(object : ColorDrawable(Color.TRANSPARENT) {})


        val lyOrderBY = dialog.findViewById<LinearLayout>(R.id.lyOrderBY)
        val lyPromoCode = dialog.findViewById<LinearLayout>(R.id.lyPromoCode)
        val lyPromoDiscount = dialog.findViewById<LinearLayout>(R.id.lyPromoDiscount)

        val tvSubmit = dialog.findViewById<TextView>(R.id.tvSubmit)
        val tvSubTotal = dialog.findViewById<TextView>(R.id.tvSubTotal)
        val tvTaxes = dialog.findViewById<TextView>(R.id.tvTaxes)
        val tvTotalMoney = dialog.findViewById<TextView>(R.id.tvTotalMoney)


        lyPromoCode.visibility = View.GONE
        lyPromoDiscount.visibility = View.GONE


        val orderMoney = String.format("%.2f", com.app.nfc.model.computeSubTotalMoney()).toDouble()
        val taxes = String.format("%.2f", orderMoney * 0.05).toDouble()  // TODO: In further versions, change it to user setting
        val totalMoney = String.format("%.2f", orderMoney + taxes).toDouble()

        tvSubmit.text = getString(R.string.next)
        tvSubTotal.text = getString(R.string.pound) + orderMoney.toString()
        tvTaxes.text = getString(R.string.pound) + taxes.toString()
        tvTotalMoney.text = getString(R.string.pound) + totalMoney.toString()

        lyOrderBY.setOnClickListener {
            showBottomDialogPromoCode()
            dialog.dismiss()
        }

        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun showBottomDialogPromoCode() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val window = dialog.window
        val wlp = window!!.attributes
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dlg_bottom)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(object : ColorDrawable(Color.TRANSPARENT) {})

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val lyOrderBY = dialog.findViewById<LinearLayout>(R.id.lyOrderBY)

        val lyPromoDiscount = dialog.findViewById<LinearLayout>(R.id.lyPromoDiscount)
        lyPromoDiscount.visibility = View.GONE  // No Apply, no visible

        val edPromoCode = dialog.findViewById<EditText>(R.id.edPromoCode)


        val tvSubmit = dialog.findViewById<TextView>(R.id.tvSubmit)
        val tvSubTotal = dialog.findViewById<TextView>(R.id.tvSubTotal)
        val tvTaxes = dialog.findViewById<TextView>(R.id.tvTaxes)
        val tvPromoDiscount = dialog.findViewById<TextView>(R.id.tvPromoDiscount)
        val tvTotalMoney = dialog.findViewById<TextView>(R.id.tvTotalMoney)
        val tvApply = dialog.findViewById<TextView>(R.id.tvApply)

        val orderMoney = String.format("%.2f", com.app.nfc.model.computeSubTotalMoney()).toDouble()
        val taxes = String.format("%.2f", orderMoney * 0.05).toDouble()  // TODO: In further versions, change it to user setting
        var totalMoney = String.format("%.2f", orderMoney + taxes).toDouble()

        tvApply.setOnClickListener {
            val discountMoney = com.app.nfc.model.getPromoCodeDiscount(
                orderMoney + taxes, edPromoCode.text.toString())
            totalMoney = if (discountMoney < orderMoney + taxes) {
                String.format("%.2f", orderMoney + taxes - discountMoney).toDouble()
            } else 0.0

            if (discountMoney != 0.0) {
                lyPromoDiscount.visibility = View.VISIBLE
                tvPromoDiscount.text = getString(R.string.pound) + discountMoney
                tvTotalMoney.text = getString(R.string.pound) + totalMoney
            }
        }

        tvSubTotal.text = getString(R.string.pound) + orderMoney.toString()
        tvTaxes.text = getString(R.string.pound) + taxes.toString()
        tvTotalMoney.text = getString(R.string.pound) + totalMoney.toString()
        lyOrderBY.setOnClickListener {
            dialog.dismiss()
            val newIntent = Intent(this, PayPalCheckout::class.java)
                .putExtra("Money", totalMoney.toString())
            startActivity(newIntent)
        }
        dialog.show()

    }

    private fun getURLForResource(resourceId: Int): String? {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse(
            "android.resource://" + R::class.java.getPackage().name + "/" + resourceId
        ).toString()
    }

}
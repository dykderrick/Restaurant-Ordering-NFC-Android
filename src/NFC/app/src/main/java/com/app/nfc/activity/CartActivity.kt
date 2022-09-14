package com.app.nfc.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nfc.R
import com.app.nfc.adapter.listCartFoodAdapter
import com.app.nfc.base.BaseActivity
import com.app.nfc.model.Dish
import com.app.nfc.model.DishInOrder
import com.app.nfc.model.FoodModel
import com.app.nfc.model.Order
import com.app.nfc.utils.Common
import io.realm.RealmList
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_home.*

class CartActivity : BaseActivity() {

    override fun setLayout(): Int {
        return R.layout.activity_cart
    }

    var TAG = "CartActivity"

    override fun InitView() {
        supportActionBar!!.hide()

//        addingData(listobject)

        Common.printLog(TAG, "---------cartArrayList------" + HomeActivity.cartArrayList.size)
        if (HomeActivity.cartArrayList.size <= 0) {
            tvNoDataFound.visibility = View.VISIBLE
            rcCart.visibility = View.GONE
            lyOrderBY.visibility = View.GONE
        } else {
            tvNoDataFound.visibility = View.GONE
            rcCart.visibility = View.VISIBLE
            lyOrderBY.visibility = View.VISIBLE
        }

        rcCart.adapter = listCartFoodAdapter(this, HomeActivity.cartArrayList)
        rcCart.layoutManager = LinearLayoutManager(this)
        ivBack.setOnClickListener {
            finish()
        }

        // Order placed!
        lyOrderBY.setOnClickListener {
            com.app.nfc.model.setTableStatus(RegisterActivity.registeredTableNo, "ordered")
            addDishToDatabaseOrder()
            openActivity(MyOrderActivity::class.java)
        }

        registerReceiver(mMessageReceiver, IntentFilter("ArraySize"))
    }

    private var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val value = intent.getIntExtra("item", 0)

//            val size = cart_size.text
            if (value > 0) {
                tvNoDataFound.visibility = View.GONE
                rcCart.visibility = View.VISIBLE
                lyOrderBY.visibility = View.VISIBLE
            } else {

                tvNoDataFound.visibility = View.VISIBLE
                rcCart.visibility = View.GONE
                lyOrderBY.visibility = View.GONE

            }


            Common.printLog(TAG, "-----------mMessageReceiver-----------${HomeActivity.value}")

        }
    }

    private fun addDishToDatabaseOrder() {
        HomeActivity.cartHashMap.keys.forEach { foodModelKey ->
            com.app.nfc.model.addDishToOrder(
                RegisterActivity.orderObjectId,
                com.app.nfc.model.getDishIdByName(foodModelKey.foodName, SplashActivity.backgroundThreadRealm),
                HomeActivity.cartHashMap.getValue(foodModelKey)
            )
        }
    }

    private fun getURLForResource(resourceId: Int): String? {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + resourceId)
            .toString()
    }
}
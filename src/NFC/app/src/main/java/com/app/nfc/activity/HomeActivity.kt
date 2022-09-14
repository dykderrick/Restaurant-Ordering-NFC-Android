package com.app.nfc.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nfc.R
import com.app.nfc.adapter.listFoodAdapter
import com.app.nfc.base.BaseActivity
import com.app.nfc.model.Dish
import com.app.nfc.model.FoodModel
import com.app.nfc.utils.Common
import com.app.nfc.utils.DataTransferInterface
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), DataTransferInterface {
    private val listobject = ArrayList<FoodModel>()
    var TAG = "HomeActivity"

    companion object {
        var value: Int = 0
        var cartArrayList = ArrayList<FoodModel>()
        var cartHashMap = HashMap<FoodModel, Int>()
    }

    override fun setLayout(): Int {
        return R.layout.activity_home
    }

    @SuppressLint("SetTextI18n")
    override fun InitView() {
        supportActionBar!!.hide()
        addingData(listobject)

        val tvHi = findViewById<TextView>(R.id.tvHi)
        tvHi.text = getString(R.string.hi) + RegisterActivity.dinerName

        rvFoodColl.adapter = listFoodAdapter(this, listobject)
        rvFoodColl.layoutManager = LinearLayoutManager(this)

        ivCart.setOnClickListener {
            openActivity(CartActivity::class.java)
        }

        registerReceiver(mMessageReceiver, IntentFilter("KeyData"))

        Common.printLog(TAG, "value$value")

        value = cartArrayList.size
        if (value > 0) {
            tvCartSize.visibility = View.VISIBLE
            tvCartSize.text = value.toString()
        }

    }

    private var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            value = intent.getIntExtra("item", 0)

//            val size = cart_size.text
            val num: Int = Integer.valueOf(tvCartSize.text.toString())
            if (value > 0) {
                tvCartSize.visibility = View.VISIBLE
                tvCartSize.text = (value).toString()
            }


            Common.printLog(TAG, "-----------mMessageReceiver-----------$value")

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addingData(foodArrayList: MutableList<FoodModel>) {

        lateinit var dishesData: RealmResults<Dish>
        SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
            dishesData = realm.where(Dish::class.java).findAll()
        }

        dishesData.forEachIndexed { index, dishData ->
            val imageUrl = getURLForResource(R.drawable.food_1)
            foodArrayList.add(
                FoodModel(
                    index + 1, dishData.name.toString(),
                    dishData.description.toString(),
                    dishData.price.toString(),
                    getFoodImageResource(dishData.name.toString()),
                    "4", "1", dishData.ETP.toString()
                )
            )
        }
    }

    private fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse(
            "android.resource://" + R::class.java.getPackage().name + "/" + resourceId
        ).toString()
    }

    override fun onResume() {
        super.onResume()

        value = cartArrayList.size
        if (value > 0) {
            tvCartSize.visibility = View.VISIBLE
            tvCartSize.text = (value).toString()
        } else {
            tvCartSize.visibility = View.GONE
        }
    }

    override fun onSetValues(al: ArrayList<FoodModel>?) {
//      value = al!!.size
    }

    private fun Context.getFoodImageResource(dishName: String): Int {
        var dishId = ""
        SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
            dishId = realm.where(Dish::class.java).equalTo("name", dishName).findFirst()?._id.toString()
        }

        val imageName = dishId.replaceFirst("6", "_")

        return resources.getIdentifier(imageName, "drawable", packageName)
    }
}
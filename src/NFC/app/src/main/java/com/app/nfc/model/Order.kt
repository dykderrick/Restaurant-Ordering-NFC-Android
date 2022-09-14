package com.app.nfc.model

import com.app.nfc.activity.HomeActivity
import com.app.nfc.activity.RegisterActivity
import com.app.nfc.activity.SplashActivity
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId
import java.net.URL
import java.util.*

@RealmClass(name = "orders")
open class Order : RealmObject {
    @PrimaryKey
    var _id: ObjectId? = null
    var createdTime: Date? = null
    var tableNo: String? = null
    var dishes: RealmList<DishInOrder>? = null
    var diningOption: String? = null
    var isFinished: Boolean = false
    var customerInfo: Customer? = null

    constructor(
        _id: ObjectId?,
        createdTime: Date?,
        tableNo: String,
        diningOption: String,
        dinerQuantity: Int,
        dinerName: String,
        isFinished: Boolean
    ) {
        this._id = _id
        this.createdTime = createdTime
        this.tableNo = tableNo
        this.dishes = RealmList()
        this.diningOption = diningOption
        this.isFinished = isFinished
        this.customerInfo = Customer(dinerName, dinerQuantity, "")  // TODO: Change customer
    }

    constructor() {}

}

fun openOrder(tableNo: String, diningMethod: String, dinerQuantity: Int, dinerName: String) {
    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        val order = Order(
            RegisterActivity.orderObjectId,
            Calendar.getInstance().time,
            tableNo,
            diningMethod,
            dinerQuantity,
            dinerName,
            false
        )
        realm.insert(order)
    }
}

fun getDishStatus(orderId: ObjectId, dishName: String): String {
    var dishStatus = ""

    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        lateinit var dishId: ObjectId

        // id-name pattern match
        val allDishes = realm.where(Dish::class.java).findAll()
        allDishes.forEach { theDish ->
            if (theDish.name == dishName) {
                dishId = theDish._id!!
            }
        }

        val order = realm.where(Order::class.java).equalTo("_id", orderId).findFirst()
        order?.dishes?.forEach { orderDish ->
            if (orderDish.dishID == dishId) {
                dishStatus = orderDish.status.toString()
            }
        }
    }

    return dishStatus
}

fun computeSubTotalMoney(): Double {
    var totalMoney = 0.0
    HomeActivity.cartHashMap.keys.forEach { foodModelKey ->
        totalMoney +=
            foodModelKey.foodPrice.toDouble() * HomeActivity.cartHashMap.getValue(foodModelKey)
    }

    return totalMoney
}

fun addDishToOrder(orderId: ObjectId, dishId: ObjectId, quantity: Int) {
    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        val order = realm.where(Order::class.java).equalTo("_id", orderId).findFirst()

        order?.dishes!!.add(DishInOrder(dishId, quantity, "preparing"))
    }
}

fun setOrderFinished(orderId: ObjectId) {
    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        val order = realm.where(Order::class.java).equalTo("_id", orderId).findFirst()
        order!!.isFinished = true
    }
}
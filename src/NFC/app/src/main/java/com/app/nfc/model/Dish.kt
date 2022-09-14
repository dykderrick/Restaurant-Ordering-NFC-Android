package com.app.nfc.model

import com.app.nfc.activity.HomeActivity
import com.app.nfc.activity.SplashActivity
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId

@RealmClass(name = "dishes")
open class Dish : RealmObject {
    @PrimaryKey var _id: ObjectId? = null
    var name: String?= null
    var category: String?= null
    var description: String?= null
    var price: Double = 0.0
    var ETP: Int = 0

    constructor(
        _id: ObjectId?,
        name: String,
        category: String,
        description: String,
        price: Double,
        ETP: Int
    ) {
        this._id = _id
        this.name = name
        this.category = category
        this.description = description
        this.price = price
        this.ETP = ETP
    }

    constructor() {}
}

fun getDishIdByName(name: String, realmObject: Realm): ObjectId {
    lateinit var foodId: ObjectId
    realmObject.executeTransaction { realm ->
        val dish = realm.where(Dish::class.java).equalTo("name", name).findFirst()
        foodId = dish?._id!!
    }

    return foodId
}
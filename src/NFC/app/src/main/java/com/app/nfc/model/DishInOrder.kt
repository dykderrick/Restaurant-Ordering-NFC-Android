package com.app.nfc.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId

@RealmClass(embedded = true)
open class DishInOrder : RealmObject {
    var dishID: ObjectId? = null
    var amount: Int = 0
    var status: String?= null

    constructor(dishID: ObjectId?, amount: Int, status: String) {
        this.dishID = dishID
        this.amount = amount
        this.status = status
    }

    constructor() {}
}
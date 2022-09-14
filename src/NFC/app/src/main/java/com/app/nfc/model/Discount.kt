package com.app.nfc.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass(embedded = true)
open class Discount : RealmObject {
    var type: String? = null
    var amount: Double = 0.0

    constructor(type: String?, amount: Double) {
        this.type = type
        this.amount = amount
    }

    constructor() {}

}
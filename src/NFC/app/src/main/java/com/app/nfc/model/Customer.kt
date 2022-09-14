package com.app.nfc.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass(embedded = true)
open class Customer : RealmObject {
    var name: String? = null
    var quantity: Int = 0
    var username: String? = null

    constructor(name: String, quantity: Int, username: String) {
        this.name = name
        this.quantity = quantity
        this.username = username
    }

    constructor() {}


}
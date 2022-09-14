package com.app.nfc.model

import android.util.Log
import com.app.nfc.activity.SplashActivity
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import org.bson.types.ObjectId

@RealmClass(name = "tables")
open class Table : RealmObject {
    @PrimaryKey var _id: ObjectId? = null
    var capacity: Int = 0
    var tableNo: String? = null
    var status: String? = null

    constructor(_id: ObjectId?, capacity: Int, tableNo: String, status: String) {
        this._id = _id
        this.capacity = capacity
        this.tableNo = tableNo
        this.status = status
    }

    constructor() {}

}

fun getTableStatus(tableNo: String): String {
    var status = ""
    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        val table = realm.where(Table::class.java).equalTo("tableNo", tableNo).findFirst()
        if (table != null) {
            status = table.status.toString()
        }
    }

    return status
}

fun setTableStatus(tableNo: String, newStatus: String) {
    if (tableNo != "") {
        SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
            val table = realm.where(Table::class.java).equalTo("tableNo", tableNo).findFirst()
            Log.v(
                "EXAMPLE",
                "Fetched object by name: $table"
            )
            if (table != null) {
                table.status = newStatus
            }
        }
    }
}

fun getTableCapacity(tableNo: String): Int {
    var capacity = 0
    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        val table = realm.where(Table::class.java).equalTo("tableNo", tableNo).findFirst()
        if (table != null) {
            capacity = table.capacity
        }
    }

    return capacity
}
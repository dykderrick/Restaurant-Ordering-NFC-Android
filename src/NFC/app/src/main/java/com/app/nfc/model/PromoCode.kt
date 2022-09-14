package com.app.nfc.model

import com.app.nfc.activity.SplashActivity
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId
import java.util.*

@RealmClass(name = "promocode")
open class PromoCode: RealmObject {
    @PrimaryKey var _id: ObjectId? = null
    var code: String? = null
    var description: String? = null
    var validStart: Date? = null
    var validEnd: Date? = null
    var discount: Discount? = null

    constructor(
        _id: ObjectId?,
        code: String?,
        description: String?,
        validStart: Date?,
        validEnd: Date?,
        discount: Discount?
    ) {
        this._id = _id
        this.code = code
        this.description = description
        this.validStart = validStart
        this.validEnd = validEnd
        this.discount = discount
    }

    constructor() {}

}

fun getPromoCodeDiscount(inputMoney: Double, inputPromoCode: String): Double {
    var discount = 0.0

    SplashActivity.backgroundThreadRealm.executeTransaction { realm ->
        val promoCodesData = realm.where(PromoCode::class.java).findAll()
        promoCodesData.forEach { promoCodeData ->
            if (promoCodeData.code == inputPromoCode) {
                val currentDateTime = Calendar.getInstance().time
                if (currentDateTime >= promoCodeData.validStart && currentDateTime <= promoCodeData.validEnd) {
                    if (promoCodeData.discount?.type == "percentage") {
                        discount = inputMoney * promoCodeData.discount?.amount!!
                    }
                    else if (promoCodeData.discount?.type == "cash") {
                        discount = promoCodeData.discount?.amount!!
                    }
                }
            }
        }
    }
    return discount
}
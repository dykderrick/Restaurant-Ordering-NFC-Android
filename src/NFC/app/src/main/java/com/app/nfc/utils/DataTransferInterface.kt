package com.app.nfc.utils

import com.app.nfc.model.FoodModel

interface DataTransferInterface {
    fun onSetValues(al: ArrayList<FoodModel>?)
}
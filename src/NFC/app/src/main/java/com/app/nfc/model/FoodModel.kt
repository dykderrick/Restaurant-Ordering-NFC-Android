package com.app.nfc.model

data class FoodModel(
        val foodId:Int,
        val foodName:String,
        val foodDes:String,
        val foodPrice: String,
        val foodImg: Int,
        val foodStar: String,
        var foodCount:String,
        var prepareTime:String
)

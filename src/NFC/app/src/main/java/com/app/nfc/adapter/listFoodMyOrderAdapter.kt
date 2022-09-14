package com.app.nfc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.nfc.R
import com.app.nfc.activity.HomeActivity
import com.app.nfc.activity.RegisterActivity
import com.app.nfc.model.FoodModel
import com.bumptech.glide.Glide


class listFoodMyOrderAdapter(
    val mContext: Context,
    private val mArrayListHomepageadapter: List<FoodModel>
) :
    RecyclerView.Adapter<listFoodMyOrderAdapter.MyViewholder>() {


    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        val tvFoodPrice: TextView = itemView.findViewById(R.id.tvFoodPrice)
        val tvFoodCount: TextView = itemView.findViewById(R.id.tvFoodCount)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val pbPrepareTime: ProgressBar = itemView.findViewById(R.id.pbPrepareTime)
        val ivFoodImg: ImageView = itemView.findViewById(R.id.ivFood_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_food_myorder, parent, false)
        return MyViewholder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val foodModel = mArrayListHomepageadapter[position]

        holder.tvFoodName.text =
            foodModel.foodName + " x" + HomeActivity.cartHashMap[foodModel].toString()
        val foodPrice =
            foodModel.foodPrice.toDouble() * HomeActivity.cartHashMap.getValue(foodModel)
        holder.tvFoodPrice.text = "£$foodPrice"
        holder.tvFoodCount.text = foodModel.prepareTime
        holder.tvStatus.text = "In Progress"
        holder.tvStatus.setTextColor(Color.parseColor("#50A9B0"))
        Glide.with(mContext).load(foodModel.foodImg).into(holder.ivFoodImg)

        // Set Preparation Time Progress Bar Timer
        val timeMills = foodModel.prepareTime.toLong() * 60000
        holder.pbPrepareTime.max = timeMills.toInt()
        holder.pbPrepareTime.progress = timeMills.toInt()
        val countDownTimer = object : CountDownTimer(timeMills, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentProgress = holder.pbPrepareTime.progress

                when (com.app.nfc.model.getDishStatus(
                    RegisterActivity.orderObjectId,
                    foodModel.foodName
                )) {
                    "prepared" -> {
                        holder.pbPrepareTime.progress = 0
                        holder.tvStatus.text = "Complete!"
                        holder.tvStatus.setTextColor(Color.GREEN)
                    }

                    "declined" -> {
                        holder.pbPrepareTime.progress = 0
                        holder.tvStatus.text = "Declined."
                        holder.tvStatus.setTextColor(Color.RED)

                        HomeActivity.cartHashMap.remove(foodModel)
                    }

                    else -> {
                        holder.pbPrepareTime.progress = currentProgress - 1000
                    }
                }
            }

            override fun onFinish() {
                holder.tvStatus.text = "Delayed."
                holder.tvStatus.setTextColor(Color.RED)
            }
        }
        countDownTimer.start()

    }

    override fun getItemCount(): Int {
        return mArrayListHomepageadapter.size
    }
}
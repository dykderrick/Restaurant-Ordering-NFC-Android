package com.app.nfc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.nfc.R
import com.app.nfc.activity.HomeActivity
import com.app.nfc.model.FoodModel
import com.bumptech.glide.Glide

class listCartFoodAdapter(private val mContext: Context, private val mArrayList: ArrayList<FoodModel>) : RecyclerView.Adapter<listCartFoodAdapter.MyViewholder>(){

    private var TAG = "listFoodOrderNowAdapter"

    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvFoodName = itemView.findViewById<TextView>(R.id.tvFoodName)
        val tvFoodPrice = itemView.findViewById<TextView>(R.id.tvFoodPrice)
        val tvFoodCount = itemView.findViewById<TextView>(R.id.tvFoodCount)
        val ivSub = itemView.findViewById<ImageView>(R.id.ivSub)
        val ivAdd = itemView.findViewById<ImageView>(R.id.ivAdd)
        val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
        val ivFood_img = itemView.findViewById<ImageView>(R.id.ivFood_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_food_order_now, parent, false)
        return listCartFoodAdapter.MyViewholder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        var price = mArrayList[position].foodPrice.toDouble()
        var count = mArrayList[position].foodCount.toInt()
        holder.ivSub.visibility =View.INVISIBLE

        holder.tvFoodName.text = mArrayList[position].foodName
        holder.tvFoodPrice.text = "£$price"
        holder.tvFoodCount.text = count.toString()
        Glide.with(mContext).load(mArrayList[position].foodImg).into(holder.ivFood_img)


        holder.ivAdd.setOnClickListener {
            com.app.nfc.utils.Common.printLog(TAG,"--------ivAdd.setOnClickListener---------")

            count += 1
            price += mArrayList[position].foodPrice.toDouble()
            HomeActivity.cartHashMap[mArrayList[position]] = HomeActivity.cartHashMap.getValue(mArrayList[position]) + 1
            holder.ivSub.visibility =View.VISIBLE
            holder.tvFoodCount.text = count.toString()
            holder.tvFoodPrice.text = "£$price"

        }



        holder.ivSub.setOnClickListener {
            com.app.nfc.utils.Common.printLog(TAG,"--------ivAdd.setOnClickListener---------")

            count -= 1
            price -= mArrayList[position].foodPrice.toDouble()
            HomeActivity.cartHashMap[mArrayList[position]] = HomeActivity.cartHashMap.getValue(mArrayList[position]) - 1
            holder.tvFoodCount.text = count.toString()
            holder.tvFoodPrice.text = "£$price"

            if (count <=1){
                holder.ivSub.visibility = View.INVISIBLE
            }
        }

        holder.ivDelete.setOnClickListener {
            mArrayList.removeAt(position)
            notifyDataSetChanged()

            val intent = Intent("ArraySize")
            intent.putExtra("item", HomeActivity.cartArrayList.size)
            mContext.sendBroadcast(intent)

        }

    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }
}
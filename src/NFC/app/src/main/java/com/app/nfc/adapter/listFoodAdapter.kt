package com.app.nfc.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.nfc.R
import com.app.nfc.activity.HomeActivity.Companion.cartArrayList
import com.app.nfc.activity.HomeActivity.Companion.cartHashMap
import com.app.nfc.model.FoodModel
import com.app.nfc.utils.Common
import com.app.nfc.utils.DataTransferInterface
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.lang.Exception
import java.net.URL

class listFoodAdapter(val mContext: Context, val mArrayList: ArrayList<FoodModel>) :

    RecyclerView.Adapter<listFoodAdapter.MyViewholder>() {

    var TAG = "listFoodAdapter"

    var dtInterface: DataTransferInterface? = null


    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvFoodName = itemView.findViewById<TextView>(R.id.tvFoodName)
        val tvDes_food = itemView.findViewById<TextView>(R.id.tvDes_food)
        val tvFoodPrice = itemView.findViewById<TextView>(R.id.tvFoodPrice)
        val ivAdd = itemView.findViewById<ImageView>(R.id.ivAdd)
        val ivFood_img = itemView.findViewById<ImageView>(R.id.ivFood_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_food, parent, false)
        return MyViewholder(view)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        Common.printLog(TAG, "-----------mArrayList-------------" + Gson().toJson(mArrayList))

        holder.tvFoodName.text = mArrayList[position].foodName
        holder.tvDes_food.text = mArrayList[position].foodDes
        holder.tvFoodPrice.text = "£" + mArrayList[position].foodPrice


//        Glide.with(mContext).load(mArrayList[position].foodImg).into(holder.ivFood_img)

        holder.ivFood_img.setImageResource(mArrayList[position].foodImg)
        //holder.ivFood_img.setImageBitmap(BitmapFactory.decodeStream(URL("https://i.imgur.com/kcvdN1g.jpg").openConnection().getInputStream()))




        holder.ivAdd.setOnClickListener {

            var isAdded: Boolean = false

            for (i in 0 until cartArrayList.size) {

                if (mArrayList[position].foodId == cartArrayList[i].foodId) {
                    isAdded = true
                    showBottomDialog()
                }
            }

            if (!isAdded) {
                cartArrayList.add(mArrayList[position])
                cartHashMap[mArrayList[position]] = 1
                val intent = Intent("KeyData")
                intent.putExtra("item", cartArrayList.size)
                mContext.sendBroadcast(intent)
            }

        }

    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }


    private fun showBottomDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val window = dialog.window
        val wlp = window!!.attributes
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dlg_allready_added)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(object : ColorDrawable(Color.TRANSPARENT) {})


        val lyOrderBY = dialog.findViewById<LinearLayout>(R.id.lyOrderBY)

        lyOrderBY.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

//        val bottomSheet = BottomSheetDialog()
//        bottomSheet.show(
//            supportFragmentManager,
//            "ModalBottomSheet"
//        )
    }
}
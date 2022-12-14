package com.app.nfc.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdaptor<T>(private val context: Activity, private var items: ArrayList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun onBindData(holder: RecyclerView.ViewHolder?, `val`: T, position: Int)
    abstract fun setItemLayout(): Int
    abstract fun setNoDataView(): TextView?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(setItemLayout(), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(holder, items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addItems(savedCardItemz: ArrayList<T>) {
        items = savedCardItemz
        notifyDataSetChanged()
        if (items.size == 0) {
            setNoDataView()?.visibility = View.VISIBLE
        } else {
            setNoDataView()?.visibility = View.GONE
        }
    }

    internal inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}
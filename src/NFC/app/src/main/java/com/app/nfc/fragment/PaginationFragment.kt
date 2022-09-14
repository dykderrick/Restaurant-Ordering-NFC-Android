package com.app.nfc.fragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nfc.R
import com.app.nfc.base.BaseAdaptor
import com.app.nfc.base.BaseFragmnet
import com.app.nfc.model.CityModel
import com.app.nfc.utils.Common
import com.app.nfc.utils.Common.alertErrorAndValidationDialog
import com.app.nfc.utils.Common.dismissLoadingProgress
import com.app.nfc.utils.Common.showLoadingProgress
import com.app.nfc.utils.SharePreference
import com.google.gson.JsonObject
import hr.apps.n80982.api.ApiClient
import kotlinx.android.synthetic.main.fragment_pagination.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaginationFragment:BaseFragmnet() {

    var manager:LinearLayoutManager?=null
    var currentPage=1
    var TotalPage=0
    var baseAdapter:BaseAdaptor<CityModel>?=null
    var listDeal:ArrayList<CityModel>?=null

    override fun setView(): Int {
        return R.layout.fragment_pagination
    }

    override fun Init(view: View) {

        listDeal= ArrayList()
        manager = LinearLayoutManager(activity)
        rvPagination!!.layoutManager = manager
        rvPagination!!.itemAnimator = DefaultItemAnimator()
        rvPagination!!.isNestedScrollingEnabled = true

        var visibleItemCount: Int = 0
        var totalItemCount: Int = 0
        var pastVisiblesItems: Int = 0

        if (Common.isCheckNetwork(activity!!)) {
            //mCallEventApi(true)
        } else {
            alertErrorAndValidationDialog(
                activity!!,
                resources.getString(R.string.no_internet)
            )
        }

        rvPagination!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = manager!!.getChildCount()
                    totalItemCount = manager!!.getItemCount()
                    pastVisiblesItems = manager!!.findFirstVisibleItemPosition()
                    if (currentPage < TotalPage) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            currentPage += 1
                            //------------------Call api Here for second---------------//
                            if (Common.isCheckNetwork(activity!!)) {
                               // mCallEventApi(true)
                            } else {
                                alertErrorAndValidationDialog(
                                    activity!!,
                                    resources.getString(R.string.no_internet)
                                )
                            }
                        }
                    }
                }
            }
        })

    }

    fun mCallEventApi(isFirstPage: Boolean) {

        showLoadingProgress(activity!!, false)
        val call: Call<JsonObject>? = ApiClient.getClient.getHotEvent(
            currentPage.toString(),
            SharePreference.getStringPref(activity!!, SharePreference.userId)!!
        )
        call!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val mainObject = JSONObject(response.body().toString())
                    val statusType = mainObject.getInt("status")
                    val statusMessage = mainObject.getString("message")
                    val dataObject = mainObject.getJSONObject("data")
                    TotalPage = dataObject.getInt("last_page");
                    // currentPage=dataObject.getInt("current_page");
                    if (statusType == 1) {
                        dismissLoadingProgress()
                        val dataArray = dataObject.getJSONArray("data")
                        if (!!isAdded && dataArray.length() > 0) {
                            rvPagination!!.visibility = View.VISIBLE
                            tvNoDataFound.visibility = View.GONE
                            for (i in 0 until dataArray.length()) {
                                val dataObj = dataArray.getJSONObject(i)
                            }
                            setAdaptor(listDeal!!,isFirstPage)
                        } else {
                            if (currentPage == 1) {
                                rvPagination.visibility = View.GONE
                                tvNoDataFound.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    dismissLoadingProgress()
                    rvPagination!!.visibility = View.GONE
                    tvNoDataFound.visibility = View.VISIBLE
                    alertErrorAndValidationDialog(
                        activity!!,
                        resources.getString(R.string.error_msg)
                    )
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dismissLoadingProgress()
                rvPagination!!.visibility = View.GONE
                tvNoDataFound.visibility = View.VISIBLE
                alertErrorAndValidationDialog(
                    activity!!,
                    resources.getString(R.string.error_msg)
                )
            }
        })
    }

    fun setAdaptor(listDeal: ArrayList<CityModel>, isFirstPage: Boolean) {
        if (isFirstPage) {
            baseAdapter = object : BaseAdaptor<CityModel>(activity!!, listDeal) {
                override fun onBindData(
                    holder: RecyclerView.ViewHolder?,
                    `val`: CityModel,
                    position: Int
                ) {
                    val tvName: TextView = holder!!.itemView.findViewById(R.id.tvName)
                    tvName.text = listDeal.get(position).cityName
                }

                override fun setItemLayout(): Int {
                    return R.layout.row_pagination
                }

                override fun setNoDataView(): TextView? {
                    return null
                }
            }
            rvPagination!!.adapter = baseAdapter
        } else {
            baseAdapter!!.notifyDataSetChanged()
        }
    }

}
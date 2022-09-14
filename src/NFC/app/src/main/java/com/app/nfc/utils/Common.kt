package com.app.nfc.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import com.app.nfc.R
import com.app.nfc.activity.HomeActivity
import com.app.nfc.activity.SplashActivity
import com.app.nfc.model.Dish
import com.app.nfc.utils.SharePreference.Companion.mContext
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object Common {
    //get Toast
    fun getToast(activity: Activity, strTxtToast: String) {
        Toast.makeText(activity, strTxtToast, Toast.LENGTH_SHORT).show()
    }

    //log print
    fun printLog(strKey: String, strValue: String) {
        Log.e(">>>---  $strKey  ---<<<", strValue)
    }

    //validation email
    fun isValidEmail(strPattern: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(strPattern).matches();
    }

    //validation password
    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    //validation valid url
    fun isValidUrl(urlString: String): Boolean {
        try {
            val url = URL(urlString)
            return URLUtil.isValidUrl(url.toString()) && Patterns.WEB_URL.matcher(url.toString())
                .matches()
        } catch (e: MalformedURLException) {
        }
        return false
    }

    //network connection check
    fun isCheckNetwork(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //Request Body function
    fun setRequestBody(strBodyData: String): RequestBody {
        return strBodyData.toRequestBody("text/plain".toMediaType())
    }

    //Single Image upload
    fun setUploadImage(file: File, strPrameterName: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            strPrameterName,
            file.getName(),
            RequestBody.create("image/*".toMediaType(), file)
        )
    }

    //Multiple Image upload
    fun setMultipleUploadImage(
        partName: String,
        fileUri: Uri,
        activity: Activity
    ): MultipartBody.Part? {
        val file: File = File(FileUtils.getPath(fileUri, activity))
        val requestFile: RequestBody = RequestBody.create(
            activity.contentResolver.getType(fileUri)!!.toMediaType(),
            file
        )
        return MultipartBody.Part.createFormData("images[]", file.name, requestFile)
    }


    //Time Piker
    @SuppressLint("SetTextI18n")
    fun setTime(tvTime: TextView, activity: Activity) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(
            activity, R.style.MyDatePickerDialogTheme,
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                val AM_PM: String
                AM_PM = if (h >= 0 && h < 12) {
                    "AM"
                } else {
                    "PM"
                }

                var selectHour = h
                if (AM_PM.equals("AM")) {
                    if (h == 0) {
                        selectHour = 12
                    } else if (h >= 1 && h < 12) {
                        selectHour = h
                    }

                } else if (AM_PM.equals("PM")) {
                    if (h == 12) {
                        selectHour = 12
                    } else if (h >= 13 && h <= 23) {
                        selectHour = h - 12
                    }
                }

                val strSelectHour = if (selectHour < 10) {
                    "0${selectHour}"
                } else {
                    selectHour.toString()
                }

                tvTime.text = "$strSelectHour:$m $AM_PM"
            }),
            hour,
            minute,
            false
        )
        tpd.show()
    }

    //Date Piker
    @SuppressLint("SetTextI18n")
    fun setDate(tvDate: TextView, activity: Activity) {
        val mYear: Long
        val mMonth: Long
        val mDay: Long
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR).toLong()
        mMonth = c.get(Calendar.MONTH).toLong()
        mDay = c.get(Calendar.DAY_OF_MONTH).toLong()

        val datePickerDialog =
            DatePickerDialog(
                activity, R.style.MyDatePickerDialogTheme, OnDateSetListener
                { view, year, monthOfYear,
                  dayOfMonth ->
                    val selectMonth = if ((monthOfYear + 1) < 10) {
                        "0${(monthOfYear + 1)}"
                    } else {
                        (monthOfYear + 1).toString()
                    }
                    val selectDay = if (dayOfMonth < 10) {
                        "0${dayOfMonth}"
                    } else {
                        dayOfMonth.toString()
                    }
                    tvDate.text = selectDay + "-" + selectMonth + "-" + year
                }, mYear.toInt(), mMonth.toInt(), mDay.toInt()
            )
        datePickerDialog.show()
    }

    //Facebook key Generate
    @SuppressLint("PackageManagerGetSignatures")
    fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            //getting application package name, as defined in manifest
            val packageName = context.applicationContext.packageName
            //Retriving package info
            packageInfo = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            Log.e("Package Name=", context.applicationContext.packageName)
            for (signature in packageInfo.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))
                printLog("Key Hash", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        return key
    }

    //load Image
    fun loadImage(activity: Activity, strUrl: String, ivLoadImage: ImageView, isUser: Boolean) {
        if (isUser) {
            Glide.with(activity).load(strUrl).placeholder(activity.resources.getDrawable(R.drawable.ic_user_place)).into(ivLoadImage)
        } else {
            Glide.with(activity).load(strUrl).placeholder(ColorDrawable(getColor(activity, R.color.gray))).into(ivLoadImage)
        }
    }

    //progress Dialog
    open var dialog: Dialog? = null

    open fun dismissLoadingProgress() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    open fun showLoadingProgress(context: Activity, isCancelable: Boolean) {
        context.runOnUiThread {
            try {
                if (dialog != null) {
                    dialog!!.dismiss()
                    dialog = null
                }
                if (!context.isFinishing) {
                    dialog = Dialog(context)
                    dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog!!.setContentView(R.layout.dlg_progressloader)
                    dialog!!.setCancelable(isCancelable)
                    if (dialog!!.isShowing()) {
                        dialog!!.dismiss()
                    }
                    if (!context.isFinishing && !dialog!!.isShowing()) dialog!!.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //error and validation Dialog
    fun alertErrorAndValidationDialog(act: Activity, msg: String?) {
        var dialog: Dialog? = null
        try {
            if (dialog != null) {
                dialog.dismiss()
                dialog = null
            }
            dialog = Dialog(act, R.style.AppCompatAlertDialogStyleBig)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            );
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            val m_inflater = LayoutInflater.from(act)
            val m_view = m_inflater.inflate(R.layout.dlg_no_internet, null, false)
            val tvDesc = m_view.findViewById<TextView>(R.id.tvDesc)
            val tvOk = m_view.findViewById<TextView>(R.id.tvOk)
            tvDesc.text = msg
            val finalDialog: Dialog = dialog
            tvOk.setOnClickListener {
                finalDialog.dismiss()
            }
            dialog.setContentView(m_view)
            if (!act.isFinishing) dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //Setting Dialog
    fun settingDialog(act: Activity) {
        var dialog: Dialog? = null
        try {
            if (dialog != null) {
                dialog.dismiss()
                dialog = null
            }
            dialog = Dialog(act, R.style.AppCompatAlertDialogStyleBig)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            );
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            val m_inflater = LayoutInflater.from(act)
            val m_view = m_inflater.inflate(R.layout.dlg_permission, null, false)
            val tvOk = m_view.findViewById<TextView>(R.id.tvOk)

            val finalDialog: Dialog = dialog
            tvOk.setOnClickListener {
                var i = Intent()
                i.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.setData(android.net.Uri.parse("package:" + act.getPackageName()))
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                act.startActivity(i)
                dialog.dismiss()
                finalDialog.dismiss()
            }
            dialog.setContentView(m_view)
            if (!act.isFinishing) dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //Logout Dialog
    fun setLogout(activity: Activity) {
        val preference = SharePreference(activity)
        preference.mLogout()
        val intent = Intent(activity, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent);
        activity.finish()
    }

    //
    fun mExitDialog(act: Activity) {
        var dialog: Dialog? = null
        try {
            if (dialog != null) {
                dialog.dismiss()
                dialog = null
            }
            dialog = Dialog(act, R.style.AppCompatAlertDialogStyleBig)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            );
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            val m_inflater = LayoutInflater.from(act)
            val m_view = m_inflater.inflate(R.layout.dlg_logout, null, false)
            val tvMsgConformation = m_view.findViewById<TextView>(R.id.tvDesc)
            tvMsgConformation.text = m_view.resources.getString(R.string.exit_conformation)
            val finalDialog: Dialog = dialog

            val tvCancel = m_view.findViewById<TextView>(R.id.tvCancel)
            tvCancel.setOnClickListener {
                finalDialog.dismiss()
            }

            val tvOk = m_view.findViewById<TextView>(R.id.tvOk)
            tvOk.setOnClickListener {
                finalDialog.dismiss()
            }
            dialog.setContentView(m_view)
            if (!act.isFinishing) dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //Hide SoftKeyboard
    fun hideSoftKeyboard(activity: Activity) {
        val view: View? = activity.currentFocus
        if (view != null) {
            val imm: InputMethodManager =activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    //Open Keyboard
    fun openKeyboard(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    //Close Keyboard
    fun closeKeyBoard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            try {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    //Capitalize Word
    fun capitalizeWord(capString: String?): String? {
        return if (capString!!.length == 0 || capString == null) {
            ""
        } else {
            val capBuffer = StringBuffer()
            val capMatcher = Pattern.compile(
                "([a-z])([a-z]*)",
                Pattern.CASE_INSENSITIVE
            ).matcher(capString)
            while (capMatcher.find()) {
                capMatcher.appendReplacement(
                    capBuffer,
                    capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase()
                )
            }
            capMatcher.appendTail(capBuffer).toString()
        }
    }

    //GetDevice Token
    fun getDeviceToken(act: Activity?): String {
        FirebaseApp.initializeApp(act!!)
        val strToken: String = FirebaseInstanceId.getInstance().getToken()!!
        printLog("FCM", "TOKEN FCM:$strToken")
        return strToken
    }

    //SetApi Error Message
    fun setErrorMessage(mainObject: JSONObject,activity: Activity) {
        if(mainObject.getInt("ResponseCode")==9){
            setLogout(activity)
        }else{
            alertErrorAndValidationDialog(
                activity,
                mainObject.getString("ResponseText")
            )
        }
    }

    //getCurrent LatLong //0:-latitude 1:-Longitude 2:-CurrentAddress
     fun getCurrentLocation(activity: Activity):ArrayList<String> {
        val arrayList=ArrayList<String>()
        val callback: LocationProvider.LocationCallback =
            object : LocationProvider.LocationCallback {
                override fun locationRequestStopped() {
                    //location updates stopped
                }

                override fun onNewLocationAvailable(lat: Float, lon: Float) {
                    if (lat != 0f && lon != 0f) {
                        try {
                            val geocoder = Geocoder(activity)
                            val addressList: List<Address>? =
                                geocoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
                            if (addressList != null && addressList.size > 0) {
                                val currentAddress: String = addressList[0].getAddressLine(0)
                                val countryName: String = addressList[0].getCountryName()
                                val city: String = addressList[0].getLocality()
                                val placeName: String = addressList[0].getFeatureName()
                                arrayList.add(lat.toDouble().toString())
                                arrayList.add(lon.toDouble().toString())
                                arrayList.add(currentAddress)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()

                        }
                    }
                }

                override fun locationServicesNotEnabled() {
                    //failed finding a location
                }

                override fun updateLocationInBackground(lat: Float, lon: Float) {
                    //if a listener returns after the main locationAvailable callback, it will go here
                   printLog("Location", "updateLocationInBackground:$lat $lon")
                }

                override fun networkListenerInitialised() {
                    //when the library switched from GPS only to GPS & network
                }
            }
        val locationProvider: LocationProvider =
            LocationProvider.Builder().setContext(activity).setListener(callback).create()
        locationProvider.requestLocation()

       return arrayList
    }

}
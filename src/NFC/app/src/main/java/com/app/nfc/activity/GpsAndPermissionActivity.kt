package com.app.nfc.activity

import android.Manifest
import android.content.Intent
import com.app.nfc.R
import com.app.nfc.base.BaseActivity
import com.app.nfc.utils.Common.getCurrentLocation
import com.app.nfc.utils.Common.getToast
import com.app.nfc.utils.Common.settingDialog
import com.app.nfc.utils.GpsStatusDetector
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class GpsAndPermissionActivity:BaseActivity(), GpsStatusDetector.GpsStatusDetectorCallBack {

    private var mGpsStatusDetector: GpsStatusDetector? = null

    override fun setLayout(): Int {
        return R.layout.activity_splash
    }

    override fun InitView() {
        mGpsStatusDetector = GpsStatusDetector(this)
        mGpsStatusDetector!!.checkGpsStatus()
    }

    override fun onGpsAlertCanceledByUser() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GpsStatusDetector.REQUEST_CODE) {
            mGpsStatusDetector!!.checkOnActivityResult(requestCode, resultCode)
        }
    }
    fun checkPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    val getLocation=getCurrentLocation(this@GpsAndPermissionActivity)
                    getToast(this@GpsAndPermissionActivity,"Lat:-${getLocation.get(0)} Lon:-${getLocation.get(1)} Address:-${getLocation.get(2)}")
                    if (report.areAllPermissionsGranted()) {

                        // after all permission success(code here)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                       settingDialog(this@GpsAndPermissionActivity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).onSameThread().check()
    }

    override fun onGpsSettingStatus(enabled: Boolean) {
        if (enabled) {
            checkPermission()
        } else {
            mGpsStatusDetector!!.checkGpsStatus()
        }
    }
}
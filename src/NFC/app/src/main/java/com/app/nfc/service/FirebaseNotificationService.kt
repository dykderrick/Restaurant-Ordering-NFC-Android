package com.app.nfc.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.nfc.R
import com.app.nfc.utils.Common
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class FirebaseNotificationService : FirebaseMessagingService() {
    var TAG = "MissingFirebaseInstanceTokenRefresh"

    private val REQUEST_CODE = 1
    private var NOTIFICATION_ID = 6578
    var ctx: Context? = null
    var Notification_message: String? = null

    @SuppressLint("WrongThread")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        ctx = applicationContext
        Common.printLog(TAG, "onMessageReceived: $remoteMessage")
        Common.printLog(TAG, "onMessageReceived: From" + remoteMessage.from)
        val Notification_title = remoteMessage.data["title"]
        Notification_message = remoteMessage.data["message"]
        val Notification_data = remoteMessage.data["data"]
        val Notification_status = remoteMessage.data["status"]
        Common.printLog("onMessageReceived=== Notification_title", "" + Notification_title)
        Common.printLog("onMessageReceived=== Notification_message", "" + Notification_message)
        Common.printLog("onMessageReceived=== Notification_data", "" + Notification_data)
        Common.printLog("onMessageReceived=== Notification_status", "" + Notification_status)
        // showNotifications(Notification_title, Notification_message, "");
        //onIncomingCalling("","","");
        onResponseBody(Notification_message)
    }

    fun onResponseBody(strResponseBody: String?) {
        try {
            val jsonObjectMain = JSONObject(strResponseBody)
            val strActionType = jsonObjectMain.getString("type")
            var strPatient_id: String? = ""
            showNotifications("Missed Call", Notification_message)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun showNotifications(strTitle: String, strMessage: String?) {

        val channelId = "channel-01"
        val channelName = "Channel Name"
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            manager!!.createNotificationChannel(mChannel)
            val mBuilder = NotificationCompat.Builder(this@FirebaseNotificationService, channelId)
                .setSmallIcon(getIcon())
                .setAutoCancel(true)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
            val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
           // stackBuilder.addNextIntent(intent)
            val resultPendingIntent: PendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            mBuilder.setContentIntent(resultPendingIntent)
            manager.notify(NOTIFICATION_ID, mBuilder.build())
        } else {
           /* val pendingIntent = PendingIntent.getActivity(
                this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
            )*/
            val notification: Notification = NotificationCompat.Builder(this)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
                .setAutoCancel(true)
               // .setContentIntent(pendingIntent)
                .setSmallIcon(getIcon())
                .build()
            manager!!.notify(NOTIFICATION_ID, notification)
        }
        NOTIFICATION_ID++
    }

    private fun getIcon(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) R.drawable.ic_small_notification else R.drawable.ic_small_notification
    }
}
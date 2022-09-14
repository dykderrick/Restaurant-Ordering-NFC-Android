package com.app.nfc.activity

import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.app.nfc.R
import com.app.nfc.base.BaseActivity
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration


class SplashActivity : BaseActivity() {
    companion object {
        lateinit var app: App
        lateinit var credentials: Credentials
        lateinit var config: SyncConfiguration
        lateinit var backgroundThreadRealm: Realm
    }


    override fun setLayout(): Int {
        return R.layout.activity_splash
    }

    override fun InitView() {
        initRealm()

        supportActionBar!!.hide()

        Handler().postDelayed({
            openActivity(RegisterActivity::class.java)
            finish()
        }, 3000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    private fun initRealm() {
        Realm.init(this)

        val appID: String = getString(R.string.realmAppID)

        app = App(
            AppConfiguration.Builder(appID)
                .build()
        )

        credentials = Credentials.anonymous()

        app.loginAsync(credentials) {
            if (it.isSuccess) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.")
                val user: User? = app.currentUser()

                val partitionValue = "ding"
                config = SyncConfiguration.Builder(user, partitionValue)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build()

                backgroundThreadRealm = Realm.getInstance(config)

            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: ${it.error}")
            }
        }
    }

}
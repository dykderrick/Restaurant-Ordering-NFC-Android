package com.app.nfc.model

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.app.nfc.R
import com.app.nfc.activity.SplashActivity
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class DishTest {
    lateinit var realm: Realm

    @Before
    fun setUp() {
        Realm.init(InstrumentationRegistry.getInstrumentation().targetContext)
        val appID = "android-ordering-ercfe"


        val app = App(
            AppConfiguration.Builder(appID)
                .build()
        )

        val credentials = Credentials.anonymous()

        app.loginAsync(credentials) {
            if (it.isSuccess) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.")
                val user: User? = SplashActivity.app.currentUser()

                val partitionValue = "ding"
                val config = SyncConfiguration.Builder(user, partitionValue)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build()

                realm = Realm.getInstance(config)

            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: ${it.error}")
            }
        }
    }

    @After
    fun tearDown() {
        realm.close()
    }

    @Test
    fun whenInputDishNameShouldReturnId() {

    }
}
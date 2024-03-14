package dev.stupak.platform.base

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.navigation.navigator.Navigator
import dev.stupak.navigation.navigator.ToFlowNavigable
import dev.stupak.platform.receiver.MyBroadcastReceiver

abstract class BaseActivity(
    @LayoutRes layout: Int,
) : AppCompatActivity(layout), ToFlowNavigable {
    private val networkStateReceiver = MyBroadcastReceiver()
    val navigator: Navigator = Navigator()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        configureUi()

        registerReceiver(
            networkStateReceiver,
            IntentFilter().apply { addAction(ConnectivityManager.CONNECTIVITY_ACTION) },
        )
    }

    override fun navigateToFlow(
        flow: NavigationFlow?,
        clearBackStackEntry: Boolean,
        deeplink: String?,
        argument: String?,
    ) {
        try {
            navigator.navigateToFlow(flow, clearBackStackEntry, deeplink)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun isFirstRun(): Boolean {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_FIRST_RUN, true)
    }

    fun setFirstRun(isFirstRun: Boolean) {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(IS_FIRST_RUN, isFirstRun).apply()
    }

    protected open fun configureUi() = Unit

    companion object {
        private const val IS_FIRST_RUN = "isFirstRun"
    }
}

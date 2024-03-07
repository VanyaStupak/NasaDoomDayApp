package dev.stupak.platform.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.navigation.navigator.Navigator
import dev.stupak.navigation.navigator.ToFlowNavigable
import dev.stupak.platform.R


abstract class BaseActivity(
    @LayoutRes layout: Int,
) : AppCompatActivity(layout), ToFlowNavigable {

    val navigator: Navigator = Navigator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureUi()
    }

    override fun navigateToFlow(
        flow: NavigationFlow?,
        clearBackStackEntry: Boolean,
        deeplink: String?,
    ) {
        try {
            navigator.navigateToFlow(flow, clearBackStackEntry, deeplink)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    fun isFirstRun(): Boolean {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isFirstRun", true)
    }

    fun setFirstRun(isFirstRun: Boolean) {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isFirstRun", isFirstRun).apply()
    }

    protected open fun configureUi() = Unit


}

package dev.stupak.platform.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        context?.let {
            isOnline.value = isOnline(intent)
            if (!isOnline(intent)) {
                Toast.makeText(context, NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isOnline(intent: Intent?): Boolean {
        if (intent?.extras != null) {
            return !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        }
        return false
    }

    companion object {
        val isOnline: MutableStateFlow<Boolean> = MutableStateFlow(false)
        const val NO_INTERNET_CONNECTION = "No internet connection"
    }
}

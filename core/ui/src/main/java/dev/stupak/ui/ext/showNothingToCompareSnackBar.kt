package dev.stupak.ui.ext

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.snackbar.Snackbar
import dev.stupak.ui.databinding.WidgetSnackbarBinding

fun View.showSnackbar(
    view: View,
    text: String,
) {
    val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
    snackbar.view.updateLayoutParams<FrameLayout.LayoutParams> {
        gravity = Gravity.BOTTOM
        bottomMargin = 192
    }
    val snackbarLayoutBinding = snackbar.view as? Snackbar.SnackbarLayout
    snackbarLayoutBinding?.setPadding(0, 0, 0, 0)

    val binding = WidgetSnackbarBinding.inflate(LayoutInflater.from(view.context))
    binding.apply {
        btnClose.setOnClickListener {
            snackbar.dismiss()
        }
        tvTitle.text = text
    }
    snackbarLayoutBinding?.addView(binding.root)

    Handler(Looper.getMainLooper()).postDelayed({
        snackbar.dismiss()
    }, 7000)

    return snackbar.show()
}

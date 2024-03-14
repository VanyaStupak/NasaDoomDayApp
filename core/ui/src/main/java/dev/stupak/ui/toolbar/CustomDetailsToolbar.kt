package dev.stupak.ui.toolbar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.stupak.ui.R
import dev.stupak.ui.databinding.WidgetToolbarBinding

class CustomDetailsToolbar : ConstraintLayout {
    private val binding by viewBinding(WidgetToolbarBinding::bind)

    var textColor: Int? = null
    var infoButtonIcon: Drawable? = null
        set(value) {
            field = value
            value?.let {
                with(binding.btnInfo) {
                    isVisible = infoButtonIcon != null
                    setImageDrawable(infoButtonIcon)
                }
            }
        }
    var buttonBack: Drawable? = null
        set(value) {
            field = value
            value?.let {
                with(binding.btnBack) {
                    isVisible = buttonBack != null
                    setImageDrawable(buttonBack)
                }
            }
        }

    var titleText: String? = null
        set(value) {
            field = value
            value?.let {
                binding.tvTitle.text = value
            }
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        WidgetToolbarBinding.inflate(
            LayoutInflater.from(context),
            this,
        )
        initAttr(attrs)
    }

    private fun initAttr(attrs: AttributeSet?) {
        this.context.obtainStyledAttributes(attrs ?: return, R.styleable.CustomToolbar).use {
            infoButtonIcon = it.getDrawable(R.styleable.CustomToolbar_ct_btn_info)
            titleText = it.getString(R.styleable.CustomToolbar_ct_txt_title)
            textColor = it.getColor(R.styleable.CustomToolbar_ct_txt_color, Color.TRANSPARENT)
            val backVisibility = it.getInt(R.styleable.CustomToolbar_ct_btn_back_visibility, View.GONE)
            val titleVisibility = it.getInt(R.styleable.CustomToolbar_ct_txt_title_visibility, View.VISIBLE)
            val profileVisibility = it.getInt(R.styleable.CustomToolbar_ct_btn_info_visibility, View.VISIBLE)

            binding.btnBack.visibility = backVisibility
            binding.tvTitle.visibility = titleVisibility
            binding.btnInfo.visibility = profileVisibility
        }
    }

    var onInfoButtonClickListener: OnClickListener? = null
        set(value) {
            field = value
            binding.btnInfo.setOnClickListener(value)
        }

    var onBackButtonClickListener: OnClickListener? = null
        set(value) {
            field = value
            binding.btnBack.setOnClickListener(value)
        }

    fun setMenuButtonVisible(visible: Boolean) {
        binding.btnInfo.isVisible = visible
    }
}

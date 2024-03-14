package dev.stupak.settings

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.platform.base.BaseFragment
import dev.stupak.settings.databinding.FragmentSettingsBinding
import dev.stupak.settings.model.SettingsDataUIModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class FragmentSettings : BaseFragment(R.layout.fragment_settings) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by viewModels()
    private var diameter: String? = null
    private var velocity: String? = null
    private var distance: String? = null
    private var pushInterval: String? = null

    override fun configureUi(savedInstanceState: Bundle?) {
        activity?.window?.statusBarColor = Color.WHITE
        configureButtons()
        observeSettingsData()
    }

    private fun observeSettingsData() {
        lifecycleScope.launch {
            viewModel.settingsStateFlow.collect { data ->
                binding.apply {
                    tvDiameter.text = data.diameterUnit ?: KILOMETERS
                    tvDistance.text = data.distanceUnit ?: KILOMETERS
                    tvVelocity.text = data.velocityUnit ?: KM_H
                    tvInterval.text = data.pushInterval ?: DAY
                }
            }
        }
    }

    private fun configureButtons() {
        with(binding) {
            linearDiameter.setOnClickListener {
                showSelectionDialog(
                    DIAMETER,
                    resources.getStringArray(dev.stupak.ui.R.array.diameter_settings).toList(),
                    resources.getStringArray(dev.stupak.ui.R.array.diameter_settings)
                        .toList()
                        .indexOf(viewModel.settingsStateFlow.value.diameterUnit ?: KILOMETERS),
                ) { selectedOption ->
                    diameter = selectedOption
                    viewModel.setSettingsData(
                        SettingsDataUIModel(
                            diameter,
                            velocity,
                            distance,
                            pushInterval,
                        ),
                    )
                }
            }

            linearVelocity.setOnClickListener {
                showSelectionDialog(
                    VELOCITY,
                    resources.getStringArray(dev.stupak.ui.R.array.velocity_settings).toList(),
                    resources.getStringArray(dev.stupak.ui.R.array.velocity_settings)
                        .toList().indexOf(viewModel.settingsStateFlow.value.velocityUnit ?: KM_H),
                ) { selectedOption ->
                    velocity = selectedOption
                    viewModel.setSettingsData(
                        SettingsDataUIModel(
                            diameter,
                            velocity,
                            distance,
                            pushInterval,
                        ),
                    )
                }
            }

            linearDistance.setOnClickListener {
                showSelectionDialog(
                    DISTANCE,
                    resources.getStringArray(dev.stupak.ui.R.array.distance_settings).toList(),
                    resources.getStringArray(dev.stupak.ui.R.array.distance_settings)
                        .toList()
                        .indexOf(viewModel.settingsStateFlow.value.distanceUnit ?: KILOMETERS),
                ) { selectedOption ->
                    distance = selectedOption
                    viewModel.setSettingsData(
                        SettingsDataUIModel(
                            diameter,
                            velocity,
                            distance,
                            pushInterval,
                        ),
                    )
                }
            }

            linearPushInterval.setOnClickListener {
                showSelectionDialog(
                    TIME_INTERVAL,
                    resources.getStringArray(dev.stupak.ui.R.array.interval_settings).toList(),
                    resources.getStringArray(dev.stupak.ui.R.array.interval_settings)
                        .toList().indexOf(viewModel.settingsStateFlow.value.pushInterval ?: DAY),
                ) { selectedOption ->
                    pushInterval = selectedOption
                    viewModel.setSettingsData(
                        SettingsDataUIModel(
                            diameter,
                            velocity,
                            distance,
                            pushInterval,
                        ),
                    )
                }
            }
        }
    }

    private fun showSelectionDialog(
        title: String,
        options: List<String>,
        selectedOption: Int,
        onOptionSelected: (String) -> Unit,
    ) {
        val dialog =
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setSingleChoiceItems(options.toTypedArray(), selectedOption) { _, which ->
                    onOptionSelected(options[which])
                }
                .setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        dev.stupak.ui.R.drawable.bg_size_comparison
                    )
                )
                .setPositiveButton("Ok", null)
                .create()

        dialog.show()
    }

    companion object {
        private const val KILOMETERS = "Kilometers"
        private const val KM_H = "Km/h"
        private const val DAY = "24h"
        private const val DIAMETER = "Diameter"
        private const val VELOCITY = "Velocity"
        private const val DISTANCE = "Distance"
        private const val TIME_INTERVAL = "Time Interval"
    }
}

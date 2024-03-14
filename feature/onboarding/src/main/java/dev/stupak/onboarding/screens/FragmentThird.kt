package dev.stupak.onboarding.screens

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.onboarding.R
import dev.stupak.onboarding.databinding.FragmentThirdBinding
import dev.stupak.platform.base.BaseActivity
import dev.stupak.platform.base.BaseFragment

class FragmentThird : BaseFragment(R.layout.fragment_third) {
    private val binding by viewBinding(FragmentThirdBinding::bind)
    private val pushNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) {}

    override fun configureUi(savedInstanceState: Bundle?) {
        super.configureUi(savedInstanceState)

        binding.tvStart.setOnClickListener {
            navigateToFlow(NavigationFlow.HostFlow, true)
            (activity as? BaseActivity)?.setFirstRun(false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val isPermissionGranted =
                    PermissionChecker.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) == PermissionChecker.PERMISSION_GRANTED
                if (!isPermissionGranted) {
                    pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}

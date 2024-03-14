package dev.stupak.onboarding.screens

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.onboarding.R
import dev.stupak.onboarding.databinding.FragmentFirstBinding
import dev.stupak.platform.base.BaseActivity
import dev.stupak.platform.base.BaseFragment

class FragmentFirst : BaseFragment(R.layout.fragment_first) {
    private val binding by viewBinding(FragmentFirstBinding::bind)
    private val pushNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) {}

    override fun configureUi(savedInstanceState: Bundle?) {
        super.configureUi(savedInstanceState)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        with(binding) {
            tvNext.setOnClickListener {
                viewPager?.currentItem = 1
            }

            tvSkip.setOnClickListener {
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
}

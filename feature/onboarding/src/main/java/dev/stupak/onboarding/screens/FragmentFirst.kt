package dev.stupak.onboarding.screens

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.onboarding.R
import dev.stupak.onboarding.databinding.FragmentFirstBinding
import dev.stupak.onboarding.databinding.FragmentOnboardingBinding
import dev.stupak.platform.base.BaseActivity
import dev.stupak.platform.base.BaseFragment

class FragmentFirst : BaseFragment(R.layout.fragment_first) {
    private val binding by viewBinding(FragmentFirstBinding::bind)

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
            }
        }
    }
}
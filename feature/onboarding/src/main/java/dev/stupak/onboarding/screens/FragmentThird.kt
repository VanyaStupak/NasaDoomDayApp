package dev.stupak.onboarding.screens

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.onboarding.R
import dev.stupak.onboarding.databinding.FragmentSecondBinding
import dev.stupak.onboarding.databinding.FragmentThirdBinding
import dev.stupak.platform.base.BaseActivity
import dev.stupak.platform.base.BaseFragment


class FragmentThird : BaseFragment(R.layout.fragment_third) {
    private val binding by viewBinding(FragmentThirdBinding::bind)

    override fun configureUi(savedInstanceState: Bundle?) {
        super.configureUi(savedInstanceState)

        binding.tvStart.setOnClickListener {
            navigateToFlow(NavigationFlow.HostFlow, true)
            (activity as? BaseActivity)?.setFirstRun(false)
        }
    }


}
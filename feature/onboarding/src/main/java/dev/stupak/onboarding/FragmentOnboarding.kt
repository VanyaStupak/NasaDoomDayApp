package dev.stupak.onboarding

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import by.kirich1409.viewbindingdelegate.viewBinding

import dev.stupak.onboarding.adapter.ViewPagerAdapter
import dev.stupak.onboarding.databinding.FragmentOnboardingBinding
import dev.stupak.onboarding.screens.FragmentFirst
import dev.stupak.onboarding.screens.FragmentSecond
import dev.stupak.onboarding.screens.FragmentThird
import dev.stupak.platform.base.BaseFragment

class FragmentOnboarding : BaseFragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun configureUi(savedInstanceState: Bundle?) {
        super.configureUi(savedInstanceState)
        val fragmentList = arrayListOf<Fragment>(
            FragmentFirst(),
            FragmentSecond(),
            FragmentThird()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.apply {
            viewPager.adapter = adapter
            viewPager.setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }
            dotsIndicator.attachTo(viewPager)

        }

    }

    private fun setParallaxTransformation(page: View, position: Float) {
       if(position >= -1 && position <= 1) {
           page.findViewById<ConstraintLayout>(R.id.page_item).translationX = -position * page.width / 2
       }else {
           page.alpha = 1f
       }
    }
}

package dev.stupak.comparison

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.comparison.adapter.PagerAdapter
import dev.stupak.comparison.databinding.FragmentComparisonBinding
import dev.stupak.platform.base.BaseFragment
import dev.stupak.ui.ext.removeBrackets
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class FragmentComparison : BaseFragment(R.layout.fragment_comparison) {
    private val binding by viewBinding(FragmentComparisonBinding::bind)
    private lateinit var viewPager2: ViewPager2
    private val viewModel: ComparisonViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureUi(savedInstanceState: Bundle?) {
        activity?.window?.statusBarColor =
            ContextCompat
                .getColor(requireContext(), dev.stupak.ui.R.color.background)
        viewPager2 = binding.viewPager
        val asteroidId: String? = arguments?.getString("id")
        lifecycleScope.launch {
            viewModel.comparisonStateFlow.collect { state ->

                if (state.favourites.isNotEmpty()) {
                    if (state.favourites.size == 2)
                        {
                            binding.apply {
                                viewPager.visibility = View.GONE
                                twoItemsLayout.visibility = View.VISIBLE

                                val favourite1 = state.favourites[0]
                                val favourite2 = state.favourites[1]
                                tvName1.text = favourite1.name.removeBrackets()
                                tvName2.text = favourite2.name.removeBrackets()

                                when (state.settingsData.diameterUnit) {
                                    "Meters" -> {
                                        val minDiameter1 = String.format("%.2f", favourite1.minDiameterM)
                                        val maxDiameter1 = String.format("%.2f", favourite1.minDiameterM)
                                        val minDiameter2 = String.format("%.2f", favourite2.minDiameterM)
                                        val maxDiameter2 = String.format("%.2f", favourite2.minDiameterM)
                                        tvDiameter1.text = "$minDiameter1 - $maxDiameter1 m"
                                        tvDiameter2.text = "$minDiameter2 - $maxDiameter2 m"
                                    }

                                    "Kilometers" -> {
                                        val minDiameter1 = String.format("%.3f", favourite1.minDiameterKm)
                                        val maxDiameter1 = String.format("%.3f", favourite1.maxDiameterKm)
                                        val minDiameter2 = String.format("%.3f", favourite2.minDiameterKm)
                                        val maxDiameter2 = String.format("%.3f", favourite2.maxDiameterKm)
                                        tvDiameter1.text = "$minDiameter1 - $maxDiameter1 km"
                                        tvDiameter2.text = "$minDiameter2 - $maxDiameter2 km"
                                    }

                                    "Miles" -> {
                                        val minDiameter1 = String.format("%.2f", favourite1.minDiameterMile)
                                        val maxDiameter1 = String.format("%.2f", favourite1.minDiameterMile)
                                        val minDiameter2 = String.format("%.2f", favourite2.minDiameterMile)
                                        val maxDiameter2 = String.format("%.2f", favourite2.minDiameterMile)
                                        tvDiameter1.text = "$minDiameter1 - $maxDiameter1 miles"
                                        tvDiameter2.text = "$minDiameter2 - $maxDiameter2 miles"
                                    }

                                    "Feet" -> {
                                        val minDiameter1 = String.format("%.2f", favourite1.minDiameterFeet)
                                        val maxDiameter1 = String.format("%.2f", favourite1.minDiameterFeet)
                                        val minDiameter2 = String.format("%.2f", favourite2.minDiameterFeet)
                                        val maxDiameter2 = String.format("%.2f", favourite2.minDiameterFeet)
                                        tvDiameter1.text = "$minDiameter1 - $maxDiameter1 ft"
                                        tvDiameter2.text = "$minDiameter2 - $maxDiameter2 ft"
                                    }
                                    else -> {
                                        val minDiameter1 = String.format("%.3f", favourite1.minDiameterKm)
                                        val maxDiameter1 = String.format("%.3f", favourite1.maxDiameterKm)
                                        val minDiameter2 = String.format("%.3f", favourite2.minDiameterKm)
                                        val maxDiameter2 = String.format("%.3f", favourite2.maxDiameterKm)
                                        tvDiameter1.text = "$minDiameter1 - $maxDiameter1 km"
                                        tvDiameter2.text = "$minDiameter2 - $maxDiameter2 km"
                                    }
                                }

                                tvCloseApproachData1.text = favourite1.closeApproachDate
                                tvCloseApproachData2.text = favourite2.closeApproachDate
                                tvOrbitingBody1.text = favourite1.orbitingBody
                                tvOrbitingBody2.text = favourite2.orbitingBody
                                tvPotentiallyDangerous1.text =
                                    if (favourite1.isPotentiallyHazardousAsteroid) {
                                        tvPotentiallyDangerous1.setTextColor(
                                            resources.getColor(
                                                dev.stupak.ui.R.color.secondaryRed,
                                                null,
                                            ),
                                        )
                                        "Yes"
                                    } else {
                                        "No"
                                    }
                                tvPotentiallyDangerous2.text =
                                    if (favourite2.isPotentiallyHazardousAsteroid) {
                                        tvPotentiallyDangerous2.setTextColor(
                                            resources.getColor(
                                                dev.stupak.ui.R.color.secondaryRed,
                                                null,
                                            ),
                                        )
                                        "Yes"
                                    } else {
                                        "No"
                                    }

                                when (state.settingsData.velocityUnit) {
                                    "Km/s" -> {
                                        tvRelativeVelocity1.text =
                                            String.format(
                                                "%.2f km/s",
                                                favourite1.relativeVelocityKmS.toDouble(),
                                            )
                                        tvRelativeVelocity2.text =
                                            String.format(
                                                "%.2f km/s",
                                                favourite2.relativeVelocityKmS.toDouble(),
                                            )
                                    }

                                    "Km/h" -> {
                                        tvRelativeVelocity1.text =
                                            String.format(
                                                "%.2f km/h",
                                                favourite1.relativeVelocityKmH.toDouble(),
                                            )
                                        tvRelativeVelocity2.text =
                                            String.format(
                                                "%.2f km/h",
                                                favourite2.relativeVelocityKmH.toDouble(),
                                            )
                                    }

                                    "Mi/h" -> {
                                        tvRelativeVelocity1.text =
                                            String.format(
                                                "%.2f mi/h",
                                                favourite1.relativeVelocityMilesH.toDouble(),
                                            )
                                        tvRelativeVelocity2.text =
                                            String.format(
                                                "%.2f mi/h",
                                                favourite2.relativeVelocityMilesH.toDouble(),
                                            )
                                    }

                                    else -> {
                                        tvRelativeVelocity1.text =
                                            String.format(
                                                "%.2f km/h",
                                                favourite1.relativeVelocityKmH.toDouble(),
                                            )
                                        tvRelativeVelocity2.text =
                                            String.format(
                                                "%.2f km/h",
                                                favourite2.relativeVelocityKmH.toDouble(),
                                            )
                                    }
                                }

                                tvAbsoluteMagnitude1.text =
                                    String.format("%.2f H", favourite1.absoluteMagnitudeH)
                                tvAbsoluteMagnitude2.text =
                                    String.format("%.2f H", favourite2.absoluteMagnitudeH)
                                tvSentryObject1.text =
                                    if (favourite1.isSentryObject) "Yes" else "No"
                                tvSentryObject2.text =
                                    if (favourite2.isSentryObject) "Yes" else "No"

                                when (state.settingsData.distanceUnit) {
                                    "Astronomical" -> {
                                        tvDistance1.text =
                                            String.format("%.2f au", favourite1.missDistanceAstronomical.toDouble())
                                        tvDistance2.text =
                                            String.format("%.2f au", favourite2.missDistanceAstronomical.toDouble())
                                    }

                                    "Lunar" -> {
                                        tvDistance1.text =
                                            String.format("%.2f LD", favourite1.missDistanceLunar.toDouble())
                                        tvDistance2.text =
                                            String.format("%.2f LD", favourite2.missDistanceLunar.toDouble())
                                    }

                                    "Kilometers" -> {
                                        tvDistance1.text =
                                            String.format("%.2f km", favourite1.missDistanceKm.toDouble())
                                        tvDistance2.text =
                                            String.format("%.2f km", favourite2.missDistanceKm.toDouble())
                                    }

                                    "Miles" -> {
                                        tvDistance1.text =
                                            String.format("%.2f miles", favourite1.missDistanceMiles.toDouble())
                                        tvDistance2.text =
                                            String.format("%.2f miles", favourite2.missDistanceMiles.toDouble())
                                    }

                                    else -> {
                                        tvDistance1.text =
                                            String.format("%.2f km", favourite1.missDistanceKm.toDouble())
                                        tvDistance2.text =
                                            String.format("%.2f km", favourite2.missDistanceKm.toDouble())
                                    }
                                }

                                cardFirstItem.setOnClickListener {
                                    navigateToFlow(null, true, "asteroids://app/${favourite1.id}/comparison")
                                }

                                cardSecondItem.setOnClickListener {
                                    navigateToFlow(null, true, "asteroids://app/${favourite2.id}/comparison")
                                }
                            }
                        } else {
                        onInfinitePageChangeCallback(state.favourites.size + 2)
                        viewPager2.adapter =
                            PagerAdapter(state.favourites, state.settingsData) { asteroidId ->
                                navigateToFlow(null, true, "asteroids://app/$asteroidId/comparison")
                            }
                        val initialPosition = state.favourites.indexOfFirst { it.id == asteroidId }
                        viewPager2.setCurrentItem(initialPosition + 1, false)
                    }
                }
            }
        }

        viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val paddingVertical = calculatePaddingFromScreenHeight(context.resources)
                setPadding(0, paddingVertical, 0, paddingVertical)
                clipToPadding = false
                clipChildren = false
            }
            val pageMargin = 16
            val marginPageTransformer = MarginPageTransformer(pageMargin)

            val scalePageTransformer =
                CompositePageTransformer().apply {
                    addTransformer { page, position ->
                        val scaleFactor = 0.9f
                        val normalizedPosition = abs(position)
                        val scale =
                            if (normalizedPosition < 1) {
                                1 - normalizedPosition * (1 - scaleFactor)
                            } else {
                                scaleFactor
                            }
                        page.scaleX = scale
                        page.scaleY = scale
                    }
                }

            setPageTransformer(
                CompositePageTransformer().apply {
                    addTransformer(marginPageTransformer)
                    addTransformer(scalePageTransformer)
                },
            )
        }

        configureButtons()
    }

    private fun onInfinitePageChangeCallback(listSize: Int) {
        viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)

                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        when (viewPager2.currentItem) {
                            listSize - 1 -> viewPager2.setCurrentItem(1, false)
                            0 -> viewPager2.setCurrentItem(listSize - 2, false)
                        }
                    }
                }
            },
        )
    }

    private fun configureButtons() {
        with(binding) {
            ctToolbar.onBackButtonClickListener =
                View.OnClickListener {
                    findNavController().popBackStack()
                }
        }
    }

    private fun calculatePaddingFromScreenHeight(resources: Resources): Int {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        return screenHeight / 3 - 70
    }
}

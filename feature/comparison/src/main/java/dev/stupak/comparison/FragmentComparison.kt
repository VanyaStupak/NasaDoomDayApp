package dev.stupak.comparison

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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
import dev.stupak.comparison.model.PagerUIModel
import dev.stupak.platform.base.BaseFragment
import dev.stupak.ui.ext.removeBrackets
import dev.stupak.ui.ext.showNothingToCompareSnackbar
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class FragmentComparison : BaseFragment(R.layout.fragment_comparison) {

    private val binding by viewBinding(FragmentComparisonBinding::bind)
    private lateinit var viewPager2: ViewPager2
    private val viewModel: ComparisonViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureUi(savedInstanceState: Bundle?) {

        viewPager2 = binding.viewPager
        val asteroidId: String? = arguments?.getString("id")
        lifecycleScope.launch {
            viewModel.favouritesListStateFlow.collect { favourites ->

                if (favourites.isNotEmpty()) {
                    if(favourites.size == 2){
                            binding.apply {
                                viewPager.visibility = View.GONE
                                twoItemsLayout.visibility = View.VISIBLE


                                val favourite1 = favourites[0]
                                val favourite2 = favourites[1]
                                tvName1.text = favourite1.name.removeBrackets()
                                tvName2.text = favourite2.name.removeBrackets()
                                val minDiameterKm1 = String.format("%.3f", favourite1.minDiameterKm)
                                val maxDiameterKm1 = String.format("%.3f", favourite1.maxDiameterKm)
                                val minDiameterKm2 = String.format("%.3f", favourite2.minDiameterKm)
                                val maxDiameterKm2 = String.format("%.3f", favourite2.maxDiameterKm)
                                tvDiameter1.text = "$minDiameterKm1 - $maxDiameterKm1 km"
                                tvDiameter2.text = "$minDiameterKm2 - $maxDiameterKm2 km"
                                tvCloseApproachData1.text = favourite1.closeApproachDate
                                tvCloseApproachData2.text = favourite2.closeApproachDate
                                tvOrbitingBody1.text = favourite1.orbitingBody
                                tvOrbitingBody2.text = favourite2.orbitingBody
                                tvPotentiallyDangerous1.text =
                                    if (favourite1.isPotentiallyHazardousAsteroid) {
                                        tvPotentiallyDangerous1.setTextColor(
                                            resources.getColor(
                                                dev.stupak.ui.R.color.secondaryRed,
                                                null
                                            )
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
                                                null
                                            )
                                        )
                                        "Yes"
                                    } else {
                                        "No"
                                    }

                                tvRelativeVelocity1.text =
                                    String.format(
                                        "%.2f km/h",
                                        favourite1.relativeVelocityKmH.toDouble()
                                    )
                                tvRelativeVelocity2.text =
                                    String.format(
                                        "%.2f km/h",
                                        favourite2.relativeVelocityKmH.toDouble()
                                    )
                                tvAbsoluteMagnitude1.text =
                                    String.format("%.2f H", favourite1.absoluteMagnitudeH)
                                tvAbsoluteMagnitude2.text =
                                    String.format("%.2f H", favourite2.absoluteMagnitudeH)
                                tvSentryObject1.text =
                                    if (favourite1.isSentryObject) "Yes" else "No"
                                tvSentryObject2.text =
                                    if (favourite2.isSentryObject) "Yes" else "No"
                                tvDistanceKilometers1.text =
                                    String.format("%.2f km", favourite1.missDistanceKm.toDouble())
                                tvDistanceKilometers2.text =
                                    String.format("%.2f km", favourite2.missDistanceKm.toDouble())

                                cardFirstItem.setOnClickListener {
                                    navigateToFlow(null, true, "asteroids://app/${favourite1.id}/comparison")
                                }

                                cardSecondItem.setOnClickListener {
                                    navigateToFlow(null, true, "asteroids://app/${favourite2.id}/comparison")
                                }
                            }
                    }
                    else  {
                        onInfinitePageChangeCallback(favourites.size + 2)
                        viewPager2.adapter = PagerAdapter(favourites) { asteroidId ->
                            navigateToFlow(null, true, "asteroids://app/$asteroidId/favourites")
                        }
                        val initialPosition = favourites.indexOfFirst { it.id == asteroidId }
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

            val scalePageTransformer = CompositePageTransformer().apply {
                addTransformer { page, position ->
                    val scaleFactor = 0.9f
                    val normalizedPosition = abs(position)
                    val scale = if (normalizedPosition < 1) {
                        1 - normalizedPosition * (1 - scaleFactor)
                    } else {
                        scaleFactor
                    }
                    page.scaleX = scale
                    page.scaleY = scale
                }
            }

            setPageTransformer(CompositePageTransformer().apply {
                addTransformer(marginPageTransformer)
                addTransformer(scalePageTransformer)
            })
        }



        configureButtons()

    }

    private fun onInfinitePageChangeCallback(listSize: Int) {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (  viewPager2.currentItem) {
                        listSize - 1 ->   viewPager2.setCurrentItem(1, false)
                        0 ->   viewPager2.setCurrentItem(listSize - 2, false)
                    }
                }
            }

        })
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
        return (screenHeight - 130) / 3
    }
}
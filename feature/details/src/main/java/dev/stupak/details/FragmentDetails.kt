package dev.stupak.details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.details.databinding.FragmentDetailsBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.platform.base.BaseFragment
import dev.stupak.ui.ext.removeBrackets
import dev.stupak.ui.ext.replaceMonthWithNumber
import dev.stupak.ui.ext.showSnackbar
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDetails : BaseFragment(R.layout.fragment_details) {
    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: DetailsViewModel by viewModels()
    private var scaleFactor = 1.0f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var lineChart: LineChart

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureUi(savedInstanceState: Bundle?) {
        configureButtons()
        observeDetailsData()
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        binding.linearAsteroidsPictures.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureButtons() {
        val asteroidId: String? = arguments?.getString(ASTEROID_ID_KEY)
        val fromFragment: String? = arguments?.getString(FROM_FRAGMENT_KEY)
        with(binding) {
            viewModel.getFavouriteAsteroid(asteroidId)
            ctToolbar.onBackButtonClickListener =
                View.OnClickListener {
                    if(fromFragment == "comparison"){
                        navigateToFlow(NavigationFlow.HostFlow, true)
                    }else {
                        findNavController().popBackStack()
                    }
                }

           btnCompare.setOnClickListener {
               lifecycleScope.launch {
                   if (viewModel.getFavouritesSize() == 1){
                       view?.showSnackbar(requireView(), SNACKBAR_TEXT)
                   } else{
                       navigateToFlow(NavigationFlow.ComparisonFlow, false, null, asteroidId)
                   }

               }
            }

            btnAddToFavourites.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    btnCompare.visibility = View.VISIBLE
                    tvName.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0,
                        dev.stupak.ui.R.drawable.ic_heart_full, 0
                    )
                    if (!viewModel.detailsData.value.isInFavourite) {
                        viewModel.addAsteroidToFavourites(asteroidId)
                    }
                } else {
                    btnCompare.visibility = View.GONE
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    if (viewModel.detailsData.value.isInFavourite) {
                        viewModel.removeAsteroidFromFavourites(asteroidId)
                    }
                    if (fromFragment == "favourites"){
                        findNavController().popBackStack()
                    }
                }
            }

            btnShowCharacteristics.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    cardAsteroidsCharacteristics.visibility = View.VISIBLE
                    btnShowCharacteristics.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0,
                        dev.stupak.ui.R.drawable.ic_arrow_up, 0
                    )
                } else {
                    cardAsteroidsCharacteristics.visibility = View.GONE
                    btnShowCharacteristics.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0,
                        dev.stupak.ui.R.drawable.ic_arrow_down, 0
                    )
                }
            }

            if (fromFragment == "push"){
                btnShowCharacteristics.isChecked = true
                nestedScroll.post {
                    nestedScroll.fullScroll(View.FOCUS_DOWN)
                }
                 linearChart.background = ContextCompat.getDrawable(requireContext(), dev.stupak.ui.R.drawable.bg_comparison_button_pressed)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun observeDetailsData() {
        val asteroidId: String? = arguments?.getString(ASTEROID_ID_KEY)
        val fromFragment: String? = arguments?.getString(FROM_FRAGMENT_KEY)
        viewModel.getAsteroidInfo(asteroidId, fromFragment)
        lifecycleScope.launch {
            viewModel.detailsData.collect { state ->
                val asteroid = state.asteroid
                if (asteroid != null) {
                    binding.apply {
                        tvName.text = asteroid.name.removeBrackets()
                        tvUniqueId.text = "id: ${asteroid.id}"
                        val minDiameterKm = String.format("%.3f", asteroid.minDiameterKm)
                        val maxDiameterKm = String.format("%.3f", asteroid.maxDiameterKm)

                        tvAsteroidDiameter.text =
                            "${asteroid.name.removeBrackets()} - $maxDiameterKm km"

                        val diameter =
                            ((minDiameterKm.toDouble() + maxDiameterKm.toDouble()) * 10).toInt()

                        val layoutParams = ivAsteroid.layoutParams
                        layoutParams.width = if (diameter < 7) {
                            7
                        } else {
                            diameter
                        }
                        layoutParams.height = if (diameter < 7) {
                            7
                        } else {
                            diameter
                        }
                        ivAsteroid.layoutParams = layoutParams

                        val layoutParamsCeres = ivCeres.layoutParams
                        layoutParamsCeres.width = 475
                        layoutParamsCeres.height = 475
                        ivCeres.layoutParams = layoutParamsCeres

                        tvDiameter.text = "$minDiameterKm - $maxDiameterKm km"
                        tvOrbitingBody.text = asteroid.orbitingBody
                        tvCloseApproachData.text =
                            asteroid.closeApproachDateFull.replaceMonthWithNumber()
                        tvPotentiallyDangerous.text =
                            if (asteroid.isPotentiallyHazardousAsteroid) {
                                tvPotentiallyDangerous.setTextColor(
                                    resources.getColor(dev.stupak.ui.R.color.secondaryRed, null)
                                )
                                "Yes"
                            } else {
                                "No"
                            }
                        tvRelativeVelocity.text =
                            String.format("%.2f km/h", asteroid.relativeVelocityKmH.toDouble())
                        tvAbsoluteMagnitude.text =
                            String.format("%.2f H", asteroid.absoluteMagnitudeH)
                        tvSentryObject.text = if (asteroid.isSentryObject) "Yes"
                        else {
                            "No"
                        }

                        tvDistanceKilometers.text =
                            String.format("%.2f km", asteroid.missDistanceKm.toDouble())

                        ctToolbar.onInfoButtonClickListener =
                            View.OnClickListener {
                                val url = asteroid.nasaJplUrl
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }
                        val missDistanceFromEarth = asteroid.missDistanceAstronomical.toFloat() + 1f
                        val list = listOf(
                            Entry(-1f, 1f),
                            Entry(1f, 1f),
                            Entry(missDistanceFromEarth, 1f)
                        )
                        initChart(list)
                    }

                }

                binding.btnAddToFavourites.isChecked = state.isInFavourite

            }

        }

    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {

            scaleFactor *= detector.scaleFactor

            scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)

            binding.linearAsteroidsPictures.scaleX = scaleFactor
            binding.linearAsteroidsPictures.scaleY = scaleFactor

            return true
        }
    }

    private fun initChart(entries: List<Entry>) {
        lineChart = binding.chLine

        lineChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textColor = Color.TRANSPARENT
            axisLineColor = Color.TRANSPARENT
        }
        lineChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textColor = Color.TRANSPARENT
            axisLineColor = Color.TRANSPARENT
        }

        lineChart.description.isEnabled = false

        lineChart.legend.isEnabled = false

        lineChart.xAxis.apply {
            setLabelCount(3, true)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(true)
        }

        val dataSet = LineDataSet(entries, null)
        dataSet.apply {
            color = resources.getColor(dev.stupak.ui.R.color.tertiaryViolet900, null)
            setDrawCircles(false)
            setDrawIcons(true)
            val sunIcon =
                ContextCompat.getDrawable(requireContext(), dev.stupak.ui.R.drawable.ic_sun)
            val earthIcon =
                ContextCompat.getDrawable(requireContext(), dev.stupak.ui.R.drawable.ic_earth)
            val asteroidIcon = ContextCompat.getDrawable(
                requireContext(),
                dev.stupak.ui.R.drawable.ic_asteroid_default
            )

            val markers = listOf(sunIcon, earthIcon, asteroidIcon)
            values = entries.mapIndexed { index, entry ->
                Entry(entry.x, entry.y, markers.getOrNull(index % markers.size))
            }

            lineWidth = 1.7f
            setDrawValues(false)
            setDrawFilled(false)
            isHighlightEnabled = false
            setDrawHorizontalHighlightIndicator(false)
        }

        lineChart.apply {
            setBackgroundColor(Color.WHITE)
            data = LineData(dataSet)
            invalidate()
        }
    }

    companion object {
        private const val SNACKBAR_TEXT = "There is nothing to compare this asteroid with yet"
        private const val ASTEROID_ID_KEY = "asteroid_id"
        private const val FROM_FRAGMENT_KEY = "from"
    }
}
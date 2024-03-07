package dev.stupak.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.details.databinding.FragmentDetailsBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.platform.base.BaseFragment
import dev.stupak.ui.ext.replaceMonthWithNumber
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDetails : BaseFragment(R.layout.fragment_details) {
    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: DetailsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureUi(savedInstanceState: Bundle?) {
        configureButtons()
        observeDetailsData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureButtons() {
        val asteroidId: String? = arguments?.getString(ASTEROID_ID_KEY)
        viewModel.getFavouriteAsteroid(asteroidId)
        binding.ctToolbar.onBackButtonClickListener =
            View.OnClickListener {
                findNavController().popBackStack()
            }
        binding.btnAddToFavourites.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!viewModel.detailsData.value.isInFavourite) {
                    viewModel.addAsteroidToFavourites(asteroidId)
                }
            } else {
                if(viewModel.detailsData.value.isInFavourite) {
                    viewModel.removeAsteroidFromFavourites(asteroidId)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun observeDetailsData() {
        val asteroidId: String? = arguments?.getString(ASTEROID_ID_KEY)
        val fromFragment: String? = arguments?.getString(FROM_FRAGMENT_KEY)
        viewModel.getAsteroidInfo(asteroidId,fromFragment)
        lifecycleScope.launch {
            viewModel.detailsData.collect { state ->
                val asteroid = state.asteroid
                if (asteroid != null) {
                    binding.apply {
                        tvName.text = asteroid.name
                        tvUniqueId.text = "id: ${asteroid.id}"
                        val minDiameterKm = String.format("%.3f", asteroid.minDiameterKm)
                        val maxDiameterKm = String.format("%.3f", asteroid.maxDiameterKm)
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
                        tvDistanceAstronomical.text =
                            String.format("%.2f au", asteroid.missDistanceAstronomical.toDouble())
                        tvDistanceLunar.text =
                            String.format("%.2f LD", asteroid.missDistanceLunar.toDouble())
                        tvDistanceKilometers.text =
                            String.format("%.2f km", asteroid.missDistanceKm.toDouble())
                        tvDistanceMiles.text =
                            String.format("%.2f mi", asteroid.missDistanceMiles.toDouble())
                        ctToolbar.onInfoButtonClickListener =
                            View.OnClickListener {
                                val url = asteroid.nasaJplUrl
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }
                    }
                }
                binding.btnAddToFavourites.isChecked = state.isInFavourite
            }

        }

    }

    companion object {
        private const val ASTEROID_ID_KEY = "asteroid_id"
        private const val FROM_FRAGMENT_KEY = "from"
    }
}
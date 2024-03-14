package dev.stupak.comparison.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.stupak.comparison.databinding.ItemComparisonBinding
import dev.stupak.comparison.model.PagerUIModel
import dev.stupak.comparison.model.SettingsDataUIModel
import dev.stupak.ui.ext.removeBrackets

class PagerAdapter(
    private val favourites: List<PagerUIModel>,
    private val settingsData: SettingsDataUIModel,
    private val itemClickListener: (String) -> Unit,
) :
    RecyclerView.Adapter<PagerAdapter.PagerViewHolder>() {
    private val newList: List<PagerUIModel> =
        listOf(favourites.last()) + favourites + listOf(favourites.first())

    class PagerViewHolder(binding: ItemComparisonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvName
        val tvDiameter: TextView = binding.tvDiameter
        val tvCloseApproachData: TextView = binding.tvCloseApproachData
        val tvPotentiallyDangerous: TextView = binding.tvPotentiallyDangerous
        val tvOrbitingBody: TextView = binding.tvOrbitingBody
        val tvRelativeVelocity: TextView = binding.tvRelativeVelocity
        val tvAbsoluteMagnitude: TextView = binding.tvAbsoluteMagnitude
        val tvSentryObject: TextView = binding.tvSentryObject
        val tvDistance: TextView = binding.tvDistance
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PagerViewHolder {
        val binding =
            ItemComparisonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: PagerViewHolder,
        position: Int,
    ) {
        val asteroid = newList[position]
        holder.apply {
            tvName.text = asteroid.name.removeBrackets()
            when (settingsData.diameterUnit) {
                "Meters" -> {
                    val minDiameter = String.format("%.2f", asteroid.minDiameterM)
                    val maxDiameter = String.format("%.2f", asteroid.maxDiameterM)
                    tvDiameter.text = "$minDiameter- $maxDiameter m"
                }

                "Kilometers" -> {
                    val minDiameter = String.format("%.3f", asteroid.minDiameterKm)
                    val maxDiameter = String.format("%.3f", asteroid.maxDiameterKm)
                    tvDiameter.text = "$minDiameter- $maxDiameter km"
                }

                "Miles" -> {
                    val minDiameter = String.format("%.2f", asteroid.minDiameterMile)
                    val maxDiameter = String.format("%.2f", asteroid.maxDiameterMile)
                    tvDiameter.text = "$minDiameter- $maxDiameter miles"
                }

                "Feet" -> {
                    val minDiameter = String.format("%.2f", asteroid.minDiameterFeet)
                    val maxDiameter = String.format("%.2f", asteroid.maxDiameterFeet)
                    tvDiameter.text = "$minDiameter- $maxDiameter ft"
                }
                else -> {
                    val minDiameter = String.format("%.3f", asteroid.minDiameterKm)
                    val maxDiameter = String.format("%.3f", asteroid.maxDiameterKm)
                    tvDiameter.text = "$minDiameter- $maxDiameter km"
                }
            }
            tvCloseApproachData.text = asteroid.closeApproachDate
            tvOrbitingBody.text = asteroid.orbitingBody
            tvPotentiallyDangerous.text =
                if (asteroid.isPotentiallyHazardousAsteroid) {
                    tvPotentiallyDangerous.setTextColor(
                        itemView.context.resources.getColor(
                            dev.stupak.ui.R.color.secondaryRed,
                            null,
                        ),
                    )
                    "Yes"
                } else {
                    "No"
                }

            when (settingsData.velocityUnit) {
                "Km/s" -> {
                    tvRelativeVelocity.text =
                        String.format(
                            "%.2f km/s",
                            asteroid.relativeVelocityKmS.toDouble(),
                        )
                }

                "Km/h" -> {
                    tvRelativeVelocity.text =
                        String.format(
                            "%.2f km/h",
                            asteroid.relativeVelocityKmH.toDouble(),
                        )
                }

                "Mi/h" -> {
                    tvRelativeVelocity.text =
                        String.format(
                            "%.2f mi/h",
                            asteroid.relativeVelocityMilesH.toDouble(),
                        )
                }

                else -> {
                    tvRelativeVelocity.text =
                        String.format(
                            "%.2f km/h",
                            asteroid.relativeVelocityKmH.toDouble(),
                        )
                }
            }
            tvAbsoluteMagnitude.text =
                String.format("%.2f H", asteroid.absoluteMagnitudeH)
            tvSentryObject.text =
                if (asteroid.isSentryObject) {
                    "Yes"
                } else {
                    "No"
                }

            when (settingsData.distanceUnit) {
                "Astronomical" -> {
                    tvDistance.text =
                        String.format("%.2f au", asteroid.missDistanceAstronomical.toDouble())
                }

                "Lunar" -> {
                    tvDistance.text =
                        String.format("%.2f LD", asteroid.missDistanceLunar.toDouble())
                }

                "Kilometers" -> {
                    tvDistance.text =
                        String.format("%.2f km", asteroid.missDistanceKm.toDouble())
                }

                "Miles" -> {
                    tvDistance.text =
                        String.format("%.2f miles", asteroid.missDistanceMiles.toDouble())
                }

                else -> {
                    tvDistance.text =
                        String.format("%.2f km", asteroid.missDistanceKm.toDouble())
                }
            }

            itemView.setOnClickListener {
                itemClickListener.invoke(asteroid.id)
            }
        }
    }

    override fun getItemCount(): Int = newList.size
}

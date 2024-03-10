package dev.stupak.comparison.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.stupak.comparison.R
import dev.stupak.comparison.databinding.ItemComparisonBinding
import dev.stupak.comparison.model.PagerUIModel
import dev.stupak.ui.ext.removeBrackets

class PagerAdapter(
    private val favourites: List<PagerUIModel>,
    private val itemClickListener: (String) -> Unit
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
        val tvDistanceKm: TextView = binding.tvDistanceKilometers
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
            val minDiameterKm = String.format("%.3f", asteroid.minDiameterKm)
            val maxDiameterKm = String.format("%.3f", asteroid.maxDiameterKm)
            tvDiameter.text = "$minDiameterKm - $maxDiameterKm km"
            tvCloseApproachData.text = asteroid.closeApproachDate
            tvOrbitingBody.text = asteroid.orbitingBody
            tvPotentiallyDangerous.text =
                if (asteroid.isPotentiallyHazardousAsteroid) {
                    tvPotentiallyDangerous.setTextColor(
                        itemView.context.resources.getColor(
                            dev.stupak.ui.R.color.secondaryRed,
                            null
                        )
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
            tvDistanceKm.text =
                String.format("%.2f km", asteroid.missDistanceKm.toDouble())

            itemView.setOnClickListener {
                itemClickListener.invoke(asteroid.id)
            }
        }
    }


    override fun getItemCount(): Int = newList.size
}
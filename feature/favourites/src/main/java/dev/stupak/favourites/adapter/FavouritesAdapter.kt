package dev.stupak.favourites.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.stupak.favourites.databinding.ItemFavouritesBinding
import dev.stupak.favourites.model.FavouritesUIModel
import dev.stupak.ui.ext.removeBrackets

class FavouritesAdapter(
    private var asteroids: List<FavouritesUIModel>,
    private val itemClickListener: (String) -> Unit,
) : RecyclerView.Adapter<FavouritesAdapter.AsteroidsViewHolder>() {
    class AsteroidsViewHolder(binding: ItemFavouritesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvName
        val tvDiameter: TextView = binding.tvDiameter
        val tvCloseApproachData: TextView = binding.tvCloseApproachData
        val tvPotentiallyDangerous: TextView = binding.tvPotentiallyDangerous
        val tvOrbitingBody: TextView = binding.tvOrbitingBody
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AsteroidsViewHolder {
        val binding =
            ItemFavouritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsteroidsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: AsteroidsViewHolder,
        position: Int,
    ) {
        val asteroid = asteroids[position]
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
            itemView.setOnClickListener {
                itemClickListener.invoke(asteroid.id)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<FavouritesUIModel>) {
        asteroids = newList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = asteroids.size
}

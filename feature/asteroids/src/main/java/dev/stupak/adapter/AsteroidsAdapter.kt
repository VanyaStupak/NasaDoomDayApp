package dev.stupak.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.stupak.asteroids.R
import dev.stupak.asteroids.databinding.ItemAsteroidBinding
import dev.stupak.model.AsteroidsUIModel
import dev.stupak.ui.ext.removeBrackets

class AsteroidsAdapter(
    private val itemClickListener: (String) -> Unit,
) :
    PagingDataAdapter<AsteroidsUIModel, AsteroidsAdapter.ViewHolder>(COMPARATOR) {
    class ViewHolder(private val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(asteroid: AsteroidsUIModel?) {
            binding.apply {
                tvName.text = asteroid?.name?.removeBrackets()
                val minDiameterKm = asteroid?.minDiameterKm?.let { String.format("%.3f", it) } ?: ""
                val maxDiameterKm = asteroid?.maxDiameterKm?.let { String.format("%.3f", it) } ?: ""
                tvDiameter.text = "$minDiameterKm - $maxDiameterKm km"
                tvOrbitingBody.text = asteroid?.orbitingBody
                tvCloseApproachData.text = asteroid?.closeApproachDate
                tvPotentiallyDangerous.text =
                    if (asteroid?.isPotentiallyHazardousAsteroid == true) "Yes"
                    else {
                        "No"
                    }
                if (asteroid?.isPotentiallyHazardousAsteroid == true) {
                    tvPotentiallyDangerous.setTextColor(
                        itemView.context.resources.getColor(
                            dev.stupak.ui.R.color.secondaryRed,
                            null,
                        )
                    )
                } else {
                    tvPotentiallyDangerous.setTextColor(
                        itemView.context.resources.getColor(
                            dev.stupak.ui.R.color.primaryBlack,
                            null,
                        )
                    )
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_asteroid, parent, false)
                val binding = ItemAsteroidBinding.bind(view)
                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val COMPARATOR =
            object : DiffUtil.ItemCallback<AsteroidsUIModel>() {
                override fun areItemsTheSame(
                    oldItem: AsteroidsUIModel,
                    newItem: AsteroidsUIModel,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AsteroidsUIModel,
                    newItem: AsteroidsUIModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            if (item != null) {
                Log.d("AsteroidsAdapter", "Clicked item with ID: ${item.id}")
                itemClickListener.invoke(item.id)
            }
        }
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder.create(parent)
    }
}

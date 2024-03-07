package dev.stupak.network.model

import com.google.gson.annotations.SerializedName

data class NearEarthObject(
    val links: Links,
    @SerializedName("element_count") val elementCount: Int,
    @SerializedName("near_earth_objects") val nearEarthObjects: Map<String, List<Asteroid>>
)

data class Links(
    val next: String,
    val previous: String,
    val self: String
)

data class Asteroid(
    val links: AsteroidLinks,
    val id: String,
    @SerializedName("neo_reference_id") val neoReferenceId: String,
    val name: String,
    @SerializedName("nasa_jpl_url") val nasaJplUrl: String,
    @SerializedName("absolute_magnitude_h") val absoluteMagnitudeH: Double,
    @SerializedName("estimated_diameter") val estimatedDiameter: EstimatedDiameter,
    @SerializedName("is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean,
    @SerializedName("close_approach_data") val closeApproachData: List<CloseApproachData>,
    @SerializedName("is_sentry_object") val isSentryObject: Boolean
)

data class AsteroidLinks(
    val self: String
)

data class EstimatedDiameter(
    val kilometers: DiameterUnit,
    val meters: DiameterUnit,
    val miles: DiameterUnit,
    val feet: DiameterUnit
)

data class DiameterUnit(
    @SerializedName("estimated_diameter_min") val estimatedDiameterMin: Double,
    @SerializedName("estimated_diameter_max") val estimatedDiameterMax: Double
)

data class CloseApproachData(
    @SerializedName("close_approach_date") val closeApproachDate: String,
    @SerializedName("close_approach_date_full") val closeApproachDateFull: String,
    @SerializedName("epoch_date_close_approach") val epochDateCloseApproach: Long,
    @SerializedName("relative_velocity") val relativeVelocity: Velocity,
    @SerializedName("miss_distance") val missDistance: MissDistance,
    @SerializedName("orbiting_body") val orbitingBody: String
)

data class Velocity(
    @SerializedName("kilometers_per_second") val kilometersPerSecond: String,
    @SerializedName("kilometers_per_hour") val kilometersPerHour: String,
    @SerializedName("miles_per_hour") val milesPerHour: String
)

data class MissDistance(
    val astronomical: String,
    val lunar: String,
    val kilometers: String,
    val miles: String
)
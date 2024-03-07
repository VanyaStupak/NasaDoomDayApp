package dev.stupak.source.model

data class AsteroidsSourceDBModel(
    val id: String,
    val name: String,
    val nasaJplUrl: String,
    val absoluteMagnitudeH: Double,
    val minDiameterKm: Double,
    val maxDiameterKm: Double,
    val minDiameterM: Double,
    val maxDiameterM: Double,
    val minDiameterMile: Double,
    val maxDiameterMile: Double,
    val minDiameterFeet: Double,
    val maxDiameterFeet: Double,
    val isPotentiallyHazardousAsteroid: Boolean,
    val closeApproachDate: String,
    val closeApproachDateFull: String,
    val closeApproachDateUnix: Long,
    val relativeVelocityKmS: String,
    val relativeVelocityKmH: String,
    val relativeVelocityMilesH: String,
    val missDistanceAstronomical: String,
    val missDistanceLunar: String,
    val missDistanceKm: String,
    val missDistanceMiles: String,
    val orbitingBody:String,
    val isSentryObject: Boolean
)

package dev.stupak.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class AsteroidsDBModel(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "nasaJplUrl")
    val nasaJplUrl: String,
    @ColumnInfo(name = "absoluteMagnitudeH")
    val absoluteMagnitudeH: Double,
    @ColumnInfo(name = "minDiameterKm")
    val minDiameterKm: Double,
    @ColumnInfo(name = "maxDiameterKm")
    val maxDiameterKm: Double,
    @ColumnInfo(name = "minDiameterM")
    val minDiameterM: Double,
    @ColumnInfo(name = "maxDiameterM")
    val maxDiameterM: Double,
    @ColumnInfo(name = "minDiameterMile")
    val minDiameterMile: Double,
    @ColumnInfo(name = "maxDiameterMile")
    val maxDiameterMile: Double,
    @ColumnInfo(name = "minDiameterFeet")
    val minDiameterFeet: Double,
    @ColumnInfo(name = "maxDiameterFeet")
    val maxDiameterFeet: Double,
    @ColumnInfo(name = "isPotentiallyDangerous")
    val isPotentiallyHazardousAsteroid: Boolean,
    @ColumnInfo(name = "closeApproachDate")
    val closeApproachDate: String,
    @ColumnInfo(name = "closeApproachDateFull")
    val closeApproachDateFull: String,
    @ColumnInfo(name = "closeApproachDateUnix")
    val closeApproachDateUnix: Long,
    @ColumnInfo(name = "relativeVelocityKmS")
    val relativeVelocityKmS: String,
    @ColumnInfo(name = "relativeVelocityKmH")
    val relativeVelocityKmH: String,
    @ColumnInfo(name = "relativeVelocityMilesH")
    val relativeVelocityMilesH: String,
    @ColumnInfo(name = "missDistanceAstronomical")
    val missDistanceAstronomical: String,
    @ColumnInfo(name = "missDistanceLunar")
    val missDistanceLunar: String,
    @ColumnInfo(name = "missDistanceKm")
    val missDistanceKm: String,
    @ColumnInfo(name = "missDistanceMiles")
    val missDistanceMiles: String,
    @ColumnInfo(name = "orbitingBody")
    val orbitingBody:String,
    @ColumnInfo(name = "isSentryObject")
    val isSentryObject: Boolean
)

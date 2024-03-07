package dev.stupak.details.model

import dev.stupak.domain.model.AsteroidsDomainModel

fun AsteroidsDomainModel.toAsteroidsDetailsUiModel(): AsteroidsDetailsUIModel {
    return AsteroidsDetailsUIModel(
        id = id,
        name = name,
        nasaJplUrl = nasaJplUrl,
        absoluteMagnitudeH = absoluteMagnitudeH,
        minDiameterKm = minDiameterKm,
        maxDiameterKm = maxDiameterKm,
        minDiameterM = minDiameterM,
        maxDiameterM = maxDiameterM,
        minDiameterMile = minDiameterMile,
        maxDiameterMile = maxDiameterMile,
        minDiameterFeet = minDiameterFeet,
        maxDiameterFeet = maxDiameterFeet,
        isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid,
        closeApproachDate = closeApproachDate,
        closeApproachDateFull = closeApproachDateFull,
        closeApproachDateUnix = closeApproachDateUnix,
        relativeVelocityKmS = relativeVelocityKmS,
        relativeVelocityKmH = relativeVelocityKmH,
        relativeVelocityMilesH = relativeVelocityMilesH,
        missDistanceAstronomical = missDistanceAstronomical,
        missDistanceLunar = missDistanceLunar,
        missDistanceKm = missDistanceKm,
        missDistanceMiles = missDistanceMiles,
        orbitingBody = orbitingBody,
        isSentryObject = isSentryObject
    )
}
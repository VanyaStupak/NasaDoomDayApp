package dev.stupak.source.model

import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.database.model.FavouritesDBModel
import dev.stupak.network.model.Asteroid

fun AsteroidsDBModel.toAsteroidsSourceDBModel(): AsteroidsSourceDBModel {
    return AsteroidsSourceDBModel(
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

fun FavouritesDBModel.toFavouritesSourceDBModel(): FavouritesSourceDBModel {
    return FavouritesSourceDBModel(
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

fun Asteroid.toAsteroidsSourceNetModel(): AsteroidsSourceNetModel {
    return AsteroidsSourceNetModel(
        id = id,
        name = name,
        nasaJplUrl = nasaJplUrl,
        absoluteMagnitudeH = absoluteMagnitudeH,
        minDiameterKm = estimatedDiameter.kilometers.estimatedDiameterMin,
        maxDiameterKm = estimatedDiameter.kilometers.estimatedDiameterMax,
        minDiameterM =   estimatedDiameter.meters.estimatedDiameterMin,
        maxDiameterM =  estimatedDiameter.meters.estimatedDiameterMax,
        minDiameterMile = estimatedDiameter.miles.estimatedDiameterMin,
        maxDiameterMile = estimatedDiameter.miles.estimatedDiameterMax,
        minDiameterFeet = estimatedDiameter.feet.estimatedDiameterMin,
        maxDiameterFeet = estimatedDiameter.feet.estimatedDiameterMax,
        isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid,
        closeApproachDate = closeApproachData.first().closeApproachDate,
        closeApproachDateFull = closeApproachData.first().closeApproachDateFull,
        closeApproachDateUnix = closeApproachData.first().epochDateCloseApproach,
        relativeVelocityKmS = closeApproachData.first().relativeVelocity.kilometersPerSecond,
        relativeVelocityKmH = closeApproachData.first().relativeVelocity.kilometersPerHour,
        relativeVelocityMilesH = closeApproachData.first().relativeVelocity.milesPerHour,
        missDistanceAstronomical = closeApproachData.first().missDistance.astronomical,
        missDistanceLunar = closeApproachData.first().missDistance.lunar,
        missDistanceKm = closeApproachData.first().missDistance.kilometers,
        missDistanceMiles = closeApproachData.first().missDistance.miles,
        orbitingBody = closeApproachData.first().orbitingBody,
        isSentryObject = isSentryObject
    )
}

fun AsteroidsSourceNetModel.toAsteroidsDBModel(): AsteroidsDBModel {
    return AsteroidsDBModel(
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
package dev.stupak.settings.model

import dev.stupak.domain.model.SettingsDataDomainModel

fun SettingsDataDomainModel.toSettingsUIModel(): SettingsDataUIModel {
    return SettingsDataUIModel(
        diameterUnit = diameterUnit,
        velocityUnit = velocityUnit,
        distanceUnit = distanceUnit,
        pushInterval = pushInterval,
    )
}

fun SettingsDataUIModel.toSettingsDataDomainModel(): SettingsDataDomainModel {
    return SettingsDataDomainModel(
        diameterUnit = diameterUnit,
        velocityUnit = velocityUnit,
        distanceUnit = distanceUnit,
        pushInterval = pushInterval,
    )
}

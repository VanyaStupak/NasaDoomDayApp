package dev.stupak.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.stupak.local.DataStoreManager
import dev.stupak.repository.FavouritesRepository
import dev.stupak.ui.ext.removeBrackets
import dev.stupak.ui.ext.removeLastChar
import dev.stupak.ui.ext.replaceMonthWithNumber
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@HiltWorker
class AsteroidWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParams: WorkerParameters,
        private val favouritesRepository: FavouritesRepository,
        private val dataStoreManager: DataStoreManager,
    ) :
    CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {
            return try {
                val currentTime = Calendar.getInstance().time
                val calendar = Calendar.getInstance()
                calendar.time = currentTime
                calendar.add(
                    Calendar.HOUR_OF_DAY,
                    dataStoreManager.getSettingsData().first()?.pushInterval?.removeLastChar()?.toInt() ?: 24,
                )

                val timePlus24 = calendar.time
                val asteroidList = favouritesRepository.getAsteroidsList().first()
                asteroidList.forEach { asteroid ->
                    val asteroidDateTime =
                        parseDateTime(asteroid.closeApproachDateFull.replaceMonthWithNumber())

                    if (!asteroid.notificationSent && asteroidDateTime in currentTime..timePlus24) {
                        sendNotification(
                            "WARNING!",
                            "An asteroid ${asteroid.name.removeBrackets()} is approaching!",
                            asteroid.id,
                        )

                        asteroid.notificationSent = true

                        favouritesRepository.updateAsteroid(asteroid)
                    }
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }

        private fun sendNotification(
            title: String,
            message: String,
            asteroidId: String,
        ) {
            if (checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel =
                        NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_DEFAULT,
                        )
                    channel.enableLights(true)
                    channel.lightColor = Color.GREEN
                    notificationManager.createNotificationChannel(channel)
                }

                val deepLinkUri = Uri.parse("asteroids://app/$asteroidId/push")

                val pendingIntent =
                    PendingIntent.getActivity(
                        applicationContext,
                        asteroidId.hashCode(),
                        Intent(Intent.ACTION_VIEW, deepLinkUri),
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
                    )
                val notificationBuilder =
                    NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                        .setSmallIcon(dev.stupak.ui.R.drawable.ic_warning_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                notificationManager.notify(asteroidId.hashCode(), notificationBuilder.build())
            }
        }

        private fun parseDateTime(dateTimeString: String): Date {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            return format.parse(dateTimeString) ?: Date()
        }

        companion object {
            private const val WORK_TAG = "MyWork"
            const val CHANNEL_ID = "default_channel"
            const val CHANNEL_NAME = "default_name"

            fun createPeriodicRequest(): PeriodicWorkRequest {
                return PeriodicWorkRequestBuilder<AsteroidWorker>(1, TimeUnit.HOURS)
                    .addTag(WORK_TAG)
                    .build()
            }
        }
    }

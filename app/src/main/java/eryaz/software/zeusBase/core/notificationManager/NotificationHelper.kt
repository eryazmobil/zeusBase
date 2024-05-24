package eryaz.software.zeusBase.core.notificationManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationHelper {

    companion object {
         const val CHANNEL_ID = "Zeus"
         private const val CHANNEL_NAME = "ZeusWms"
         private const val CHANNEL_DESCRIPTION = "ZeusWmsApp"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = CHANNEL_DESCRIPTION
                val notificationManager = context.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
    }


}
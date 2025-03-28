package isil.edu.pe.integracion.sesion1.demo1.messaging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import isil.edu.pe.integracion.sesion1.demo1.MainActivity
import isil.edu.pe.integracion.sesion1.demo1.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Log the message payload
        Log.d("FCM", "From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Message Body: ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, message: String?) {
        val channelId = "default_channel"
        // Crear una notificación con PendingIntent
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                                                      PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        // Crear la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)  // Asegúrate de tener este ícono en tu carpeta `drawable`
            .setContentTitle(title ?: "Nueva notificación")
            .setContentText(message ?: "Mensaje recibido")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        // Crear el canal de notificaciones para versiones de Android Oreo y superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
        // Mostrar la notificación
        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(0, notificationBuilder.build())
        }
    }

}

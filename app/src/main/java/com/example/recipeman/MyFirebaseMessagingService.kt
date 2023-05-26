package com.example.recipeman

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.recipeman"

class MyFirebaseMessagingService : FirebaseMessagingService() {

//    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
//        sendRegistrationToServer(token)

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        generateNotification(message.notification!!.title!!, message.notification!!.body!!)

    }

// TODO: think how to solve all of this
/*
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_icon"
            android:resource="@drawable/chef_recipes_book"/>
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_color"
            android:resource="@color/dark_text"/>
        <service android:name=".MyFirebaseMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT"/>
            </intent-filter>
        </service>

*/

//    @RequiresApi(Build.VERSION_CODES.S)
//    @SuppressLint("UnspecifiedImmutableFlag")
//    fun generateNotification(title: String, content: String) {
//        val intent = Intent(this, AccessActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        var builder = NotificationCompat.Builder(applicationContext, channelId)
//            .setSmallIcon(R.drawable.chef_recipes_book)
//            .setAutoCancel(true)
//            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//
//        builder = builder.setContent(getRemoteView(title, content))
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        notificationManager.notify(0, builder.build())
//    }

//    private fun getRemoteView(title: String, content: String): RemoteViews {
//        val remoteView = RemoteViews("com.example.recipeman", R.layout.notification)
//        remoteView.setTextViewText(R.id.recipeManTitle, title)
//        remoteView.setTextViewText(R.id.recipeManContent, content)
//        remoteView.setImageViewResource(R.id.recipeManLogo, R.drawable.chef_recipes_book)
//        return remoteView
//    }

    @SuppressLint("MissingPermission")
    private fun generateNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(this, "MyFirebaseChannel")
            .setSmallIcon(R.drawable.chef_recipes_book)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
        notificationManager.notify(101, builder.build())
    }
}
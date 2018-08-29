package com.smredlabs.myapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.AsyncTask
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import java.time.LocalDateTime

class MyJobService : JobService(){

    val CHANNEL_ID = "Noti12141"

    override fun onStopJob(job: JobParameters?): Boolean {
        if(AsynTaskExample() != null){
            AsynTaskExample().cancel(true)
        }
        return true
    }

    override fun onStartJob(job: JobParameters?): Boolean {
        AsynTaskExample().execute(job)
        return true
    }

    inner class AsynTaskExample : AsyncTask<JobParameters, String, JobParameters>(){
        override fun doInBackground(vararg params: JobParameters?): JobParameters {
            println("Job Started... ")
            val intent = Intent(this@MyJobService, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this@MyJobService, 0, intent, 0)

            val mBuilder = NotificationCompat.Builder(this@MyJobService, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("My notification")
                    .setContentText("Much longer text that cannot fit one line...")
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line so that I am extending this line to fill more than one line"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)

            // Step 4: Show the notification
            val notificationManager = NotificationManagerCompat.from(this@MyJobService)
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(12141, mBuilder.build())
            return params[0]!!
//            jobFinished(params[0]!!,false)
        }

        override fun onPostExecute(result: JobParameters?) {
            jobFinished(result!!,false)
        }
    }

}
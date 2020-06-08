package dial.babyradio.cadena

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dial.babyradio.DialBabyRadio
import dial.babyradio.R

class ServceRadioBudilnik : Service() {
    var dataTimeString = ""
    var title          = ""
    var nameRadio      = ""
    var offSound       = ""

    override fun onCreate() {
        super.onCreate()

        dataTimeString    = App.sharedPreferences.getString("dataTimeString", "Error").toString()
        val use    = resources.getString(R.string.installlition)
        nameRadio        = "Despertador " + App.sharedPreferences.getString("nameStationShow", "none").toString()
        title             = "$use $dataTimeString"
        offSound          = getString(R.string.offSound)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notIntent = Intent(this, DialBabyRadio::class.java)
        val pendIntent = PendingIntent.getActivity(this, 19, notIntent, 0)

        App.notification = NotificationCompat.Builder(this, App.channel_id_alarm)
            .setContentTitle(nameRadio)
            .setContentText(title)
            .setSmallIcon(R.drawable.alarm)
            .setContentIntent(pendIntent)
            .build()
        startForeground(312, App.notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? { return null }
}
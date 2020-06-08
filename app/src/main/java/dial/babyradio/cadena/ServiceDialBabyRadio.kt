package dial.babyradio.cadena
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dial.babyradio.cadena.App.Companion.playExoplayer
import dial.babyradio.cadena.App.Companion.url

class ServiceDialBabyRadio: Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(111, App.news)
        playExoplayer(url, this)
        return START_STICKY
    }



}
package dial.babyradio.cadena

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class AutoOffReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "Radio Apagada", Toast.LENGTH_LONG).show()

        if(App.player.isPlaying) App.stop()
        if(App.mp.isPlaying)     App.mpStop()


    }
}
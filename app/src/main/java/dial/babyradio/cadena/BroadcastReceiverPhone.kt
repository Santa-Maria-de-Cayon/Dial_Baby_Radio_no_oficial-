package dial.babyradio.cadena

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log


class BroadcastReceiverPhone : BroadcastReceiver() {



    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {

        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

        if (state == null) {
            //Outgoing call
            val number = intent?.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
          //  Log.e("tag", "Outgoing number : " + number);

        } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
          //  Log.e("tag", "EXTRA_STATE_OFFHOOK")
          //  Toast.makeText(context, "state = call started", Toast.LENGTH_LONG ).show()
            if ( App.player.isPlaying){
                App.player.volume = 0F
            }


        } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
            Log.e("tag", "EXTRA_STATE_IDLE")
          //  Toast.makeText(context, "state = call ended", Toast.LENGTH_LONG ).show()
            if ( App.player.isPlaying){
                App.player.volume = 1F
            }

        } else if (state == TelephonyManager.EXTRA_STATE_RINGING) {

            //Incoming call
            val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
          //  Log.e("tag", "Incoming number : $number")

        } else
            Log.e("tag", "none")
    }
}

// if (player.volume == 0F) {
//     player.volume = 1F
// } else {
//     player.volume = 0F
// }
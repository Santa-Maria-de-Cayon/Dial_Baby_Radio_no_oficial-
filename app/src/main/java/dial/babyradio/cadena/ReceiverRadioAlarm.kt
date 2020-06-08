package dial.babyradio.cadena
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import dial.babyradio.AlarmRadioBuiltin

class ReceiverRadioAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context?.applicationContext, AlarmRadioBuiltin::class.java)
            i.addFlags(FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
       context?.startActivity(i)




    }
}
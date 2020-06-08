package dial.babyradio
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dial.babyradio.cadena.AutoOffReceiver
import kotlinx.android.synthetic.main.auto_off_selection.*

class AutoOffSelection : AppCompatActivity() {
    private var milliseconds : Long = 0
    private lateinit var alarmManager: AlarmManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auto_off_selection)

        title = "Apagado automático"

        off15.setOnClickListener {
            setOff(this, 900000) // 1800000
            finish()
        }

        off30.setOnClickListener {
            setOff(this, 1800000) // 1800 000
            finish()
        }

        off60.setOnClickListener {
            setOff(this, 3600000) // 1800 000
            finish()
        }

        off90.setOnClickListener {
            setOff(this, 5400000) // 1800 000
            finish()
        }


    }

    private fun setOff(c: Context, m: Long ){
        milliseconds = System.currentTimeMillis() + m
        alarmManager = c.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(c, AutoOffReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(c, 31, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                milliseconds,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent)
        }
    }
}

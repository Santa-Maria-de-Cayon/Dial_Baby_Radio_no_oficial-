package dial.babyradio.cadena
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import dial.babyradio.R
import kotlinx.android.synthetic.main.custom.view.*
import java.util.*
class Utils(val c : Context) {
    private val alarmManager: AlarmManager = c.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    var nameRadio = ""
    var dataTimeString = ""


    @SuppressLint("InflateParams")
    fun startAlarm() {
        val mDialogView = LayoutInflater.from(c).inflate(R.layout.custom, null)
        val builder = AlertDialog.Builder(c).setView(mDialogView) // .setTitle("")
        val alertDialog = builder.show()
        val titleDialogText = c.resources.getString(R.string.titleDialogText)
        mDialogView.titleCustom.text = titleDialogText
        mDialogView.numberPicker_hours.minValue = 0
        mDialogView.numberPicker_hours.maxValue = 23
        mDialogView.numberPicker_hours.setFormatter { String.format("%02d", it) }
        mDialogView.numberPicker_minute.minValue = 0
        mDialogView.numberPicker_minute.maxValue = 59
        mDialogView.numberPicker_minute.setFormatter { String.format("%02d", it) }

        val setHour = App.sharedPreferences.getInt("data_hour", 111)
        val setMin = App.sharedPreferences.getInt("data_minute", 111)
        if (setHour != 111) {
            mDialogView.numberPicker_minute.value = setMin
            mDialogView.numberPicker_hours.value = setHour
        }

        mDialogView.cancelDialog.setOnClickListener { alertDialog.dismiss() }

        mDialogView.goToWork.setOnClickListener {
           setAlarm(mDialogView, alertDialog)
        }

        val valueChecked = App.sharedPreferences.getBoolean("valueChecked", false)
        mDialogView.switch_button.isChecked = valueChecked

        mDialogView.switch_button.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("tag", " " + isChecked)
            if(isChecked){
                setAlarm(mDialogView, alertDialog)
            }
            if(!isChecked){
                App.edit.putBoolean("valueChecked", isChecked); App.edit.apply()
                val intent = Intent(c, ReceiverRadioAlarm::class.java)
                val pendingIntent = PendingIntent.getBroadcast(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.cancel(pendingIntent)
                c.stopService(Intent(c, ServceRadioBudilnik::class.java))
            }
        }
    }

    private fun setAlarm(mDialogView: View, alertDialog: AlertDialog) {
        App.edit.putBoolean("valueChecked", true); App.edit.apply()
        val i = Intent(c, ServceRadioBudilnik::class.java)
        c.stopService(i)
        val hours = mDialogView.numberPicker_hours.value
        val minute = mDialogView.numberPicker_minute.value
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hours
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        var milliseconds = calendar.timeInMillis
        if (milliseconds < System.currentTimeMillis()) milliseconds += 86400000
        val intent = Intent(c, ReceiverRadioAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                milliseconds,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent)
        }

        App.edit.putLong("dataTimeLongSetAlarm", milliseconds)
        App.edit.putLong("dataTimeLong", milliseconds)
        App.edit.putInt("data_hour", hours)
        App.edit.putInt("data_minute", minute)
        App.edit.apply()

        nameRadio = App.sharedPreferences.getString("nameStationShow", "Dial Baby Radio").toString()
        val ho = if (hours < 10) "0$hours"
        else "" + hours
        val mi = if (minute < 10) "0$minute"
        else "" + minute
        val use = c.resources.getString(R.string.use) + " $ho:$mi \n\n\n $nameRadio"
        Toast.makeText(c, use, Toast.LENGTH_LONG).show()
        dataTimeString = "$ho:$mi"
        App.edit.putString("dataTimeString", dataTimeString); App.edit.apply()
        setVisibilityAlarm()
        ContextCompat.startForegroundService(c, i)
        alertDialog.dismiss()
    }

    private fun setVisibilityAlarm() {

    }



}
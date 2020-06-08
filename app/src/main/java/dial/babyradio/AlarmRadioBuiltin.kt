package dial.babyradio
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import dial.babyradio.cadena.App
import dial.babyradio.cadena.ReceiverRadioAlarm
import kotlinx.android.synthetic.main.alarm_radio_budilnik.*

class AlarmRadioBuiltin : AppCompatActivity() {
    private lateinit var alarmManager: AlarmManager
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_radio_budilnik)

        title = "Despertador Activo"



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else { this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON) }


        alarmManager      = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dataTimeLong  = App.sharedPreferences.getLong("dataTimeLong", 1)
        val next = dataTimeLong + 86400000
        App.edit.putLong("dataTimeLong",  next); App.edit.apply()
        val intent        = Intent(applicationContext, ReceiverRadioAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next, pendingIntent)
        } else { alarmManager.setExact(AlarmManager.RTC_WAKEUP, next, pendingIntent) }


       // webView2.webViewClient = WebViewClient()
       //  val data : Uri? = intent.data
       // webView2.loadUrl("https://diseno-web-cantabria.web.app/")




        val puth    = App.sharedPreferences.getString("url", "http://sd396babyradio1.lcinternet.com:8000/live").toString()
        if (isInternetAvailable(this)){
            App.play(this, puth)
        } else App.mpPlay(this)


        off_r.setOnClickListener{
            if (App.player.isPlaying) App.player.stop()
            if (App.mp.isPlaying)     App.mp.stop()
            finish()
            startActivity(Intent(this, DialBabyRadio::class.java))
        }


    }


    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }


}


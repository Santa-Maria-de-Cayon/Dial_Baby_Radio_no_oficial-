package dial.babyradio.cadena
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dial.babyradio.DialBabyRadio
import dial.babyradio.R

class App : Application() {
    companion object{
        lateinit var news: Notification
        lateinit var notification: Notification
        lateinit var sharedPreferences: SharedPreferences
        lateinit var edit: SharedPreferences.Editor
        lateinit var mp: MediaPlayer
        var nameStationShow = "Los 40"
        var url = "http://sd396babyradio1.lcinternet.com:8000/live"
        var playImageVisible = true
        const val channel_id = "baby radio"
        val channel_id_alarm = "Radio_Budilnik"
        const val name = "Baby Radio"
        lateinit var player : SimpleExoPlayer

        fun playExoplayer(s:String, context: Context){
            val content : ProgressiveMediaSource = ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(context , "Mozilla")
            ).createMediaSource(Uri.parse(s))
            player.prepare(content)
            player.playWhenReady = true
        }

        fun stopRadio(){
            if(player.isPlaying){
                player.stop()
            }
        }

        fun play(c: Context, s: String){
            // player = ExoPlayerFactory.newSimpleInstance(c)
            player.playWhenReady = true
            player.prepare(ProgressiveMediaSource.Factory(DefaultDataSourceFactory(c, "Mozilla")).createMediaSource(Uri.parse(s)))
        }
        fun stop(){
            player.playWhenReady = false
            //    player.release()
        }
        fun mpPlay(c: Context){
            //   val n        = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //   mp           = MediaPlayer.create(c, n)
            mp.isLooping = true
            mp.start()
        }
        fun mpStop(){
            if(mp.isPlaying) {
                mp.stop()
                mp.release()
            }
        }
    }


    override fun onCreate(){
        super.onCreate()
        val n   = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mp           = MediaPlayer.create(this, n)
        player = ExoPlayerFactory.newSimpleInstance(this)

        sharedPreferences = getSharedPreferences("tag", Context.MODE_PRIVATE)
        edit              = sharedPreferences.edit()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serChannel = NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serChannel)

            val serChannel1 = NotificationChannel(channel_id_alarm, name, NotificationManager.IMPORTANCE_LOW)
            val manager1 = getSystemService(NotificationManager::class.java)
            manager1?.createNotificationChannel(serChannel1)
        }

        val aplication = getString(R.string.app_name)
        val ads = getString(R.string.ads)
        val notIntent = Intent(this, DialBabyRadio::class.java)
        notIntent.putExtra("stop", "stop")
        val pendIntent = PendingIntent.getActivity(this, 123, notIntent, 0)
         news = NotificationCompat.Builder(this, channel_id)
            .setContentTitle(aplication)
            .setContentText(ads)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendIntent)
            .build()

    }

}
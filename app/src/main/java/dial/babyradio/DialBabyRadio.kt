package dial.babyradio

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dial.babyradio.cadena.*
import dial.babyradio.cadena.App.Companion.nameStationShow
import dial.babyradio.cadena.App.Companion.playImageVisible
import dial.babyradio.cadena.App.Companion.url
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL


class DialBabyRadio : AppCompatActivity() {
    private lateinit var nameStations: Array<String>
    private lateinit var urlStations:  Array<String>

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try { this.supportActionBar!!.hide() } catch (e: Exception) { }
        setContentView(R.layout.activity_main)


      if(playImageVisible){
          stop.visibility = View.GONE
          play.visibility = View.VISIBLE
      } else {
          play.visibility = View.GONE
          stop.visibility = View.VISIBLE
      }
        play.setOnClickListener {
          playImageVisible = false
          play.visibility = View.GONE
          stop.visibility = View.VISIBLE
          ContextCompat.startForegroundService(this, Intent(this, ServiceDialBabyRadio::class.java))
      }
        stop.setOnClickListener {
          playImageVisible = true
          stop.visibility = View.GONE
          play.visibility = View.VISIBLE
          stopService(Intent(this, ServiceDialBabyRadio::class.java))
          App.stopRadio()
      }
        web.setOnClickListener{
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://alexei-developer.blogspot.com/")))
      }
        value.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=dial.babyradio")))
        }
        share.setOnClickListener{
            val s     = Intent()
            s.action = Intent.ACTION_SEND
            s.type   = "text/plain"
            val name = resources.getString(R.string.app_name)
            s.putExtra(Intent.EXTRA_TEXT, "$name \n https://play.google.com/store/apps/details?id=dial.babyradio")
            startActivity(Intent.createChooser(s, ""))
        }
        alarm.setOnClickListener{
            Utils(this).startAlarm()
         // startActivity(Intent(this, SetTimeAlarm::class.java))
        }

        val readRadioStation = Thread{
            try{
                val response     = URL("https://diseno-desarrollo-web-app-cantabria.github.io/babyradio/baby.js").readText()
                val res          = JSONObject(response)
                val name         = res.getJSONArray("name")
                nameStations = Array(name.length()){name.getString(it)}
                val url          = res.getJSONArray("uri")
                urlStations  = Array(url.length()){url.getString(it)}
                relativeLayoutGeneral.post{
                    mainListView.adapter = CustomAdapter(this, nameStations)
                }
            } catch (e: java.lang.Exception){ Log.d("tag", "Error -> " + e.message)}
        }
        readRadioStation.start()


        mainListView.setOnItemClickListener{ _ , _ , position, _ ->
            nameStationShow = nameStations[position]
            url             = urlStations[position]
            playImageVisible = false
            play.visibility = View.GONE
            stop.visibility = View.VISIBLE
            Toast.makeText(this, nameStationShow, Toast.LENGTH_LONG).show()
            App.edit.putString("url", url)
            App.edit.putString("nameStationShow", nameStationShow)
            App.edit.apply()
            ContextCompat.startForegroundService(this, Intent(this, ServiceDialBabyRadio::class.java))
        }


        val thread = Thread{
            try{
                url = URL("https://diseno-desarrollo-web-app-cantabria.github.io/los40/only_los40.txt").readText()
            } catch (e: java.lang.Exception){}
        }
        thread.start()



     //  val imageArray = arrayOf(R.drawable.a0, R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25, R.drawable.a26, R.drawable.a27, R.drawable.a28, R.drawable.a29, R.drawable.a30, R.drawable.a31)
     //  val rand = (imageArray.indices).random()
     //  relativeLayoutGeneral.background = ContextCompat.getDrawable(this, imageArray[rand] )

        auto_off_sel.setOnClickListener{
            startActivity(Intent(this, AutoOffSelection::class.java))
        }




        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 11)
        }


    }
}



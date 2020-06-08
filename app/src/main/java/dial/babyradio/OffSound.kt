package dial.babyradio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dial.babyradio.cadena.App

class OffSound : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.off_sound)

        if(App.player.isPlaying) App.stop()
        if(App.mp.isPlaying)     App.mpStop()

        val message = getString(R.string.message)
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()

        finish()
    }
}

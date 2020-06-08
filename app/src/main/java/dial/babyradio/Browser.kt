package dial.babyradio
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_brouser_cadena_dial.*


class Browser : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brouser_cadena_dial)

        webView.webChromeClient = WebChromeClient()
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        val data = intent.data
        webView.loadUrl(data.toString())
    }
}

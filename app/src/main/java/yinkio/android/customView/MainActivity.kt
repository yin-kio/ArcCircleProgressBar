package yinkio.android.customView

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import yinkio.android.customView.progressBar.ArcCircleProgressBar

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar: ArcCircleProgressBar = findViewById(R.id.progressBar)
        val decreaseProgress: Button = findViewById(R.id.decreaseProgress)
        val increaseProgress: Button = findViewById(R.id.increaseProgress)
        val textView: TextView = findViewById(R.id.textView)
        decreaseProgress.setOnClickListener {
            progressBar.progress -= 1
            textView.text = "${progressBar.progress}%"
        }

        increaseProgress.setOnClickListener {
            progressBar.progress += 1
            textView.text = "${progressBar.progress}%"
        }
    }

}
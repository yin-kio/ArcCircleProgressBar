package yinkio.android.customView

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import yinkio.android.customView.progressBar.ArcCircleProgressBar

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar: ArcCircleProgressBar = findViewById(R.id.progressBar)
//        progressBar.progress = 90f

        CoroutineScope(Dispatchers.IO).launch{

            while (true){
                delay(200)
                withContext(Dispatchers.Main){
                    progressBar.progress -= 1f
                }
            }


        }
    }

}
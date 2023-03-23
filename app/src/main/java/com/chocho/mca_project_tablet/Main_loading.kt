package com.chocho.mca_project_tablet

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import java.util.*
import kotlin.concurrent.timer

class Main_loading : AppCompatActivity() {

    var timer: Timer? = null
    var deltaTime = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val value = intent.getStringExtra("key1")
        val textView = findViewById<TextView>(R.id.TextView_loading)

        Log.d("key1", value.toString())


        val progressBar = findViewById<ImageView>(R.id.progressBar)
        Glide.with(this).load(R.raw.loading_lavender).into(progressBar)
        textView.text = "Loading..."

//        TimerFun()

        val delayMillis = 100L
        var percent = 0

        startCountdown(delayMillis, 1000, textView, progressBar, percent, value, this)



        Handler(Looper.myLooper()!!).postDelayed({
            val progressBar = findViewById<ImageView>(R.id.progressBar)


            val Main = Intent(this, Main::class.java)
            val Intent_Amenity = Intent(this, Amenity_Main::class.java)
            val Intent_Serving = Intent(this, Serving_Main::class.java)

            progressBar.isVisible = false

            if (value == "0") {
                startActivity(Main)
                finish()
            }
            if (value == "1") {
                Intent_Amenity.putExtra("key10","10")
                startActivity(Intent_Amenity)

            }
            if (value == "2") {
                Intent_Serving.putExtra("key1","1")
                startActivity(Intent_Serving)
                finish()
            }


            Log.d("딜리미터", percent.toString())
            finish()

        }, delayMillis)


    }

    fun startCountdown(
        millisInFuture: Long,
        countDownInterval: Long,
        textView: TextView,
        progressBar: ImageView,
        percent: Int,
        value: String?,
        activity: AppCompatActivity
    ) {
        object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val p = (1 - seconds.toDouble() / (millisInFuture / 1000)) * 100
                val currentPercent = p.toInt()

                textView.text = "$currentPercent%" // 현재 진행된 퍼센트값으로 textView 갱신

                // 전체 퍼센트 0부터 100까지의 값을 for문을 이용하여 모두 표시
                for (i in 0..100) {
                    if (currentPercent == i) {
                        Log.d("전체 퍼센트", "$i%")
                        break
                    }
                }
            }
            override fun onFinish() {
                if (value == "None") {
                    activity.startActivity(Intent(activity, Main::class.java))
                }
                if (value == "Hotel") {
                    val Intent_Amenity = Intent(activity, Amenity_Main::class.java)
                    Intent_Amenity.putExtra("key10","10")
                    startActivity(Intent_Amenity)
//                    activity.startActivity(Intent(activity, Amenity_Main::class.java))
                }
                if (value == "Serving") {
                    val Intent_Serving = Intent(activity, Serving_Main::class.java)
                    Intent_Serving.putExtra("key11","11")
                    startActivity(Intent_Serving)
//                    activity.startActivity(Intent(activity, Serving_Main::class.java))
                }
                activity.finish()
            }
        }.start()
    }






    override fun onDestroy() {
        super.onDestroy()

    }
}

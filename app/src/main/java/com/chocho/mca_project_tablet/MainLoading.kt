package com.chocho.mca_project_tablet

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import java.util.*

class MainLoading : AppCompatActivity() {

    private var delayMillis = 0L
    private var percent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        //각 화면에서 온 값 도착
        val mainIntentKey = intent.getStringExtra("key1")
        Log.d("mainIntentKey", mainIntentKey.toString()) // 잘 도착했는지 확인

        //xml에서 가져오기
        val txLoading = findViewById<TextView>(R.id.txLoading)
        val progressBar = findViewById<ImageView>(R.id.progressBar)

        //intent 값들
        val main = Intent(this, Main::class.java)
        val intentAmenity = Intent(this, AmenityMain::class.java)
        val intentServing = Intent(this, ServingMain::class.java)

        //액티비티에서 제공하는 앱바를 제어하는 객체입니다.
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        /**
        with함수 코틀린에서 제공하는 스코프 함수 중 하나, 특정 객체를 선택한 뒤 해당 객체를
        lambda 식의 매개변수로 전달하는 함수  this = MainLoading(객체)

        Glide 라이브러리
        load 메소드 : 이미지 로드할 위치 지정
        into 메소드 : 이미지 로드후 표시할 이미지 지정
        **/
        Glide.with(this).load(R.raw.loading_lavender).into(progressBar)

        /** int 형 2147483648 이상이면 Long 타입  **/
        delayMillis = 1000L


        startCountdown(delayMillis, 1000, txLoading, progressBar, percent, mainIntentKey, this)

        Handler(Looper.myLooper()!!).postDelayed({

            progressBar.isVisible = false

            when (mainIntentKey) {
                "0" -> startActivity(main)
                "1" -> startActivity(intentAmenity)
                "2" -> startActivity(intentServing)
            }

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
                Log.d("확인용",value.toString())
                if (value == "None") {
                    activity.startActivity(Intent(activity, Main::class.java))
                }
                if (value == "Hotel") {
                    val Intent_Amenity = Intent(activity, AmenityMain::class.java)

                    startActivity(Intent_Amenity)

                }
                if (value == "Serving") {
                    val Intent_Serving = Intent(activity, ServingMain::class.java)

                    startActivity(Intent_Serving)
                }
                activity.finish()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}

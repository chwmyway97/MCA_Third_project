package com.chocho.mca_project_tablet

import android.annotation.SuppressLint
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

class MainLoading : AppCompatActivity() {

    private var delayMillis = 2000L //gif 이미지 켜저있는 시간

    private var percent = 0

    private lateinit var mainIntentKey: String

    private lateinit var txLoading: TextView

    private lateinit var progressBar: ImageView

    private lateinit var main: Intent
    private lateinit var intentHotel: Intent
    private lateinit var intentServing: Intent


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        init()

    }

    private fun init() {
        //각 화면에서 온 값 도착
        mainIntentKey = intent.getStringExtra("key1").toString()

        Log.d("mainIntentKey", mainIntentKey) // 잘 도착했는지 확인

        //xml에서 가져오기
        txLoading = findViewById(R.id.txLoading)
        progressBar = findViewById(R.id.progressBar)

        //intent 값들
        main = Intent(this, Main::class.java) //처음화면으로
        intentHotel = Intent(this, HotelMain::class.java) //호텔 화면으로
        intentServing = Intent(this, ServingMain::class.java) //서빙 화면으로


        //액티비티에서 제공하는 앱바를 제어하는 객체입니다.
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false) //뒤로 가기 버튼을 표시하지 않도록 설정하고
            setDisplayShowHomeEnabled(false) //액션 바에서 홈 아이콘을 표시하지 않도록 설정합니다.
        }


        Glide.with(this).load(R.raw.spiner5).into(progressBar)


        //startCountdown 함수
        startCountdown(delayMillis, 1000L, txLoading, progressBar, percent, mainIntentKey, this)

        Handler(Looper.myLooper()!!).postDelayed({

            progressBar.isVisible = false

            when (mainIntentKey) {

                "0" -> {

                    main.putExtra("key1", "string") //해제 메세지 코드 키

                    startActivity(main) //처음화면으로

                }
                "1" -> {

                    intentHotel.putExtra("key","1")
                    startActivity(intentHotel) // 호텔화면으로
                     }

                "2" -> startActivity(intentServing) //서빙화면으로

            }

        }, delayMillis)

    }


    //이미지 떠있는 시간 계산하는 코드
    private fun startCountdown(
        millisInFuture: Long,  //카운트다운이 진행될 총 시간 (밀리초)
        countDownInterval: Long, //카운트다운의 각 틱 간격 (밀리초)
        textView: TextView, //텍스트를 표시할 TextView
        progressBar: ImageView, //이미지를 표시할 ImageView (아마도 프로그레스 바를 나타내는 이미지)
        percent: Int, //퍼센트 값 (정수)
        value: String?, //조건에 따라 화면 전환을 위한 값을 나타내는 문자열
        activity: AppCompatActivity //현재 액티비티의 AppCompatActivity 객체
    ) {
        object : CountDownTimer(millisInFuture, countDownInterval) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val p = (1 - seconds.toDouble() / (millisInFuture / 1000)) * 100
                val currentPercent = p.toInt()

                textView.text = "$currentPercent%"

                Log.d("전체 퍼센트", "$currentPercent%")
            }

            //카운트 다운이 완료되었을 때
            override fun onFinish() {
                when (value) {
                    "None" -> activity.startActivity(Intent(activity, Main::class.java))
                    "Hotel" -> activity.startActivity(Intent(activity, HotelMain::class.java))
                    "Serving" -> activity.startActivity(Intent(activity, ServingMain::class.java))
                }
                activity.finish()
            }
        }.start()
    }

    override fun onDestroy() {

        super.onDestroy()

    }

}
/**
with함수 코틀린에서 제공하는 스코프 함수 중 하나, 특정 객체를 선택한 뒤 해당 객체를
lambda 식의 매개변수로 전달하는 함수  this = MainLoading(객체)

Glide 라이브러리
load 메소드 : 이미지 로드할 위치 지정
into 메소드 : 이미지 로드후 표시할 이미지 지정

 **/

/** int 형 2147483648 이상이면 Long 타입  **/
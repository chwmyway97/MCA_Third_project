package com.chocho.mca_project_tablet

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class HotelMain : AppCompatActivity() {


    private lateinit var robot: ConstraintLayout

    private lateinit var netPageIntent: Intent

    private lateinit var intentLoding: Intent


    private val database = Firebase.database

    private val nfc = database.reference.child("NFC")

    private val hotelToastMessage = "호텔화면 업데이트 완료되었습니다." //호텔 화면 처음 떳을 때 토스트 메세지

    private val servingTTS = "호텔화면 업데이트 완료되었습니다." //tts 메세지

    private val hotel = database.reference.child("Hotel")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()


    }

    private fun init() {





        when (intent.getStringExtra("key")) {

            "1" -> {
                customToastView(hotelToastMessage)
                //만든 함수 여기에 적은 문자가 소리로 나오게 된다.


            }//호텔화면 처음 떳을 때 커스텀 메세지,음성

            else -> {} //그후

        }
        hotel.removeValue() //이동 값 제거


        //xml 선언
        robot = findViewById(R.id.Robot)

        //MainLoading 이동
        intentLoding = Intent(this@HotelMain, MainLoading::class.java)


        //메인페이지 클릭시
        robot.setOnClickListener {

            netPageIntent = Intent(this, HotelPage1::class.java)

            startActivity(netPageIntent)


        }

        nfc.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nfcValue = snapshot.value

                when (nfcValue) {

                    // 로딩_메인(0)
                    "None" -> {

                        intentLoding.putExtra("key1", "0")

                        startActivity(intentLoding)

                        finish()
                    }

                    // 로딩_서빙(2)
                    "Serving" -> {

                        intentLoding.putExtra("key1", "2")

                        startActivity(intentLoding)

                        finish()
                    }
                }

                Log.d("AmenityMain_nfc", "Value is: $nfcValue")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }
        })

    }

    private fun customToastView(text: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.activity_custom_toast, findViewById<ViewGroup>(R.id.toast_layout_root))
        val textView = layout.findViewById<TextView>(R.id.textboard)
        textView.text = text

        val toastView = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toastView.setGravity(Gravity.CENTER, 0, 0)
        toastView.view = layout
        toastView.show()
    }

}
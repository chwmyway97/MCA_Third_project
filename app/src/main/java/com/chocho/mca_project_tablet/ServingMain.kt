package com.chocho.mca_project_tablet

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide.init
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ServingMain : AppCompatActivity(), TextToSpeech.OnInitListener  {

    private val database = Firebase.database
    private val nfc = database.reference.child("NFC")
    private val sound = database.reference.child("Sound")
    private var tts: TextToSpeech? = null //TTS

    private lateinit var soundListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    private fun init(){

        val robot = findViewById<ConstraintLayout>(R.id.Robot) //화면 터치


        //TTS
        tts = TextToSpeech(this, this) //tts 에 TextToSpeech 값 넣어줌

        soundListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                if(value == "Sound"){
                    startTTS("테이블 위의 물건들을 치워주시고 손을 책상위에서 내려주시기 바랍니다.")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        }
        sound.addValueEventListener(soundListener)



        //MainLoading 이동
        val intentLoding = Intent(this, MainLoading::class.java)

        Toast.makeText(this,"서빙", Toast.LENGTH_SHORT).show()

        robot.setOnClickListener {
            val netPageIntent = Intent(this, ServingPage1::class.java)
            startActivity(netPageIntent)
        }

        nfc.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val nfcValue = snapshot.value // nfcValue 값

                when (nfcValue) {

                    // 로딩_메인(0)
                    "None" -> {
                        intentLoding.putExtra("key1", "0")
                        startActivity(intentLoding)
                        finish()
                    }


                    // 로딩_호텔(1)
                    "Hotel" -> {
                        intentLoding.putExtra("key1", "1")
                        startActivity(intentLoding)
                        finish()
                    }

                }

                Log.d("nfcValue", "Value is: $nfcValue") // nfcValue 값 확인
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("nfcValue", "Failed to read value.", error.toException())
            }

        })
    }

    private fun startTTS(text: String) {
        //10초 정도 text 두번 출력
        tts!!.speak(text+text, TextToSpeech.QUEUE_FLUSH, null, "")

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.KOREA)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "해당언어는 지원되지 않습니다.")
                return
            } else {
                //doSomething
            }
        } else {
            //doSomething
        }
    }

    override fun onDestroy() {

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        sound.removeEventListener(soundListener)

    }



}
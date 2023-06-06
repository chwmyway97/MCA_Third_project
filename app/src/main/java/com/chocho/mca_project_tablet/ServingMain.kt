package com.chocho.mca_project_tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ServingMain : AppCompatActivity(), TextToSpeech.OnInitListener  {


    private val servingToastMessage = "서빙 화면 업데이트 완료되었습니다." //호텔 화면 처음 떳을 때 토스트 메세지

    private val servingTTS = "테이블 위의 물건들을 치워주시고 손을 책상위에서 내려주시기 바랍니다." //tts 메세지

    private val database = Firebase.database

    private val nfc = database.reference.child("NFC")

    private val sound = database.reference.child("Sound")

    private var tts: TextToSpeech? = null //TTS



    private lateinit var soundListener: ValueEventListener

    private lateinit var robot :ConstraintLayout

    private lateinit var intentLoding :Intent

    private lateinit var netPageIntent :Intent



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    private fun init(){

        //xml 선언
        robot = findViewById(R.id.Robot)

        //TTS
        tts = TextToSpeech(this, this) //tts 에 TextToSpeech 값 넣어줌

        //사운드 관련 파이어베이스
        soundListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val soundValue = snapshot.value

                if(soundValue == "Sound"){

                    //만든 함수 여기에 적은 문자가 소리로 나오게 된다.
                    startTTS(servingTTS)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        }
        sound.addValueEventListener(soundListener)


        //MainLoading 이동
        intentLoding = Intent(this, MainLoading::class.java)

        customToastView(servingToastMessage)

        //화면 터치시 이동
        robot.setOnClickListener {

            netPageIntent = Intent(this, ServingPage1::class.java)

            startActivity(netPageIntent)

        }

        //화면 이동 관련 파이어베이스
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

    //tts 함수
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
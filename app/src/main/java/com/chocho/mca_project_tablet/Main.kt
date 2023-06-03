package com.chocho.mca_project_tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide.init
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Main : AppCompatActivity() {

    private val database = Firebase.database

    private val nfc = database.reference.child("NFC")

    private lateinit var robot : ConstraintLayout

    private lateinit var intentLoding : Intent

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()

    }

    private fun init(){

        // 화면 생성되면 뜨는 커스텀 토스트 메세지
        when(intent.getStringExtra("key1")){

            "string" -> customToastView("해제 완료 되었습니다.")

            else ->  customToastView("안녕하세요")

        }

        //xml에서 가져오기
        robot = findViewById(R.id.Robot)

        //MainLoading 이동
        intentLoding = Intent(this@Main, MainLoading::class.java)

        //메인페이지 클릭시 토스트메세지
        robot.setOnClickListener { customToastView("업데이트 된 화면이 없습니다.") }

        //nfc파이어베이스
        nfc.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val nfcValue = snapshot.value // nfcValue 값

                when (nfcValue) {

                    // 로딩_호텔(1)
                    "Hotel" -> {
                        intentLoding.putExtra("key1", "1")
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

                Log.d("nfcValue", "Value is: $nfcValue") // nfcValue 값 확인

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("nfcValue", "Failed to read value.", error.toException())
            }

        })
    }

    //커스텀 메세지
    private fun customToastView(text: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.activity_custom_toast, findViewById(R.id.toast_layout_root))
        val textView = layout.findViewById<TextView>(R.id.textboard)
        textView.text = text

        val toastView = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toastView.setGravity(Gravity.CENTER, 0, 0)
        toastView.view = layout
        toastView.show()
    }

}
package com.chocho.mca_project_tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Main : AppCompatActivity() {

    private val database = Firebase.database
    private val nfc = database.reference.child("NFC")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        Toast.makeText(this,"연결 안됨", Toast.LENGTH_SHORT).show()

        //xml에서 가져오기
        val robot = findViewById<ConstraintLayout>(R.id.Robot)

        //MainLoading 이동
        val intentLoding = Intent(this@Main, MainLoading::class.java)

        //메인페이지 클릭시 토스트메세지
        robot.setOnClickListener { Toast.makeText(this, "메인페이지", Toast.LENGTH_SHORT).show() }

        //nfc파이어베이스
        nfc.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val nfcValue = snapshot.value // nfcValue 값

                when (nfcValue) {

                    //로딩_메인(0)
                    "None" -> {
                        intentLoding.putExtra("key1","0")
                        startActivity(intentLoding)
                        finish()
                    }

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
}
package com.chocho.mca_project_tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AmenityMain : AppCompatActivity(){


    private var robot : ConstraintLayout? = null

    private var netPageIntent: Intent? = null


    private val database = Firebase.database

    private val nfc = database.reference.child("NFC")

    private val hotelToastMessage = "호텔화면 업데이트 완료되었습니다."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }
    private fun init(){

        robot = findViewById(R.id.Robot)

        customToastView(hotelToastMessage)

//        Toast.makeText(this,"호텔", Toast.LENGTH_SHORT).show()


        robot = findViewById(R.id.Robot)
        robot?.setOnClickListener {

            netPageIntent = Intent(this, AmenityPage1::class.java)

            startActivity(netPageIntent)

            finish()
        }

        nfc.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value

                when (value) {
                    "None" -> {
                        val main = Intent(this@AmenityMain, MainLoading::class.java)
                        main.putExtra("key1", "0")
                        startActivity(main)
                        finish()
                    }
                    "Serving" -> {
                        val intentServing = Intent(this@AmenityMain, MainLoading::class.java)
                        intentServing.putExtra("key1", "2")
                        startActivity(intentServing)
                        finish()
                    }
                }

                Log.d("AmenityMain_nfc", "Value is: $value")
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
package com.chocho.mca_project_tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AmenityPage2 : AppCompatActivity() {

    private lateinit var startListener: ValueEventListener


    private val database = Firebase.database
    val unLockImg = R.drawable.img_lock7
    val lockImg = R.drawable.img_lock2
    val Hotel = database.reference.child("Hotel")
    val Start = database.reference.child("Start")
    val QR = database.reference.child("QR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page2)

        val img_motor1 = findViewById<ImageView>(R.id.lock6)
        val img_motor2 = findViewById<ImageView>(R.id.lock7)
        val img_motor3 = findViewById<ImageView>(R.id.lock8)



        Start.setValue("Question")
        Hotel.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val image_lock1 = snapshot.child("Lock1").value
                val image_lock2 = snapshot.child("Lock2").value
                val image_lock3 = snapshot.child("Lock3").value



                Log.d("image_lock3", image_lock3.toString())



                if (image_lock1 == "First_Unlock") {
                    img_motor1.setImageResource(unLockImg)


                } else {
                    img_motor1.setImageResource(lockImg)
                }
                if (image_lock2 == "Second_Unlock") {
                    img_motor2.setImageResource(unLockImg)

                } else {
                    img_motor2.setImageResource(lockImg)
                }
                if (image_lock3 == "Third_Unlock") {
                    img_motor3.setImageResource(unLockImg)

                } else {
                    img_motor3.setImageResource(lockImg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })
        startListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val start = snapshot.value

                if (start == "Home_Fail") {
                    Log.d("확인", "Value is: $start")

                    Toast.makeText(this@AmenityPage2, "문을 닫아 주세요", Toast.LENGTH_SHORT).show()

                }else if (start == "Home_Success") {
                    QR.removeValue()

                    val intent = Intent(this@AmenityPage2,AmenityMain::class.java)
                    startActivity(intent)

                    Toast.makeText(this@AmenityPage2, "출발합니다.", Toast.LENGTH_SHORT).show()

                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        Start.addValueEventListener(startListener)
    }
    override fun onStop() {
        super.onStop()
        Start.removeEventListener(startListener)
    }
}
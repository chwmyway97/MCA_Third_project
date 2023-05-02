package com.chocho.mca_project_tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.*

class AmenityMain : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this,"호텔", Toast.LENGTH_SHORT).show()

        database = FirebaseDatabase.getInstance()

        val NFC = database.reference.child("NFC")


        val robot = findViewById<ConstraintLayout>(R.id.Robot)
        robot.setOnClickListener {
            val netPageIntent = Intent(this, AmenityPage1::class.java)
            startActivity(netPageIntent)
            finish()
        }


        NFC.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value

                if (value == "None"){
                    val Main = Intent(this@AmenityMain,MainLoading::class.java)
                    Main.putExtra("key1","0")
                    startActivity(Main)
                    finish()
                }
                Log.d("파이어", "Value is: $value")
                if (value == "Serving"){
                    val Intent_Serving = Intent(this@AmenityMain,MainLoading::class.java)
                    Intent_Serving.putExtra("key1","2")
                    startActivity(Intent_Serving)
                    finish()
                }
                Log.d("파이어", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })



    }


}
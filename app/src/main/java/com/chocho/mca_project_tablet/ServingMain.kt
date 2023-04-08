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

class ServingMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this,"서빙", Toast.LENGTH_SHORT).show()

        val robot = findViewById<ConstraintLayout>(R.id.Robot)
        robot.setOnClickListener {
            val netPageIntent = Intent(this, Serving_page1::class.java)
            startActivity(netPageIntent)
        }




        val database = Firebase.database

        val myRef = database.reference.child("NFC")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value

                if (value == "None"){
                    val Intent_Main = Intent(this@ServingMain,Main::class.java)
                    startActivity(Intent_Main)
                }

                if (value == "Hotel"){
                    val Intent_Amenity = Intent(this@ServingMain,AmenityMain::class.java)
                    startActivity(Intent_Amenity)
                }

                Log.d("파이어", "Value is: $value")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })
    }
}
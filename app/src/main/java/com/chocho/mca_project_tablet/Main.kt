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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val robot = findViewById<ConstraintLayout>(R.id.Robot)
        robot.setOnClickListener {
            Toast.makeText(this,"메인페이지",Toast.LENGTH_SHORT).show()
        }


        val database = Firebase.database
        val myRef = database.reference.child("NFC")




        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value

                if (value == "1"){
                    val Intent_Amenity = Intent(this@Main,Main_loading::class.java)
                    Intent_Amenity.putExtra("key1","1")
                    startActivity(Intent_Amenity)
                }

                if (value == "2"){
                    val Intent_Serving = Intent(this@Main,Main_loading::class.java)
                    Intent_Serving.putExtra("key1","2")
                    startActivity(Intent_Serving)
                }
                Log.d("파이어", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })


    }
}
package com.chocho.mca_project_tablet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainSetting : AppCompatActivity() {

    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val Module_Motor_button = findViewById<Button>(R.id.Module_Motor)

        val Module_Motor = database.reference.child("Module_Motor")

        Module_Motor_button.setOnClickListener {
            Module_Motor.setValue("Open")
        }
    }
}
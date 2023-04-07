package com.chocho.mca_project_tablet

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator

class Amenity_Page3 : AppCompatActivity() {


    val database = FirebaseDatabase.getInstance()
    val go = database.reference.child("Hotel").child("go")
    val QR = database.reference.child("QR")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        QR.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == "QR") {
                    //큐얼코드 리더기 추가
                    val integrator = IntentIntegrator(this@Amenity_Page3)
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    integrator.setPrompt("Scan a QR code")
                    integrator.setOrientationLocked(false)
                    integrator.setBeepEnabled(false)
                    integrator.setBarcodeImageEnabled(true)
                    integrator.initiateScan()
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MainActivity", "onCancelled: $databaseError")
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {

                val scannedData = result.contents // 스캔한 QR 코드의 결과 값

                go.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val Hotel = snapshot.value

                        if (scannedData == Hotel.toString()){

                            Log.d("호텔",scannedData)
                            Toast.makeText(this@Amenity_Page3,"확인됨",Toast.LENGTH_LONG).show()
                            QR.removeValue()
                            val intent =Intent(this@Amenity_Page3,Amenity_Page2::class.java)
                            startActivity(intent)


                        }else{
                            Toast.makeText(this@Amenity_Page3,"실패",Toast.LENGTH_LONG).show()
                            // 실패한 경우 QR 코드 리더기 다시 시작
                            QR.removeValue()
                            QR.setValue("QR")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show()
                // 실패한 경우 QR 코드 리더기 다시 시작
                QR.removeValue()
                QR.setValue("QR")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}



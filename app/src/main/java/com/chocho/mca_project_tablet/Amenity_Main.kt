package com.chocho.mca_project_tablet

import android.Manifest.permission.NFC
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator

class Amenity_Main : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var qrCodeReaderValueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        val database = Firebase.database
        val NFC = database.reference.child("NFC")



        val ExtraString = intent.getStringExtra("key10")
        Log.d("키10", ExtraString.toString())
        if(ExtraString == "10" ) {
            Toast.makeText(this,"어메니티", Toast.LENGTH_SHORT).show()
        }


        val robot = findViewById<ConstraintLayout>(R.id.Robot)
        robot.setOnClickListener {
            val netPageIntent = Intent(this, Amenity_Page1::class.java)
            startActivity(netPageIntent)
        }





        NFC.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value

                if (value == "None"){
                    val Main = Intent(this@Amenity_Main,Main_loading::class.java)
                    Main.putExtra("key1","0")
                    startActivity(Main)
                }
                Log.d("파이어", "Value is: $value")
                if (value == "Serving"){
                    val Intent_Serving = Intent(this@Amenity_Main,Main_loading::class.java)
                    Intent_Serving.putExtra("key1","2")
                    startActivity(Intent_Serving)
                }
                Log.d("파이어", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })

        val intent_main = intent.getStringExtra("호실")
        Log.d("intent_main", intent_main.toString())

        val dataRef: DatabaseReference = database.reference


        dataRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataSnapshot.key == "QR") {
                    // QR 코드 리더기 실행
                    val integrator = IntentIntegrator(this@Amenity_Main)
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    integrator.setPrompt("Scan a QR code")
                    integrator.setCameraId(0)
                    integrator.setBeepEnabled(false)
                    integrator.setBarcodeImageEnabled(false)
                    integrator.initiateScan()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MainActivity", "onCancelled: $databaseError")
            }
        })
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // 리스너 등록 해제
//        database.getReference("qrCodeReader").removeEventListener(qrCodeReaderValueEventListener)
//    }

    // QR 코드 리더기 결과를 처리하는 메서드
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d("MainActivity", "Cancelled")
            } else {
                // QR 코드에서 읽은 결과를 출력합니다.
                val intent_main = intent.getStringExtra("호실")
                Log.d("intent_main", intent_main.toString())
                Log.d("MainActivity", "Scanned:" + result.contents)
                Toast.makeText(this,result.contents,Toast.LENGTH_SHORT).show()
                if(result.contents == intent_main){
                    val intent = Intent(this, Amenity_Page1::class.java)
                    startActivity(intent)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



}
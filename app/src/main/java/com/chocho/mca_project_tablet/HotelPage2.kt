package com.chocho.mca_project_tablet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//도착후
class HotelPage2 : AppCompatActivity() {

    //imageView 변수
    private lateinit var imgMotor1: ImageView
    private lateinit var imgMotor2: ImageView
    private lateinit var imgMotor3: ImageView

    //파이어베이스 읽기 값
    private lateinit var imageLock1: String
    private lateinit var imageLock2: String
    private lateinit var imageLock3: String

    //Intent 변수
    private lateinit var intentLoding: Intent
    private lateinit var intentHotelMain: Intent

    //String 변수
    private val homeSuccessToast = "출발합니다."
    private val homeFailToast = "문을 닫아 주세요"

    //드로어블 이미지
    val unLockImg = R.drawable.img_lock7
    val lockImg = R.drawable.img_lock2

    //파이어베이스
    private val database = Firebase.database
    private val nfc = database.reference.child("NFC")
    private val hotel = database.reference.child("Hotel")
    private val hotelStart = database.reference.child("Start")
    private val qr = database.reference.child("QR")
    private val hotelMotor = database.reference.child("Hotel_Motor")
    private val hotelMotor1 = hotelMotor.child("Hotel_Motor1")
    private val hotelMotor2 = hotelMotor.child("Hotel_Motor2")
    private val hotelMotor3 = hotelMotor.child("Hotel_Motor3")
    private lateinit var startListener: ValueEventListener
    private lateinit var hotelListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_page2)

        //이미지 선언
        imgMotor1 = findViewById(R.id.lock6)
        imgMotor2 = findViewById(R.id.lock7)
        imgMotor3 = findViewById(R.id.lock8)

        //start 파이어베이스에 넣기
        hotelStart.setValue("Question1")

        //호텔의 자물쇠 이미지
        hotelListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                imageLock1 = snapshot.child("Lock1").value.toString()
                imageLock2 = snapshot.child("Lock2").value.toString()
                imageLock3 = snapshot.child("Lock3").value.toString()


                //파이어베이스 값 잘 들어 오는지 확인
                Log.d("image_lock", "image_lock1 : $imageLock1")
                Log.d("image_lock", "image_lock2 : $imageLock2")
                Log.d("image_lock", "image_lock3 : $imageLock3")

                //자물쇠의 상태에 따른 조건문
                if (imageLock1 == "First_Unlock") {
                    imgMotor1.setImageResource(unLockImg)
                    hotelMotor1.setValue("First_Unlock")

                } else {
                    imgMotor1.setImageResource(lockImg)
                }
                if (imageLock2 == "Second_Unlock") {
                    imgMotor2.setImageResource(unLockImg)
                    hotelMotor2.setValue("Second_Unlock")

                } else {
                    imgMotor2.setImageResource(lockImg)
                }
                if (imageLock3 == "Third_Unlock") {
                    imgMotor3.setImageResource(unLockImg)
                    hotelMotor3.setValue("Third_Unlock")

                } else {
                    imgMotor3.setImageResource(lockImg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        }
        hotel.addValueEventListener(hotelListener) //리스터 작동

        //돌아가는 코드
        startListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val startValue = snapshot.value

                if (startValue == "Home_Success") {

                    qr.removeValue() //qr 제거


                    //모든 자물쇠 잠그기
                    hotelMotor1.setValue("First_Lock")
                    hotelMotor2.setValue("Second_Lock")
                    hotelMotor3.setValue("Third_Lock")

                    //얼굴화면으로 고고
                    intentHotelMain = Intent(this@HotelPage2, HotelMain::class.java)

                    startActivity(intentHotelMain)

                    //토스트 메세지 출발
                    customToastView(homeSuccessToast)


                    finish()

                } else if (startValue == "Home_Fail") { //문이 안닫혀 있을 때

                    Log.d("확인", "Value is: $startValue")

                    //토스트 메세지 실패 했음
                    customToastView(homeFailToast)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("nfcValue", "Failed to read value.", error.toException())
            }
        }
        hotelStart.addValueEventListener(startListener)

        nfc.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                when (snapshot.value) {

                    // 로딩_메인(0)
                    "None" -> {

                        intentLoding.putExtra("key1", "0")

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

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
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

    override fun onStop() {
        super.onStop()
        hotelStart.removeEventListener(startListener)
        hotel.removeEventListener(hotelListener)
    }
}
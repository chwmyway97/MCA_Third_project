package com.chocho.mca_project_tablet

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class HotelPage1 : AppCompatActivity() {

    private lateinit var startListener: ValueEventListener
    private lateinit var motorListener: ValueEventListener
    private lateinit var moduleListener: ValueEventListener

    private val database = Firebase.database
    private val nfc = database.reference.child("NFC") //모듈 변경을 위한 nfc
    private val module = database.reference.child("Module_Motor") //모듈 잠금 장치
    private val motor = database.reference.child("Hotel_Motor") //서랍 잠금 장치
    private val move = database.reference.child("Start") //출발 신호 보내기
    private val hotel = database.reference.child("Hotel") //저장값 저장하기


    private val imgLock7 = R.drawable.img_lock7
    private val imgLock2 = R.drawable.img_lock2

    private lateinit var textHome: TextView
    private lateinit var moduleTx: TextView

    private lateinit var moduleImg: ImageFilterView

    private lateinit var intentLoding: Intent

    private lateinit var lockImgIds: List<Int>
    private lateinit var buttonIds: List<Int>
    private lateinit var imageButtons: Array<ImageButton>
    private lateinit var lockImg: Array<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)

        init()


    }

    private fun init() {

        //버튼 변수 리스트로 정리
        buttonIds = listOf(
            R.id.imageButton0,
            R.id.imageButton1,
            R.id.imageButton2,
            R.id.imageButton3,
            R.id.imageButton4,
            R.id.imageButton5,
            R.id.imageButton6,
            R.id.imageButton7,
            R.id.imageButton8,
            R.id.imageButton9,
            R.id.imageButtonBack,
            R.id.imageButtonStart,
            R.id.imageButtonLock

        )
        imageButtons = Array(buttonIds.size) { i -> findViewById(buttonIds[i]) }

        //서랍 잠금 장치 변수 리스트로 정리
        lockImgIds = listOf(R.id.lock1, R.id.lock2, R.id.lock3)
        lockImg = Array(lockImgIds.size) { o -> findViewById(lockImgIds[o]) }


        moduleImg = findViewById(R.id.chain) //모듈 연결 상태 이미지
        moduleTx = findViewById(R.id.chain_tx) //모듈 연결 상태 텍스트


        hotel.removeValue() //저장값 초기화


        moduleImg.setOnClickListener { module.setValue("Open") }  //모듈 잠금장치

        //모듈 잠금 장치 값 읽고 이미지
        moduleListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moduleValue = snapshot.value
                if (moduleValue == "Open") {
                    moduleImg.setImageResource(R.drawable.chain_broken)
                    moduleTx.text = "해제"
                } else {
                    moduleImg.setImageResource(R.drawable.chain2)
                    moduleTx.text = "연결"
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        module.addValueEventListener(moduleListener)

        textHome = findViewById(R.id.text_home)

        //버튼 클릭 함수
        fun buttonClick(num: String) {
            with(textHome) {
                setTextColor(ContextCompat.getColor(this@HotelPage1, R.color.purple_CACAE1))
                if (num == "del") {
                    if (text.isNotEmpty()) text = text.substring(0, text.length - 1)
                } else {
                    val setNum = text.toString() + num
                    if (setNum.length <= 3) text = setNum
                }
            }
        }

        val button = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "del")

        for (i in button.indices) {
            imageButtons[i].setOnClickListener { buttonClick(button[i]) }
        }


        //로봇 이동 start버튼
        imageButtons[11].setOnClickListener {

            move.setValue("Question")

            val length = textHome.text.length
            when {
                length < 3 -> {
                    customToastView("호실을 지정해주시기 바랍니다.")
                    move.setValue("Null")
                }
                length == 3 -> {
                    startListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val startValue = snapshot.value
                            val lock1 = motor.child("Hotel_Motor1")
                            val lock2 = motor.child("Hotel_Motor2")
                            val lock3 = motor.child("Hotel_Motor3")

                            Log.d("startValue", "Value is: $startValue")

                            if (startValue == "Fail") {

                                customToastView("  문을 닫아 주세요  ")


                            } else if (startValue == "Success") {


                                motorListener = object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {

                                        val hotelMotor1 = snapshot.child("Hotel_Motor1").value.toString()
                                        val hotelMotor2 = snapshot.child("Hotel_Motor2").value.toString()
                                        val hotelMotor3 = snapshot.child("Hotel_Motor3").value.toString()

                                        if (hotelMotor1 == "First_Unlock") {
                                            hotel.child("Lock1").setValue("First_Unlock")
                                        }
                                        if (hotelMotor2 == "Second_Unlock") {
                                            hotel.child("Lock2").setValue("Second_Unlock")

                                        }
                                        if (hotelMotor3 == "Third_Unlock") {
                                            hotel.child("Lock3").setValue("Third_Unlock")

                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                }
                                motor.addValueEventListener(motorListener)


                                lock1.setValue("First_Lock")
                                lock2.setValue("Second_Lock")
                                lock3.setValue("Third_Lock")
                                hotel.child("go").setValue(textHome.text)

                                customToastView("  ${textHome.text}호로  출발 합니다.  ")


                                val intentAmenityPage3 = Intent(this@HotelPage1, HotelPage3::class.java)
                                intentAmenityPage3.putExtra("go", textHome.text)
                                startActivity(intentAmenityPage3)
                                finish()


                            }




                        }


                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }




                    }
                    move.addValueEventListener(startListener)

                }
            }

        }
        //nfc 동작
        nfc.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nfcValue = snapshot.value

                when (nfcValue) {

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

                Log.d("AmenityMain_nfc", "Value is: $nfcValue")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }
        })


        //잠금장치 클릭시 바텀 시트
        imageButtons[12].setOnClickListener {
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)


        }


        //잠금장치 이미지 넣기 위한
        motorListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val locks = arrayOf(
                    snapshot.child("Hotel_Motor1").value as String?,
                    snapshot.child("Hotel_Motor2").value as String?,
                    snapshot.child("Hotel_Motor3").value as String?
                )

                val lockImages = arrayOf(imgLock2, imgLock7)

                locks.forEachIndexed { index, lock ->
                    val imageResource = if (lock == "First_Lock" || lock == "Second_Lock" || lock == "Third_Lock") {
                        lockImages[0]
                    } else {
                        lockImages[1]
                    }
                    lockImg[index].setImageResource(imageResource)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        }
        motor.addValueEventListener(motorListener)

        //Thread 이용하여 매초마다 업데이트 되는 방식
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                updateUI()
                handler.postDelayed(this, 10)
            }
        }
        handler.post(runnable)

    }

    //배터리&시간
    private fun updateUI() {
        // Set the time
        val textTime = findViewById<TextView>(R.id.text_time)
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm:ss", Locale.getDefault())
        val getTime = dateFormat.format(now)
        textTime.text = getTime

        // Set the battery level
        val textBattery: TextView = findViewById<TextView>(R.id.text_battery)
        val batteryPct = getBatteryLevel()
        textBattery.text = "${batteryPct}%"
        someFunction(batteryPct.toString())
    }

    //배터리 계산
    private fun getBatteryLevel(): Int {
        return try {
            val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = registerReceiver(null, batteryFilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            (level / scale.toFloat() * 100).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    //
    fun someFunction(returnData: String) {
        val Image_battery = findViewById<ImageFilterView>(R.id.Image_battery)
        when {
            returnData.toInt() == 100 -> Image_battery.setImageResource(R.drawable.battery_full)
            returnData.toInt() >= 70 -> Image_battery.setImageResource(R.drawable.battery_threequarter)
            returnData.toInt() >= 50 -> Image_battery.setImageResource(R.drawable.battery_half)
            returnData.toInt() >= 30 -> Image_battery.setImageResource(R.drawable.battery_low)
            else -> Image_battery.setImageResource(R.drawable.battery_low)
        }
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




    override fun onStop() {
        super.onStop()
        move.removeEventListener(startListener)
        motor.removeEventListener(motorListener)
        module.removeEventListener(moduleListener)

    }
}



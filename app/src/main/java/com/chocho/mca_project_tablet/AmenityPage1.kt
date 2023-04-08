package com.chocho.mca_project_tablet

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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


class AmenityPage1 : AppCompatActivity() {
    private lateinit var textHome: TextView

    private val database = Firebase.database
    val NFC = database.reference.child("NFC")
    val Motor = database.reference.child("Hotel_Motor")
    val Start = database.reference.child("Start")
    val Hotel = database.reference.child("Hotel")
    val imgLock7 = R.drawable.img_lock7
    val imgLock2 = R.drawable.img_lock2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)

        val buttonIds = listOf(
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
            R.id.imageButtonEnter

        )
        val lockImgIds = listOf(

            R.id.lock1,
            R.id.lock2,
            R.id.lock3
        )
        val button = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "del")


        val imageButtons = Array(buttonIds.size) { i -> findViewById<ImageButton>(buttonIds[i]) }
        val lockImg = Array(lockImgIds.size) { o -> findViewById<ImageView>(lockImgIds[o]) }

        textHome = findViewById(R.id.text_home)

        //버튼 클릭 함수
        fun buttonClick(num: String) {
            with(textHome) {
                setTextColor(ContextCompat.getColor(this@AmenityPage1, R.color.purple_CACAE1))
                if (num == "del") {
                    if (text.isNotEmpty()) text = text.substring(0, text.length - 1)
                } else {
                    val setNum = text.toString() + num
                    if (setNum.length <= 3) text = setNum
                }
            }
        }

        //숫자 버튼 클릭
        for (i in button.indices) {
            imageButtons[i].setOnClickListener { buttonClick(button[i]) }
        }


        //로봇 이동
        imageButtons[11].setOnClickListener {

            Start.setValue("Question")

            val length = textHome.text.length
            when {
                length < 3 -> {
                    makeText(this@AmenityPage1, "호실을 지정해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                    Start.setValue("Null")
                }
                length == 3 -> {
                    Start.addValueEventListener(questionListener())

                }
            }
        }

        //잠금장치 클릭시
        imageButtons[12].setOnClickListener {
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)
        }

        //NFC
        NFC.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nfcValue = snapshot.value as? String

                Log.d("파이어", "Value is: $nfcValue")
                when (nfcValue) {
                    "None" -> {
                        val main = Intent(this@AmenityPage1, MainLoading::class.java)
                        main.putExtra("key1", "0")
                        startActivity(main)
                    }
                    "Serving" -> {
                        val intentServing = Intent(this@AmenityPage1, MainLoading::class.java)
                        intentServing.putExtra("key1", "2")
                        startActivity(intentServing)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }
        })

        //잠금장치 이미지 넣기 위한
        Motor.addValueEventListener(object : ValueEventListener {

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

        })

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

    //배터리 & 시간
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

    private fun questionListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val startValue = snapshot.value
                val lock1 = Motor.child("Hotel_Motor1")
                val lock2 = Motor.child("Hotel_Motor2")
                val lock3 = Motor.child("Hotel_Motor3")

                Log.d("startValue", "Value is: $startValue")
                if (startValue == "Fail") {
                    makeText(this@AmenityPage1, "문을 닫아 주세요", Toast.LENGTH_SHORT).show()
                } else if (startValue == "Success") {
                    makeText(this@AmenityPage1, "출발 합니다.", Toast.LENGTH_SHORT).show()

                    Motor.addValueEventListener(motorListener())

                    lock1.setValue("First_Lock")
                    lock2.setValue("Second_Lock")
                    lock3.setValue("Third_Lock")
                    Hotel.child("go").setValue(textHome.text)
                    val intentAmenityPage3 = Intent(this@AmenityPage1, AmenityPage3::class.java)
                    startActivity(intentAmenityPage3)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Do nothing
            }
        }
    }

    private fun motorListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Hotel_Motor1 = snapshot.child("Hotel_Motor1").value.toString()
                val Hotel_Motor2 = snapshot.child("Hotel_Motor2").value.toString()
                val Hotel_Motor3 = snapshot.child("Hotel_Motor3").value.toString()

                if (Hotel_Motor1 == "First_Unlock") {
                    Hotel.child("Lock1").setValue("First_Unlock")
                }
                if (Hotel_Motor2 == "Second_Unlock") {
                    Hotel.child("Lock2").setValue("Second_Unlock")
                }
                if (Hotel_Motor3 == "Third_Unlock") {
                    Hotel.child("Lock3").setValue("Third_Unlock")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Do nothing
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}



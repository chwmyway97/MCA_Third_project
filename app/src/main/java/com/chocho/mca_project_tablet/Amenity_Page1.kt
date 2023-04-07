package com.chocho.mca_project_tablet

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
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


class Amenity_Page1 : AppCompatActivity() {

    private val database = Firebase.database
    val NFC = database.reference.child("NFC")
    val Motor = database.reference.child("Hotel_Motor")
    val Start = database.reference.child("Start")
    val go = database.reference.child("Hotel")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)

        val imageButton1 = findViewById<ImageButton>(R.id.imageButton1)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        val imageButton3 = findViewById<ImageButton>(R.id.imageButton3)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButton4)
        val imageButton5 = findViewById<ImageButton>(R.id.imageButton5)
        val imageButton6 = findViewById<ImageButton>(R.id.imageButton6)
        val imageButton7 = findViewById<ImageButton>(R.id.imageButton7)
        val imageButton8 = findViewById<ImageButton>(R.id.imageButton8)
        val imageButton9 = findViewById<ImageButton>(R.id.imageButton9)
        val imageButton10 = findViewById<ImageButton>(R.id.imageButton0)
        val imageButtonBack = findViewById<ImageButton>(R.id.imageButtonBack)
        val imageButtonEnter = findViewById<ImageButton>(R.id.imageButtonEnter)
        val imageButtonStart = findViewById<ImageButton>(R.id.imageButtonStart)


        val text_home = findViewById<TextView>(R.id.text_home)


        //버튼 클릭 함수
        fun buttonClick(num: String) {
            val txHome: String = text_home.text.toString()
            val setNum = txHome + num

            text_home.setTextColor(ContextCompat.getColor(this, R.color.purple_CACAE1))
            if (num == "del") {
                if (text_home.text.isNotEmpty()) {
                    text_home.text = text_home.text.substring(0, text_home.text.length - 1)
                } else {
                    text_home.text = ""
                }
            } else {
                if (setNum.length <= 3) {
                    text_home.text = setNum

                }
            }


        }


        //버튼 클릭
        imageButton1.setOnClickListener { buttonClick("1") }
        imageButton2.setOnClickListener { buttonClick("2") }
        imageButton3.setOnClickListener { buttonClick("3") }
        imageButton4.setOnClickListener { buttonClick("4") }
        imageButton5.setOnClickListener { buttonClick("5") }
        imageButton6.setOnClickListener { buttonClick("6") }
        imageButton7.setOnClickListener { buttonClick("7") }
        imageButton8.setOnClickListener { buttonClick("8") }
        imageButton9.setOnClickListener { buttonClick("9") }
        imageButton10.setOnClickListener { buttonClick("0") }
        imageButtonBack.setOnClickListener { buttonClick("del") }

        imageButtonStart.setOnClickListener {

            Start.setValue("Question")

            if (text_home.text.length < 3) {

                makeText(this@Amenity_Page1, "지정된 호실이 없습니다.", Toast.LENGTH_SHORT).show()

                Start.setValue("Null")

            } else if (text_home.text.length == 3) {

                Start.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val startValue = snapshot.value

                        val lock1 = Motor.child("Hotel_Motor1")
                        val lock2 = Motor.child("Hotel_Motor2")
                        val lock3 = Motor.child("Hotel_Motor3")

                        Log.d("확인", "Value is: $startValue")

                        if (startValue == "Fail") {

                            makeText(this@Amenity_Page1, "문을 닫아 주세요", Toast.LENGTH_SHORT).show()

                        } else if (startValue == "Success") {
                            makeText(this@Amenity_Page1, "출발 합니다.", Toast.LENGTH_SHORT).show()

                            Motor.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val Hotel_Motor1 = snapshot.child("Hotel_Motor1").value.toString()
                                    val Hotel_Motor2 = snapshot.child("Hotel_Motor2").value.toString()
                                    val Hotel_Motor3 = snapshot.child("Hotel_Motor3").value.toString()
                                    if (Hotel_Motor1 == "First_Unlock") {
                                        go.child("Lock1").setValue("First_Unlock")
                                    }
                                    if (Hotel_Motor2 == "Second_Unlock") {
                                        go.child("Lock2").setValue("Second_Unlock")
                                    }
                                    if (Hotel_Motor3 == "Third_Unlock") {
                                        go.child("Lock3").setValue("Third_Unlock")
                                    }
                                    lock1.setValue("First_Lock")
                                    lock2.setValue("Second_Lock")
                                    lock3.setValue("Third_Lock")
                                    go.child("go").setValue(text_home.text)

                                    val intentAmenityPage3 = Intent(this@Amenity_Page1, Amenity_Page3::class.java)
                                    startActivity(intentAmenityPage3)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }
        }


        //잠금장치 클릭시
        imageButtonEnter.setOnClickListener {
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)
        }


        //NFC
        NFC.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val value = snapshot.value

                if (value == "None") {
                    val Main = Intent(this@Amenity_Page1, MainLoading::class.java)
                    Main.putExtra("key1", "0")
                    startActivity(Main)
                }
                Log.d("파이어", "Value is: $value")
                if (value == "Serving") {
                    val Intent_Serving = Intent(this@Amenity_Page1, MainLoading::class.java)
                    Intent_Serving.putExtra("key1", "2")
                    startActivity(Intent_Serving)
                }
                Log.d("파이어", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })

        val unLockImg = R.drawable.img_lock7
        val lockImg = R.drawable.img_lock2
        val lock1_img = findViewById<ImageView>(R.id.lock1)
        val lock2_img = findViewById<ImageView>(R.id.lock2)
        val lock3_img = findViewById<ImageView>(R.id.lock3)


        Motor.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val image_lock1 = snapshot.child("Hotel_Motor1").value
                val image_lock2 = snapshot.child("Hotel_Motor2").value
                val image_lock3 = snapshot.child("Hotel_Motor3").value

                if (image_lock1 == "First_Lock") {
                    lock1_img.setImageResource(lockImg)
                } else {
                    lock1_img.setImageResource(unLockImg)
                }
                if (image_lock2 == "Second_Lock") {
                    lock2_img.setImageResource(lockImg)
                } else {
                    lock2_img.setImageResource(unLockImg)
                }
                if (image_lock3 == "Third_Lock") {
                    lock3_img.setImageResource(lockImg)
                } else {
                    lock3_img.setImageResource(unLockImg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })


        //Thread 이용하여 매초마다 업데이트 되는 방식
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(10)
                        runOnUiThread { updateYOURthing() }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        t.start()
    }


    private fun updateYOURthing() {

        //시간
        val text_time = findViewById<TextView>(R.id.text_time)
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("a hh:mm:ss")
        val getTime: String = dateFormat.format(date)
        text_time.text = getTime

        //배터리 용량
        val text_battery = findViewById<TextView>(R.id.text_battery)

        var returnData = ""
        try {
            val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = registerReceiver(null, batteryFilter)

            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryPct = level / scale.toFloat()
            returnData = (batteryPct * 100).toInt().toString()
        } catch (e: Exception) {
            e.printStackTrace();
        }
//        Log.d("---", "---");
//        Log.w("//===========//", "================================================");
//        Log.d("ㅇ", "\n" + "[A_Battery > getBatteryRemainder() 메소드 : 현재 남은 배터리 잔량 확인 수행 실시]");
//        Log.d("ㅇ", "\n[배터리 잔량 : $returnData]");
//        Log.w("//===========//", "================================================");
//        Log.d("---", "---");
        text_battery.text = "$returnData%"
        someFunction(returnData)



        return


    }

    fun someFunction(returnData: String) {
        val Image_battery = findViewById<ImageFilterView>(R.id.Image_battery)
        // returnData 값이 변경될 때마다 호출되는 코드 블록
        if (returnData.toInt() == 100) {
            // 이미지를 변경하는 코드
            Image_battery.setImageResource(R.drawable.battery_full)
        }
        if (returnData.toInt() < 100) {
            Image_battery.setImageResource(R.drawable.battery_threequarter)
        } else if (returnData.toInt() <= 50) {
            Image_battery.setImageResource(R.drawable.battery_half)
        } else if (returnData.toInt() <= 30) {
            Image_battery.setImageResource(R.drawable.battery_low)
        }
    }


}



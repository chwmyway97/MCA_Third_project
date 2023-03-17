@file:Suppress("UNREACHABLE_CODE")

package com.chocho.mca_project_tablet

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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

        imageButtonEnter.setOnClickListener {
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(supportFragmentManager,BottomSheetFragment.TAG)
        }


        //버튼 클릭 함수
        fun buttonClick(num: String) {
            val backKey: String = text_home.text.toString()
            if (text_home.text == "호실을 입력해주세요") {
                text_home.text = ""
                text_home.setTextColor(ContextCompat.getColor(this, R.color.purple_CACAE1))
                text_home.append(num)
            } else {

                if (backKey.length > 2) {
                    text_home.text
                } else {
                    text_home.setTextColor(ContextCompat.getColor(this, R.color.purple_CACAE1))
                    text_home.append(num)
                }
            }
            text_home.text
//            if (backKey.length == 3) {
//                text_home.text = "${text_home.text}호"
//            }


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


        imageButtonBack.setOnClickListener {

            val backKey: String = text_home.text.toString()


            if (backKey.length > 0) {
                Log.d("길이", backKey.length.toString())
                if (backKey.length == 5) {
                    text_home.text = backKey.substring(0, backKey.length - 2)
                } else {
                    text_home.text = backKey.substring(0, backKey.length - 1)
                }

            }


        }




        val database = Firebase.database
        val myRef = database.reference.child("NFC")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val value = snapshot.value

                if (value == "0"){
                    val Main = Intent(this@Amenity_Page1,Main_loading::class.java)
                    Main.putExtra("key1","0")
                    startActivity(Main)
                }
                Log.d("파이어", "Value is: $value")
                if (value == "2"){
                    val Intent_Serving = Intent(this@Amenity_Page1,Main_loading::class.java)
                    Intent_Serving.putExtra("key1","2")
                    startActivity(Intent_Serving)
                }
                Log.d("파이어", "Value is: $value")
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
        Log.d("---", "---");
        Log.w("//===========//", "================================================");
        Log.d("ㅇ", "\n" + "[A_Battery > getBatteryRemainder() 메소드 : 현재 남은 배터리 잔량 확인 수행 실시]");
        Log.d("ㅇ", "\n[배터리 잔량 : $returnData]");
        Log.w("//===========//", "================================================");
        Log.d("---", "---");
        text_battery.text = "$returnData%"
        someFunction(returnData)



        return



    }
    
    fun someFunction(returnData : String) {
        val Image_battery =findViewById<ImageFilterView>(R.id.Image_battery)
        // returnData 값이 변경될 때마다 호출되는 코드 블록
        if (returnData.toInt() == 100) {
            // 이미지를 변경하는 코드
            Image_battery.setImageResource(R.drawable.battery_full)
        }
        if( returnData.toInt() < 100){
            Image_battery.setImageResource(R.drawable.battery_threequarter)
        }
        else if (returnData.toInt() <= 50){
            Image_battery.setImageResource(R.drawable.battery_half)
        }
        else if (returnData.toInt() <= 30  ){
            Image_battery.setImageResource(R.drawable.battery_low)
        }
    }



}


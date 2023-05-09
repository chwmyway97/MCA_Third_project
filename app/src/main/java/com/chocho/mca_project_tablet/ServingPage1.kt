package com.chocho.mca_project_tablet

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ServingPage1 : AppCompatActivity() {


    private val database = Firebase.database
    private val module = database.reference.child("Module_Motor")

    private lateinit var moduleListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serving)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val moduleImg = findViewById<ImageFilterView>(R.id.chain)
        val moduleTx = findViewById<TextView>(R.id.chain_tx)

        moduleImg.setOnClickListener {

            module.setValue("Open")

        }

        //모듈 잠금 해제 코드
        moduleListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moduleValue = snapshot.value
                if (moduleValue == "Open"){
                    moduleImg.setImageResource(R.drawable.chain_broken)
                    moduleTx.text = "해제"
                }
                else{
                    moduleImg.setImageResource(R.drawable.chain2)
                    moduleTx.text = "연결"
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        module.addValueEventListener(moduleListener)


        // 데이터 생성
        val items = listOf("1번 테이블", "2번 테이블", "3번 테이블", "4번 테이블", "5번 테이블")


        // 어댑터 생성
        val adapter = ServingAdapter(items,this@ServingPage1)


        // 리사이클러뷰 설정
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



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


}
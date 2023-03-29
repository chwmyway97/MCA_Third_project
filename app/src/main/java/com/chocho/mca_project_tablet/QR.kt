//package com.chocho.mca_project_tablet
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.zxing.integration.android.IntentIntegrator
//
//class QR : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_qr)
//    }
//
//    fun runQRcodeReader(view: View) {
//        val integrator = IntentIntegrator(this.)
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
//        integrator.setPrompt("QR 코드를 인증해 주세요.") //QR 코드 스캔 activity 하단에 띄울 텍스트 설정
//        integrator.setOrientationLocked(false) // 세로,가로 모드를 고정 시키는 역할 false = 세로
//        integrator.setCameraId(0) //전면 1 후면 0 카메라
//        integrator.setBeepEnabled(true) // QR 코드 스캔시 소리 나게 하려면 true 아니면 false 로 지정
//        integrator.setBarcodeImageEnabled(true)
//        integrator.initiateScan() //QR 코드 스캔의 결과 값은 onActivityResult 함수로 전달
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if (result.contents != null) {
//                Toast.makeText(
//                    this, "Scanned : ${result.contents} format: ${result.formatName}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//            if(result.barcodeImagePath != null) {
//                val bitmap = BitmapFactory.decodeFile(result.barcodeImagePath)
//                val imgView : ImageView = findViewById(R.id.scannedBitmap)
//                imgView.setImageBitmap(bitmap)
//            }
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//        //super.onActivityResult()
//    }
//}
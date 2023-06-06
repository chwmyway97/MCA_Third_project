package com.chocho.mca_project_tablet

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*
import java.lang.Math.min

class BottomSheetFragment : BottomSheetDialogFragment() {

    // Firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var motor: DatabaseReference

    val unLockImg = R.drawable.img_lock6
    val lockImg = R.drawable.img_lock3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)




        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog

            bottomSheetDialog.setCanceledOnTouchOutside(true) // 바깥쪽 터치(회색 부분) ture 사라짐 false 안사라짐



            setupRatio(bottomSheetDialog)
        }




        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase
        database = FirebaseDatabase.getInstance()

        motor = database.getReference("Hotel_Motor")

        val motor1 = motor.child("Hotel_Motor1")
        val motor2 = motor.child("Hotel_Motor2")
        val motor3 = motor.child("Hotel_Motor3")

        val motorButton1 = view.findViewById<ImageButton>(R.id.btn_motor1)
        val motorButton2 = view.findViewById<ImageButton>(R.id.btn_motor2)
        val motorButton3 = view.findViewById<ImageButton>(R.id.btn_motor3)

        motor.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                setMotorLockStates(snapshot, listOf(motor1 to motorButton1, motor2 to motorButton2, motor3 to motorButton3))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }
        })

        // SharedPreferences 인스턴스 가져오기
        val prefs = context?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        setLockButtonClickHandler(motorButton1, motor1, prefs, MOTOR1KEY)
        setLockButtonClickHandler(motorButton2, motor2, prefs, MOTOR2KEY)
        setLockButtonClickHandler(motorButton3, motor3, prefs, MOTOR3KEY)

    }

    private fun setMotorLockStates(snapshot: DataSnapshot, motors: List<Pair<DatabaseReference, ImageButton>>) {
        for ((motor, button) in motors) {
            val value = snapshot.child(motor.key!!).value.toString()
            setLockImage(value, button)
        }
    }

    private fun setLockButtonClickHandler(btnMotor: ImageButton, motor: DatabaseReference, prefs: SharedPreferences?, prefsKey: String) {

        btnMotor.setOnClickListener {
            handleLockButtonClick(motor, btnMotor, lockImg, unLockImg, prefs, prefsKey)
        }
    }

    private fun setLockImage(value: Any?, imageView: ImageView) {

        if (value == "First_Lock" || value == "Second_Lock" || value == "Third_Lock") imageView.setImageResource(lockImg)
        else imageView.setImageResource(unLockImg)

    }

    private fun updateMotorLockState(motorRef: DatabaseReference, state: String) {
        motorRef.setValue(state)
    }

    private fun handleLockButtonClick(
        motorRef: DatabaseReference,
        btnLock: ImageButton,
        lockImg: Int,
        unLockImg: Int,
        prefs: SharedPreferences?,
        prefsKey: String
    ) {
        motorRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()

                // lock 값이 0이면 1로 업데이트, 1이면 0으로 업데이트
                if (value.endsWith("_Lock")) {
                    updateMotorLockState(motorRef, value.replace("_Lock", "_Unlock"))
                    btnLock.setImageResource(unLockImg)
                    prefs?.edit()?.putBoolean(prefsKey, true)?.apply()
                } else {
                    updateMotorLockState(motorRef, value.replace("_Unlock", "_Lock"))
                    btnLock.setImageResource(lockImg)
                    prefs?.edit()?.putBoolean(prefsKey, false)?.apply()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)


        bottomSheet.setBackgroundResource(android.R.color.transparent)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight() //높이 지정
        bottomSheet.layoutParams = layoutParams


        behavior.state = BottomSheetBehavior.STATE_EXPANDED



        behavior.isDraggable = false
        behavior.isHideable = false //아래로 드래그 하여 닫을수 있는지 여부 설정


//        val button = bottomSheetDialog.findViewById<ImageButton>(R.id.button)
//        button?.setOnClickListener { bottomSheetDialog.dismiss() }



    }
    //바텀 시트 높이 비율 90프로로 한다
    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 90 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return min(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    companion object {
        const val TAG = "BottomSheetFragment"
        const val MOTOR1KEY = "Hotel_Motor1"
        const val MOTOR2KEY = "Hotel_Motor2"
        const val MOTOR3KEY = "Hotel_Motor3"

        const val PREFS_FILENAME = "com.chocho.myapp.prefs"
    }

}
package com.chocho.mca_project_tablet

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*
import java.lang.Math.min

// 상수 선언
private const val PREFS_FILENAME = "com.chocho.myapp.prefs"
private const val MOTOR1KEY = "Hotel_Motor1"
private const val MOTOR2KEY = "Hotel_Motor2"
private const val MOTOR3KEY = "Hotel_Motor3"

class BottomSheetFragment : BottomSheetDialogFragment() {

    // Firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var motor: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            bottomSheetDialog.setCanceledOnTouchOutside(false) // 바깥쪽 터치로 다이얼로그가 사라지는 것을 방지
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

        val btn_lock1 = view.findViewById<ImageButton>(R.id.btn_lock1)
        val btn_lock2 = view.findViewById<ImageButton>(R.id.btn_lock2)
        val btn_lock3 = view.findViewById<ImageButton>(R.id.btn_lock3)

        val unLockImg = R.drawable.img_lock6
        val lockImg = R.drawable.img_lock3


        motor.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val image_lock1 = snapshot.child("Hotel_Motor1").value
                val image_lock2 = snapshot.child("Hotel_Motor2").value
                val image_lock3 = snapshot.child("Hotel_Motor3").value

                if (image_lock1 == "First_Lock") {
                    btn_lock1.setImageResource(lockImg)
                } else {
                    btn_lock1.setImageResource(unLockImg)
                }
                if (image_lock2 == "Second_Lock") {
                    btn_lock2.setImageResource(lockImg)
                } else {
                    btn_lock2.setImageResource(unLockImg)
                }
                if (image_lock3 == "Third_Lock") {
                    btn_lock3.setImageResource(lockImg)
                } else {
                    btn_lock3.setImageResource(unLockImg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("파이어", "Failed to read value.", error.toException())
            }

        })

        // SharedPreferences 인스턴스 가져오기
        val prefs = context?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        // lock1 값이 저장된 SharedPreferences에서 가져오기
        val lock1Pref = prefs?.getBoolean(MOTOR1KEY, false)
        val lock2Pref = prefs?.getBoolean(MOTOR2KEY, false)
        val lock3Pref = prefs?.getBoolean(MOTOR3KEY, false)

        // 기존 상태 적용
        if (lock1Pref == true) {
            btn_lock1.setImageResource(unLockImg)
        } else {
            btn_lock1.setImageResource(lockImg)
        }
        if (lock2Pref == true) {
            btn_lock2.setImageResource(unLockImg)
        } else {
            btn_lock2.setImageResource(lockImg)
        }
        if (lock3Pref == true) {
            btn_lock3.setImageResource(unLockImg)
        } else {
            btn_lock3.setImageResource(lockImg)
        }

        btn_lock1?.setOnClickListener {
            motor1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value.toString()

                    // lock 값이 0이면 1로 업데이트, 1이면 0으로 업데이트
                    if (value == "First_Lock") {
                        motor1.setValue("First_Unlock")
                        btn_lock1.setImageResource(unLockImg)
                        prefs?.edit()?.putBoolean(MOTOR1KEY, true)?.apply()
                    } else {
                        motor1.setValue("First_Lock")
                        btn_lock1.setImageResource(lockImg)
                        prefs?.edit()?.putBoolean(MOTOR1KEY, false)?.apply()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "Failed to read value.", error.toException())
                }
            })
        }
        btn_lock2?.setOnClickListener {
            motor2.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value.toString()

                    // lock 값이 0이면 1로 업데이트, 1이면 0으로 업데이트
                    if (value == "Second_Lock") {
                        motor2.setValue("Second_Unlock")
                        btn_lock2.setImageResource(unLockImg)
                        prefs?.edit()?.putBoolean(MOTOR2KEY, true)?.apply()
                    } else {
                        motor2.setValue("Second_Lock")
                        btn_lock2.setImageResource(lockImg)
                        prefs?.edit()?.putBoolean(MOTOR2KEY, false)?.apply()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "Failed to read value.", error.toException())
                }
            })
        }
        btn_lock3?.setOnClickListener {
            motor3.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value.toString()

                    // lock 값이 0이면 1로 업데이트, 1이면 0으로 업데이트
                    if (value == "Third_Lock") {
                        motor3.setValue("Third_Unlock")
                        btn_lock3.setImageResource(unLockImg)
                        prefs?.edit()?.putBoolean(MOTOR3KEY, true)?.apply()
                    } else {
                        motor3.setValue("Third_Lock")
                        btn_lock3.setImageResource(lockImg)
                        prefs?.edit()?.putBoolean(MOTOR3KEY, false)?.apply()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "Failed to read value.", error.toException())
                }
            })

        }

    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)

        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        behavior.isHideable = false

        val button = bottomSheetDialog.findViewById<ImageButton>(R.id.button)
        button?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


    }

    //    override fun onStart() {
//        super.onStart()
//        mBottomBehavior = BottomSheetBehavior.from(requireView().parent as View)
//        (mBottomBehavior as BottomSheetBehavior<*>).maxWidth = ViewGroup.LayoutParams.MATCH_PARENT
//        (mBottomBehavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_EXPANDED
//    }
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
    }

}
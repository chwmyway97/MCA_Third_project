package com.chocho.mca_project_tablet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SubActivityRepo {

    fun getData(): LiveData<MutableList<Meat>> {

        val mutableData = MutableLiveData<MutableList<Meat>>()
        val database = Firebase.database
        val myRef = database.getReference("master")

        myRef.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<Meat> = mutableListOf()

            override fun onDataChange(snapshot: DataSnapshot) {

                listData.clear()

                if (snapshot.exists()) {

                    for (meatSnapshot in snapshot.children) {

                        val getData = meatSnapshot.getValue(Meat::class.java)

                        listData.add(getData!!)

                        mutableData.value = listData

                    }
                } else {

                    listData.clear()

                    mutableData.value = listData

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return mutableData
    }
}
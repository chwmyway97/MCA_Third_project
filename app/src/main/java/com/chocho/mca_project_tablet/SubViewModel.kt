package com.chocho.mca_project_tablet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubViewModel : ViewModel() {

    private val repo = Table1ActivityRepo()
    private val repo2 = Table2ActivityRepo()


    fun table1Data(): LiveData<MutableList<Meat>>{

        val mutableData = MutableLiveData<MutableList<Meat>>()

        repo.getData().observeForever{ mutableData.value = it }

        return mutableData
    }

    fun table2Data() : LiveData<MutableList<Meat>>{

        val mutableData = MutableLiveData<MutableList<Meat>>()

        repo2.getData().observeForever{ mutableData.value = it }

        return mutableData
    }

}
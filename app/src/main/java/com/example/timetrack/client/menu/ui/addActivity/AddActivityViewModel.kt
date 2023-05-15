package com.example.timetrack.client.menu.ui.addActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddActivityViewModel : ViewModel() {
    val time = MutableLiveData<Long>()

}
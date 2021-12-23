package com.dmcs.op.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dmcs.op.R

class NewMiniViewModel (application: Application) : AndroidViewModel(application){
    var op_list = MutableLiveData<ArrayList<String>>()
}
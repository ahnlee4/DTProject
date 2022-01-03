package com.dmcs.op.model

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class BluetoothViewModel (application: Application) : AndroidViewModel(application){
    var bluetooth_list: MutableLiveData<ArrayList<BluetoothDevice>> = MutableLiveData<ArrayList<BluetoothDevice>>()
    var bluetooth_list2: MutableLiveData<ArrayList<BluetoothDevice>> = MutableLiveData<ArrayList<BluetoothDevice>>()
    var info:MutableLiveData<String> = MutableLiveData<String>()
    var is_loading:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
}
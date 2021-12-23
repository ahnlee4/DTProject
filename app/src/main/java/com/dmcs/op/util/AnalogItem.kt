package com.dmcs.op.util

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmcs.op.databinding.RecyclerviewAnalogItemBinding

class AnalogItem{
    var name:String = ""
    var value:String = ""
    var sign:String = ""

    constructor () {
    }
    constructor (name:String, value:String, sign:String) {
        this.name = name
        this.value = value
        this.sign = sign
    }
}

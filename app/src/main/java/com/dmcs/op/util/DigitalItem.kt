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

class DigitalItem{
    var name:String? = null
    var check:Int = 0

    constructor () {
    }
    constructor (name:String?, check:Int) {
        this.name = name
        this.check = check
    }
}

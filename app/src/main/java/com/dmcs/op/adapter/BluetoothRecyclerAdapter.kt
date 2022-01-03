package com.dmcs.op.adapter

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dmcs.op.R
import kotlinx.android.synthetic.main.recyclerview_blue_item.view.*

class BluetoothRecyclerAdapter(var data: MutableLiveData<ArrayList<BluetoothDevice>>, val itemClick: (pos: Int, BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<BluetoothRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_blue_item, parent, false)) {
        var tv1 = itemView.text1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(parent)
    }

    override fun getItemCount(): Int {
        if(data.value==null)
            return 0
        else
            return data.value!!.size
    }

    var row = -1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val safePosition = holder.adapterPosition
        data.value!!.get(safePosition).let { item ->
            with(holder) {
                tv1.text = item.name

                itemView.setOnClickListener{
                    itemClick.invoke(safePosition, item)
                }
            }
        }
    }
}
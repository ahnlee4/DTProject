package com.dmcs.op.fragment

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmcs.op.AppData
import com.dmcs.op.MyFragment
import com.dmcs.op.NewMiniActivity
import com.dmcs.op.R
import com.dmcs.op.adapter.BluetoothRecyclerAdapter
import com.dmcs.op.databinding.FragmentBluetoothBinding
import com.dmcs.op.model.BluetoothViewModel
import com.dmcs.op.util.Util
import kotlinx.android.synthetic.main.fragment_bluetooth.*

class BluetoothFragment() : MyFragment() {
    var mBinding : FragmentBluetoothBinding? = null
    var viewModel: BluetoothViewModel? = null
    var data = MutableLiveData<ArrayList<BluetoothDevice>>()
    var data2 = MutableLiveData<ArrayList<BluetoothDevice>>()
    var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun getTitle() : String{
        return ""
    }

    override fun Refresh() {
    }

    override fun Exit() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bluetooth, container, false)
        viewModel = ViewModelProvider(this)[BluetoothViewModel::class.java]
        mBinding!!.bluetoothmodel = viewModel
        mBinding!!.lifecycleOwner = this

        mBinding!!.bluetoothList.layoutManager = LinearLayoutManager(activity)
        mBinding!!.bluetoothList2.layoutManager = LinearLayoutManager(activity)

        val dataObserver: Observer<ArrayList<BluetoothDevice>> =
                Observer { livedata ->
                    data.value = livedata
                    var newAdapter = BluetoothRecyclerAdapter(data) { pos, item ->
                        if (mBluetoothAdapter.isDiscovering()) {
                            mBluetoothAdapter!!.cancelDiscovery()
                            viewModel!!.info.postValue("")
                            viewModel!!.is_loading.postValue(false)

                        }
                        AppData.Bluetooth.bluetooth_name = item.name
                        AppData.Bluetooth.mRemoteDevice = item

                        Handler(Looper.getMainLooper()).post {
                            NewMiniActivity.sub_layout_active = false
                            NewMiniActivity.mBinding.subFragment.visibility = View.GONE
                        }

                        NewMiniActivity.Connect(requireActivity(), 1)

                    }
                    mBinding!!.bluetoothList.adapter = newAdapter
                }

        viewModel!!.bluetooth_list.observe(viewLifecycleOwner, dataObserver)

        val dataObserver2: Observer<ArrayList<BluetoothDevice>> =
                Observer { livedata ->
                    data2.value = livedata
                    var newAdapter2 = BluetoothRecyclerAdapter(data2) { pos, item ->
                        mBluetoothAdapter!!.cancelDiscovery()
                        viewModel!!.info.postValue("")
                        viewModel!!.is_loading.postValue(false)

                        AppData.Bluetooth.bluetooth_name = item.name
                        AppData.Bluetooth.mRemoteDevice = item

                        var thread = Thread(Runnable {
                            try {
                                var pinBytes = ("0000").toByteArray()
                                if (AppData.Bluetooth.mRemoteDevice != null) {
                                    AppData.Bluetooth.mRemoteDevice!!.setPin(pinBytes)
                                    if(AppData.Bluetooth.mRemoteDevice!!.createBond()){
                                        if(activity!=null) {
                                            NewMiniActivity.Connect(requireActivity(), 1)

                                            Handler(Looper.getMainLooper()).post {
                                                NewMiniActivity.sub_layout_active = false
                                                NewMiniActivity.mBinding.subFragment.visibility = View.GONE
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        })
                        thread.isDaemon = true
                        thread.start()
                    }
                    mBinding!!.bluetoothList2.adapter = newAdapter2
                }

        viewModel!!.bluetooth_list2.observe(viewLifecycleOwner, dataObserver2)
        mBinding!!.bluetooth.setOnClickListener(View.OnClickListener {
            var thread = Thread(Runnable {
                find_bluetooth()
            })
            thread.isDaemon = true
            thread.start()
        })


        AppData.Bluetooth.pairing_list.clear()
        if (mBluetoothAdapter == null) {
            Util.status("No bluetooth adapter available")
        }

        if (!mBluetoothAdapter!!.isEnabled()) {
            val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetooth, 0)
        }

        val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter!!.getBondedDevices()
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                AppData.Bluetooth.pairing_list.add(device)
                viewModel!!.bluetooth_list.postValue(ArrayList(AppData.Bluetooth.pairing_list))
            }
        }
        return mBinding!!.root
    }


    fun find_bluetooth():Boolean{
        try {
            if (mBluetoothAdapter == null) {
                Util.status("No bluetooth adapter available")
            }

            if (!mBluetoothAdapter!!.isEnabled()) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetooth, 0)
            }

            if (!mBluetoothAdapter.isDiscovering()) {
                viewModel!!.info.postValue("찾는중..")
                viewModel!!.is_loading.postValue(true)
                viewModel!!.is_search.postValue(true)
                AppData.Bluetooth.bluetooth_list.clear()
                var start:Long = System.currentTimeMillis()
                while(System.currentTimeMillis()-start<60000){
                    mBluetoothAdapter!!.startDiscovery()
                    viewModel!!.bluetooth_list2.postValue(ArrayList(AppData.Bluetooth.bluetooth_list))
                    Thread.sleep(1000)
                }
                mBluetoothAdapter!!.cancelDiscovery()

                viewModel!!.info.postValue("")
                viewModel!!.is_loading.postValue(false)
                viewModel!!.is_search.postValue(false)
            }else{
                mBluetoothAdapter!!.cancelDiscovery()
                viewModel!!.info.postValue("")
                viewModel!!.is_loading.postValue(false)
            }
        } catch (e: Exception) {
            Util.status("connection failed: " + e.message)
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mBluetoothAdapter!!.cancelDiscovery()
    }
}
package com.dmcs.op

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.dmcs.op.databinding.ActivityNewMiniBinding
import com.dmcs.op.model.NewMiniViewModel
import com.dmcs.op.packet.MINI250EPSPacket
import com.dmcs.op.util.*
import java.io.*


class NewMiniActivity : AppCompatActivity(){
    lateinit var viewModel:NewMiniViewModel
    lateinit var mBinding : ActivityNewMiniBinding
    var data = MutableLiveData<ArrayList<String>>()
    var op_list = ArrayList<String>()
    var mMINI250EPSPacket : MINI250EPSPacket? = null
    var serial_socket : SerialSocket? = null

    var init = false
    var stopWorker = false
    var serialThread: Thread? = null

    override fun onStart() {
        super.onStart()
        serialThread?.interrupt()

        serialThread = SerialThread()
        serialThread?.isDaemon = true
        serialThread?.start()
    }

    override fun onStop() {
        super.onStop()
        stopWorker = true
        serialThread!!.interrupt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_mini)
        checkPermission()
        setIntent()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_mini)
        viewModel = ViewModelProvider(this)[NewMiniViewModel::class.java]
        mBinding.newmodel = viewModel
        mBinding.lifecycleOwner = this

        serial_socket = SerialSocket(this)
        mMINI250EPSPacket = MINI250EPSPacket(serial_socket!!)

        mBinding.setKey.setOnClickListener(View.OnClickListener {
        })

        mBinding.modeKey.setOnClickListener(View.OnClickListener {
        })

        mBinding.downKey.setOnClickListener(View.OnClickListener { })

        mBinding.upKey.setOnClickListener(View.OnClickListener {})

        for (i in 0 until 9)
            op_list.add("                     ")
    }

    fun setIntent(){
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(AppData.Main.INTENT_ACTION_GRANT_USB) // 복수로 등록가능
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED) // 복수로 등록가능
        registerReceiver(broadcastReceiver, IntentFilter(intentFilter))
    }

    var permission_list = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    fun checkPermission() {
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        for (permission in permission_list) {
            //권한 허용 여부를 확인한다.
            val chk = checkCallingOrSelfPermission(permission)
            if (chk == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permission_list, 0)
            }
        }
    }

    var i_array:ByteArray? = null
    inner class SerialThread : Thread(){
        override fun run() {
            try {
                stopWorker = false
                init = false
                while (!currentThread().isInterrupted() && !stopWorker) {
                    if(init) {

                    }else{

                    }
                }
            }catch(e: java.lang.Exception){
                e.printStackTrace()
            }
        }
    }
}
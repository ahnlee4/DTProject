package com.dmcs.op

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.TimeAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.dmcs.op.databinding.ActivityNewMiniBinding
import com.dmcs.op.fragment.BluetoothFragment
import com.dmcs.op.model.NewMiniViewModel
import com.dmcs.op.packet.MINI110CANPacket
import com.dmcs.op.packet.MINI250EPSPacket
import com.dmcs.op.util.*
import java.lang.Exception
import kotlin.experimental.and


class NewMiniActivity : AppCompatActivity(){
    companion object {
        lateinit var viewModel:NewMiniViewModel
        lateinit var mBinding : ActivityNewMiniBinding
        var data = MutableLiveData<ArrayList<String>>()
        var mMINI250EPSPacket : MINI250EPSPacket? = null
        var mMINI110CANPacket : MINI110CANPacket? = null
        var serial_socket : SerialSocket? = null
        lateinit var mFragmentManager: FragmentManager

        var smart_lock_flag = -1
        var key = -1
        var stopWorker = false
        var serialThread: Thread? = null

        var sub_layout_active = false
        fun replaceContentSubFragment(fragment: BluetoothFragment) {
            sub_layout_active = true
            mFragmentManager.beginTransaction().replace(R.id.sub_fragment, fragment, "MY_FRAGMENT2").commit()
            mBinding.subFragment.visibility = View.VISIBLE

            val animation1 = ObjectAnimator.ofFloat(mBinding!!.subFragment, "alpha", 0f, 1f)
            animation1.duration = 400
            animation1.start()
        }

        var connect_wait = 0
        var is_running = false
        fun Connect(mActivity: Activity, type: Int) {
            var thread = Thread(Runnable {
                try {
                    if (!is_running && !AppData.Connect) {
                        is_running = true

                        var frist = true
                        while (!AppData.Usb_Detached || frist) {
                            Handler(Looper.getMainLooper()).post {
                                mBinding.connectKey.text = "연결중.."
                                circle_wait_anim(mBinding.circle)
                            }
                            Thread.sleep(connect_wait.toLong())

                            frist = false
                            if (serial_socket?.connect(type) == true) {
                                Handler(Looper.getMainLooper()).post {
                                    mBinding.connectKey.visibility = View.INVISIBLE
                                    mBinding.status.visibility = View.VISIBLE
                                    circle_connect_anim(mBinding.circle)
                                    Util.status_log("CONNECT")
                                }
                                Thread.sleep(100)
                                AppData.Usb_Detached = false
                                AppData.disconnect_flag = true
                                AppData.Connect_Type = type
                                AppData.Connect = true
                                AppData.disconnect_time = System.currentTimeMillis()

                                serialThread = SerialThread(mActivity)
                                serialThread?.isDaemon = true
                                serialThread?.start()

                                break
                            }

                            Thread.sleep(100)
                        }
                        if (!AppData.Connect) {
                            Handler(Looper.getMainLooper()).post {
                                mBinding.connectKey.text = "연결"
                                circle_disconnect_anim(mBinding.circle)
                                serial_socket?.detached()
                            }
                        }
                        is_running = false
                    }
                } catch (e: java.lang.Exception) {
                    Handler(Looper.getMainLooper()).post {
                        mBinding.connectKey.text = "연결"
                        circle_disconnect_anim(mBinding.circle)
                        serial_socket?.detached()
                    }
                    is_running = false
                    e.printStackTrace()
                }
            })
            thread.isDaemon = true
            thread.start()
        }

        fun startAnimation(view: View, start: Int, mid: Int, end: Int) {
            val evaluator = ArgbEvaluator()
            val preloader = view
            preloader.visibility = View.VISIBLE
            val gradient = preloader.background as GradientDrawable
            val animator = TimeAnimator.ofFloat(0.0f, 1.0f)
            animator.duration = 1000
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.REVERSE
            animator.addUpdateListener { valueAnimator ->
                val fraction = valueAnimator.animatedFraction
                val newStrat = evaluator.evaluate(fraction, start, end) as Int
                val newMid = evaluator.evaluate(fraction, mid, start) as Int
                val newEnd = evaluator.evaluate(fraction, end, mid) as Int
                val newArray = intArrayOf(newStrat, newMid, newEnd)
                gradient.colors = newArray
            }
            animator.start()
        }

        fun stopAnimation(view: Int, activity: Activity) {
            ObjectAnimator.ofFloat(activity.findViewById(view), "alpha", 0f).setDuration(125).start()
        }

        fun circle_disconnect_anim(view:View){
            startAnimation(view, view.resources.getColor(R.color.teal), view.resources.getColor(R.color.teal_200), view.resources.getColor(R.color.teal_700))
        }
        fun circle_connect_anim(view:View){
            startAnimation(view, view.resources.getColor(R.color.colorOn2), view.resources.getColor(R.color.colorOn), view.resources.getColor(R.color.colorAccentStroke_wait))
        }
        fun circle_wait_anim(view:View){
            startAnimation(view, view.resources.getColor(R.color.colorAccent), view.resources.getColor(R.color.colorAccent2), view.resources.getColor(R.color.purple))
        }

        class SerialThread(val activity:Activity) : Thread(){
            override fun run() {
                try {
                    stopWorker = false
                    var init = true
                    while (!currentThread().isInterrupted() && !stopWorker) {
                        if(AppData.Connect){
                            if(init) {
                                // 버전
                                var i_array:ByteArray? = mMINI110CANPacket?.send_Version_Read(AppData.Protocol.MINI110_CON_ID)
                                if (i_array != null) {
                                    AppData.Main.Mini_Version = Util.byte2short(i_array, 3)
                                    if(AppData.Main.Mini_Version.toByte()==AppData.Trac_Con_DT_Version){
                                        AppData.ID_NUMBER = AppData.Protocol.MINI110_CON_ID

                                        i_array = mMINI110CANPacket?.send_New_Version()
                                        if (i_array != null) {
                                            Handler(Looper.getMainLooper()).post{
                                                mBinding.version.text = String.format("Ver %.1f", Util.byte2short(i_array, 3) * 0.1)
                                            }
                                            init = false

                                            mMINI110CANPacket?.send_Remote_Start()
                                            AppData.Lock_flag = true
                                            show_object()
                                        }else{
                                            Handler(Looper.getMainLooper()).post {
                                                mBinding.version.text = "데이터 없음"
                                            }
                                        }
                                    }
                                }
                            }else{
                                if(cnt%10==0){
                                    Handler(Looper.getMainLooper()).post{
                                        mBinding.log.text = AppData.log
                                    }
                                }
                                cnt++
                                data_read(activity)
                            }
                        }
                    }
                }catch (e: java.lang.Exception){
                    e.printStackTrace()
                }
                disconnect()
            }
        }

        var cnt:Int = 0

        var before_gage_stage = 0
        var current_gage_stage = 0
        var before_current_gage = -1
        var current_gage = 0
        var current_stage = 20
        var gages:Array<ImageView>? = null

        var before_smart_key:Boolean? = null
        var before_brake:Boolean? = null
        var before_turtle:Boolean? = null
        var before_error_code = 0

        fun data_read(activity:Activity){
            var i_array: ByteArray? = mMINI110CANPacket?.send_Analog_ALL_Data(23)
            if (i_array != null) {
                // 오류코드
                var error_code = Util.byte2short(i_array, 5)
                if(before_error_code==0 && error_code==0){
                    Handler(Looper.getMainLooper()).post {
                        mBinding.error.visibility = View.INVISIBLE
                    }
                }else if(before_error_code!=0 || error_code!=0){
                    Handler(Looper.getMainLooper()).post {
                        mBinding.error.visibility = View.VISIBLE
                    }
                }
                Handler(Looper.getMainLooper()).post {
                    mBinding.error.text = Util.mini_error_text(error_code)
                }
                before_error_code = error_code

                //motor
                Handler(Looper.getMainLooper()).post {
                    mBinding.speed.text = String.format("%.1f", Util.byte2short(i_array, 17) * 0.1)
                    if(Util.byte2short(i_array, 17) == 0){
                        mBinding.lockKey.isEnabled = true
                    }else{
                        mBinding.lockKey.isEnabled = false
                    }
                }

                // battery
                current_gage = Util.byte2short(i_array, 3) / 10
                if(before_current_gage!=current_gage){
                    Handler(Looper.getMainLooper()).post {
                        mBinding.batteryText.text = "배터리 " + current_gage.toString() + "%"
                        println(current_gage)
                    }

                    before_current_gage = current_gage

                    current_gage_stage = current_gage / 10
                    if(before_gage_stage>current_gage_stage){
                        for(i in before_gage_stage until current_gage_stage){
                            Handler(Looper.getMainLooper()).post(Runnable {
                                gages?.get(i)?.visibility = View.INVISIBLE
                            })
                        }
                        before_gage_stage = current_gage_stage
                    }else if(before_gage_stage<current_gage_stage){
                        for(i in before_gage_stage until current_gage_stage){
                            Handler(Looper.getMainLooper()).post(Runnable {
                                gages?.get(i)?.visibility = View.VISIBLE
                            })
                        }
                        before_gage_stage = current_gage_stage
                    }
                }

                // rf_remocon_bit
                var rf_remocon_bit = Util.byte2short(i_array, 3+(21*2))
                var brake = (rf_remocon_bit.toByte() and 1) == 1.toByte()
                var smart_key = (rf_remocon_bit.toByte() and 2) == 2.toByte()
                var turtle = (rf_remocon_bit.toByte() and 4) == 4.toByte()


                if(before_brake!=brake){
                    Handler(Looper.getMainLooper()).post {
                        if(mBinding.stopKeyText.text == "해제") {
                            smart_lock_flag = 0
                            smart_lock_check()
                        }

                        if(brake) {
                            mBinding.stopKeyText.text = "주행"
                            circle_connect_anim(mBinding.circle)
                            mBinding.statusBreak.text = "      정지"
                            mBinding.statusBreak.setTextColor(activity.resources.getColor(R.color.colorOn))
                            mBinding.stopKeyIcon.setImageResource(R.drawable.ic_baseline_directions_car_24)
                        }else{
                            mBinding.stopKeyText.text = "정지"
                            circle_disconnect_anim(mBinding.circle)
                            mBinding.statusBreak.text = "      주행"
                            mBinding.statusBreak.setTextColor(activity.resources.getColor(R.color.colorAccentStroke_disconnect))
                            mBinding.stopKeyIcon.setImageResource(R.drawable.ic_baseline_do_disturb_24)
                        }
                        before_brake=brake
                    }
                }

                if(before_smart_key!=smart_key){
                    Handler(Looper.getMainLooper()).post {
                        if(smart_key) {
                            mBinding.lockOnKey.isOn = true
                            mBinding.statusSmart.text = "      사용"
                            mBinding.statusSmart.setTextColor(activity.resources.getColor(R.color.colorAccentStroke_disconnect))
                            smart_lock_flag = 1
                            mBinding.stopKeyText.text = "주행"
                        }else{
                            mBinding.lockOnKey.isOn = false
                            mBinding.statusSmart.text = "  미사용"
                            mBinding.statusSmart.setTextColor(activity.resources.getColor(R.color.white))
                            smart_lock_flag = 0
                        }
                        smart_lock_check()
                        before_smart_key=smart_key
                    }
                }

                if(before_turtle!=turtle){
                    Handler(Looper.getMainLooper()).post {
                        if(turtle) {
                            mBinding.modeKey.text = "고속"
                            mBinding.statusMode.text = "      저속"
                            mBinding.statusMode.setTextColor(activity.resources.getColor(R.color.white))
                        }else{
                            mBinding.modeKey.text = "저속"
                            mBinding.statusMode.text = "      고속"
                            mBinding.statusMode.setTextColor(activity.resources.getColor(R.color.colorAccentStroke_disconnect))
                        }
                        before_turtle=turtle
                    }
                }


                current_stage = Util.byte2short(i_array, 3+(22*2))
                if(current_stage==20){
                    Handler(Looper.getMainLooper()).post {
                        mBinding.upKey.isEnabled = false
                        mBinding.downKey.isEnabled = true
                    }
                }else if(current_stage==1){
                    Handler(Looper.getMainLooper()).post {
                        mBinding.downKey.isEnabled = false
                        mBinding.upKey.isEnabled = true
                    }
                }else{
                    Handler(Looper.getMainLooper()).post {
                        mBinding.upKey.isEnabled = true
                        mBinding.downKey.isEnabled = true
                    }
                }

            }
        }

        fun smart_lock_check(){
            if(smart_lock_flag==1){
                smart_lock_flag = -1
                mBinding.statusBreak.text = "      잠금"
                mBinding.statusBreak.setTextColor(mBinding.root.resources.getColor(R.color.colorOn2))
                mBinding.stopKeyIcon.setImageResource(R.drawable.ic_baseline_lock_open_24)
                mBinding.stopKeyText.text = "해제"
                mBinding.modeKey.isEnabled = false
                mBinding.upKey.isEnabled = false
                mBinding.downKey.isEnabled = false
            }else if(smart_lock_flag==0){
                smart_lock_flag = -1
                mBinding.statusBreak.text = "      정지"
                mBinding.statusBreak.setTextColor(mBinding.root.resources.getColor(R.color.colorOn))
                mBinding.stopKeyIcon.setImageResource(R.drawable.ic_baseline_directions_car_24)
                mBinding.stopKeyText.text = "주행"

                mBinding.modeKey.isEnabled = true
                mBinding.upKey.isEnabled = true
                mBinding.downKey.isEnabled = true
            }
        }

        fun disconnect(){
            Handler(Looper.getMainLooper()).post{
                hide_object()
                connect_wait = 2000
                serial_socket?.detached()
                mBinding.version.text = "버전"
                mBinding.connectKey.text = "연결"
                mBinding.status.visibility = View.INVISIBLE
                mBinding.connectKey.visibility = View.VISIBLE
                mBinding.batteryText.text = "배터리"
                mBinding.error.text = ""
                mBinding.speed.text = "0.0 [%]"
                for(i in 0 until 10){
                    gages?.get(i)?.visibility = View.INVISIBLE
                }


                mBinding.lockKey.visibility = View.VISIBLE
                mBinding.lockLayout.visibility = View.INVISIBLE

                smart_lock_flag = -1

                before_gage_stage = 0
                current_gage_stage = 0
                before_current_gage = -1
                current_gage = 0
                current_stage = 20

                before_smart_key = null
                before_brake = null
                before_turtle = null
                before_error_code = 0


                circle_disconnect_anim(mBinding.circle)
                serialThread?.interrupt()
            }
        }

        fun show_object(){
            Handler(Looper.getMainLooper()).post {
                mBinding.version.animate().translationX(0f).start()
                mBinding.error.animate().translationX(0f).start()
                mBinding.batteryLayout.animate().translationX(0f).start()
                mBinding.stopKeyLayout.animate().translationX(0f).start()
                mBinding.modeKey.animate().translationX(0f).start()
                mBinding.smartLayout.animate().translationX(0f).start()
                mBinding.speedLayout.animate().translationX(0f).start()

            }
        }
        fun hide_object(){
            Handler(Looper.getMainLooper()).post {
                mBinding.version.animate().translationX(-1000f).start()
                mBinding.error.animate().translationX(-1000f).start()
                mBinding.batteryLayout.animate().translationX(-1000f).start()
                mBinding.stopKeyLayout.animate().translationX(-1000f).start()
                mBinding.modeKey.animate().translationX(-1000f).start()
                mBinding.smartLayout.animate().translationX(1000f).start()
                mBinding.speedLayout.animate().translationX(1000f).start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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

        mFragmentManager = supportFragmentManager
        circle_disconnect_anim(mBinding.circle)
        gages = arrayOf(
            mBinding.battery0, mBinding.battery1, mBinding.battery2, mBinding.battery3, mBinding.battery4, mBinding.battery5, mBinding.battery6,
            mBinding.battery7, mBinding.battery8, mBinding.battery9
        )

        serial_socket = SerialSocket(this)
        mMINI250EPSPacket = MINI250EPSPacket(serial_socket!!)
        mMINI110CANPacket = MINI110CANPacket(serial_socket!!)

        mBinding.stopKeyLayout.setOnClickListener(View.OnClickListener {
            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x01) }).start()
            Util.status_log("STOP")
            key = 1
        })

        mBinding.modeKey.setOnClickListener(View.OnClickListener {
            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x02) }).start()
            Util.status_log("MODE")
            key = 2
        })

        mBinding.downKey.setOnTouchListener(View.OnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                is_clicking = true
                down_thread = DownThread()
                down_thread!!.start()
            }else if(event.action == MotionEvent.ACTION_UP) {
                is_clicking = false
                down_thread?.interrupt()
            }
            return@OnTouchListener true
        })

        mBinding.upKey.setOnTouchListener(View.OnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                is_clicking = true
                up_thread = UpThread()
                up_thread!!.start()
            }else if(event.action == MotionEvent.ACTION_UP) {
                is_clicking = false
                up_thread?.interrupt()
            }
            return@OnTouchListener true
        })

//        mBinding.upKey.setOnClickListener(View.OnClickListener {
//            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x03) }).start()
//            Util.status_log("UP")
//            key = 3
//        })
//
//        mBinding.downKey.setOnClickListener(View.OnClickListener {
//            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x04) }).start()
//            Util.status_log("DOWN")
//            key = 4
//        })


        mBinding.lockKey.setOnClickListener(View.OnClickListener {
            var dialog: Dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog)
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            var yes = dialog.findViewById<TextView>(R.id.yes)
            yes.setOnClickListener(View.OnClickListener {
                mBinding.lockKey.visibility = View.INVISIBLE
                mBinding.lockLayout.visibility = View.VISIBLE
                dialog.dismiss()
            })
            var no = dialog.findViewById<TextView>(R.id.no)
            no.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })
            dialog.show()
        })

        mBinding.lockLayout.setOnClickListener(View.OnClickListener {
            mBinding.lockKey.visibility = View.VISIBLE
            mBinding.lockLayout.visibility = View.INVISIBLE
        })
        mBinding.lockOnKey.setOnClickListener(View.OnClickListener {
            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x05) }).start()
            Util.status_log("LOCK")
            key = 5
        })

        mBinding.status.setOnLongClickListener(View.OnLongClickListener{
            Util.status_log("DISCONNECT")
            disconnect()
            return@OnLongClickListener true
        })
        mBinding.connectKey.setOnClickListener(View.OnClickListener {
            if (!is_running) {
                if (AppData.Connect) {
                    Util.status_log("DISCONNECT")
                    disconnect()
                } else {
                    replaceContentSubFragment(BluetoothFragment())
                }
            }
        })

        hide_object()
    }

    override fun onResume() {
        super.onResume()

        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (BluetoothDevice.ACTION_FOUND == intent.action) {
                    var device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    for(str in AppData.Bluetooth.pairing_list) {
                        if (str == device) {
                            return
                        }
                    }
                    AppData.Bluetooth.bluetooth_list.add(device!!)
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST) // 복수로 등록가능
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND) // 복수로 등록가능
        registerReceiver(broadcastReceiver, IntentFilter(intentFilter))
    }

    override fun onBackPressed() {
        if (sub_layout_active) {
            if (mFragmentManager.backStackEntryCount > 0) {
                mFragmentManager.popBackStack()
            }else {
                sub_layout_active = false
                mBinding.subFragment.visibility = View.GONE
            }
        }
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

    var down_thread:DownThread? = null
    var up_thread:UpThread? = null
    var is_clicking:Boolean = false
    inner class DownThread: Thread() {
        override fun run() {
            super.run()
            var clicking_time:Long = 500

            try {
                while(is_clicking){
                    Handler(Looper.getMainLooper()).post {
                        if(current_stage>1){
                            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x04) }).start()
                            Util.status_log("DOWN")
                            key = 4
                        }
                    }
                    sleep(clicking_time)
                }
            }catch (e: Exception) {
            }
        }
    }

    inner class UpThread: Thread() {
        override fun run() {
            super.run()
            var clicking_time: Long = 500
            try {
                while (is_clicking) {
                    Handler(Looper.getMainLooper()).post {
                        if(current_stage<20){
                            Thread(Runnable { mMINI110CANPacket?.send_Remote_Key(0x03) }).start()
                            Util.status_log("UP")
                            key = 3
                        }
                    }
                    sleep(clicking_time)
                }
            } catch (e: Exception) {
            }
        }
    }
}
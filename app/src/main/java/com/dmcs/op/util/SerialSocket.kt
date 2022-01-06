package com.dmcs.op.util

import android.app.Activity
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.dmcs.op.AppData
import com.dmcs.op.NewMiniActivity
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class SerialSocket(private val activity: Activity) : SerialInputOutputManager.Listener {
    private var deviceId = 0
    private var portNum = 0
    private val withIoManager = true
    private var usbIoManager: SerialInputOutputManager? = null
    private var usbSerialPort: UsbSerialPort? = null
    private var mHandler:Handler? = null
    private val mainLooper: Handler
    var receive_start: Long = 0
    var parser_start: Long = 0
    var fail_count: Int = 0
    var send_start: Long = 0
    var Wait_time = 3000
    var baudRate = 19200

    override fun onNewData(data: ByteArray) {
        val sb = StringBuilder()
        for (b in data) {
            AppData.Main.arrQueue.offer(b)
            sb.append(String.format("%02x ", b))
        }
        Util.status("[" + sb.toString() + "=========" + (System.currentTimeMillis() - receive_start) + "ms Receive]")
        receive_start = System.currentTimeMillis()
    }

    override fun onRunError(e: Exception) {
        Util.status("connection lost: " + e.message)
        disconnect()
    }


    // 디바이스 = 포트 연결
    fun connect(type: Int): Boolean {
        if (!AppData.Connect) {
            if (type == 0) {
                if (connect_usb()) {
                    Util.status("USB 연결 성공")
                    socket_type = type
                    return true
                }
            } else if (type == 1) {
                AppData.Usb_Detached = true
                if (connect_bluetooth()) {
                    Util.status("블루투스 연결 성공")
                    socket_type = type
                    return true
                }
            }
        }
        return false
    }

    var isUsbPermission: Int = 0

    fun connect_usb(): Boolean {
        if (!AppData.Connect) {
            var device: UsbDevice? = null
            val usbManager =
                activity.getSystemService(Context.USB_SERVICE) as UsbManager
            val usbDefaultProber = UsbSerialProber.getDefaultProber()
            for (d in usbManager.deviceList.values) {
                val driver = usbDefaultProber.probeDevice(d)
                if (driver != null) {
                    for (port in driver.ports.indices) {
                        portNum = port
                        deviceId = d.deviceId
                    }
                }
            }
            for (v in usbManager.deviceList.values) if (v.deviceId == deviceId) device =
                v
            if (device == null) {
                Util.status("connection failed: device not found")
                return false
            }
            val driver = UsbSerialProber.getDefaultProber().probeDevice(device)
            if (driver == null) {
                Util.status("connection failed: no driver for device")
                return false
            }
            if (driver.ports.size < portNum) {
                Util.status("connection failed: not enough ports at device")
                return false
            }
            usbSerialPort = driver.ports[portNum]
            val usbConnection = usbManager.openDevice(driver.device)


            if (!usbManager.hasPermission(driver.device) && isUsbPermission==0) {
                isUsbPermission = -1
                val usbPermissionIntent = PendingIntent.getBroadcast(activity, 0, Intent(AppData.Main.INTENT_ACTION_GRANT_USB), 0)
                usbManager.requestPermission(driver.device, usbPermissionIntent)
                return false
            } else {
                isUsbPermission = 1
            }

            if (usbConnection == null) {
                if (!usbManager.hasPermission(driver.device)) {
                    Util.status("connection failed: permission denied")
                } else {
                    Util.status("connection failed: open failed")
                }
                return false
            }

            try {
                usbSerialPort?.open(usbConnection)
                usbSerialPort?.setParameters(
                    baudRate,
                    8,
                    UsbSerialPort.STOPBITS_1,
                    UsbSerialPort.PARITY_NONE
                )
                usbSerialPort?.setDTR(true)
                usbSerialPort?.setRTS(true)
                // Loop설정(싱글스레드)
                if (withIoManager) {
                    usbIoManager = SerialInputOutputManager(usbSerialPort, this)
                    Executors.newSingleThreadExecutor().submit(usbIoManager)
                }
                Util.status("connected")
                return true
            } catch (e: Exception) {
                Util.status("connection failed: " + e.message)
            }
        }
        return false
    }

    fun connect_bluetooth(): Boolean {
        try {
            Util.status("찾는중...")
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter == null) {
                Util.status("No bluetooth adapter available")
            }

            if (!mBluetoothAdapter!!.isEnabled()) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                activity.startActivityForResult(enableBluetooth, 0)
            }

            if (AppData.Bluetooth.mRemoteDevice != null) {
                openBT()
                return true
            }
            Util.status("connection failed: device not found")
        } catch (e: Exception) {
            Util.status("connection failed: " + e.message)
        }
        return false
    }

    @Throws(IOException::class)
    fun openBT() {
        Util.status("여는중...")
        mSocket =createBluetoothSocket(AppData.Bluetooth.mRemoteDevice)
        mSocket!!.connect()
        beginListenForData()
        Util.status("Success")
    }

    fun beginListenForData() {
        stopWorker = false
        readBufferPosition = 0
        workerThread = Thread(Runnable {
            try {
                var len = 0
                val packetBytes = ByteArray(1024)

                while (!Thread.currentThread().isInterrupted && !stopWorker) {
                    len = mSocket!!.inputStream!!.available()
                    if (len > 0) {
                        len = mSocket!!.inputStream!!.read(packetBytes, 0, len)
                        for (i in 0 until len) {
                            AppData.Main.arrQueue.offer(packetBytes[i])
                        }
                    }
                    Thread.sleep(10)
                }
            } catch (e: java.lang.Exception) {
                stopWorker = true
            }
            detached()
        })
        workerThread!!.isDaemon = true
        workerThread!!.start()
    }

    //연결 해제
    fun detached() {
        if(AppData.Connect){
            Util.status("연결 종료")
            AppData.Connect = false
            stopWorker = true
            if (usbIoManager != null) usbIoManager!!.stop()
            usbIoManager = null
            try {
                usbSerialPort!!.close()
            } catch (ignored: Exception) {
            }
            usbSerialPort = null
            mBluetoothAdapter = null

            try {
                mSocket!!.close()
            } catch (ignored: Exception) {
            }
            mSocket = null

            serial_clear()


        }
    }

    //통신 해제
    fun disconnect() {
        if(AppData.Connect) {
            Util.status("통신 종료")
            if (usbIoManager != null) usbIoManager!!.stop()
            usbIoManager = null
            try {
                usbSerialPort!!.close()
            } catch (ignored: Exception) {
            }
            usbSerialPort = null

            AppData.Connect = false
            NewMiniActivity.stopWorker = true
            serial_clear()
        }
    }

    fun serial_clear(){
        AppData.ID_NUMBER = 0
        AppData.ID_NUMBER_MINI250_FLAG = false

        AppData.Main.DeviceName = ""
        AppData.Connect_Type = -1

        AppData.Mini.mini_can_driverf = 0
        AppData.Mini.drivetypef = 0
        AppData.Mini.stackerf = 0
        AppData.Mini.agvf = 0
        AppData.Mini.evecf = 0
        AppData.Mini.mpsf = 0
        AppData.Mini.sleepf = 0

        AppData.Main.arrQueue.clear()
    }

    var parser_count = 0

    @Synchronized
    fun PacketSend(vararg i_array: Byte, limit: Int, is_receive: Boolean): ByteArray? {
        if(!is_receive){
            READ_WAIT_MILLIS = 10
        }else{
            READ_WAIT_MILLIS = 500
        }

        for(i in 0 until limit) {
            var receive = Send(*i_array)
            if (receive != null) {
                AppData.disconnect_time = System.currentTimeMillis()
                return receive
            }
        }

        if(is_receive) {
            AppData.disconnect_flag = true
            if(!AppData.Usb_Detached){
                disconnect()
            }else {
                detached()
            }
        }
        return null
    }

    fun Send(vararg i_array: Byte): ByteArray? {
        try {
            var crc16:CRC16Modbus = CRC16Modbus()
            crc16.update(i_array)
            var send_array :ByteArray = ByteArray(i_array.size + 2)
            for(i in 0 until i_array.size){
                send_array[i] = i_array[i]
            }
            send_array[i_array.size] = crc16.crcBytes.get(0)
            send_array[i_array.size + 1] = crc16.crcBytes.get(1)

            if (socket_type == 0) {
                usbSerialPort!!.write(send_array, WRITE_WAIT_MILLIS)
            } else if (socket_type == 1) {
                mSocket!!.outputStream!!.write(send_array)
                mSocket!!.outputStream!!.flush()
            }

            val sb = StringBuilder()
            for (b in send_array) sb.append(String.format("%02x ", b))
            Util.status("TX : [" + sb.toString() + "=" + (System.currentTimeMillis() - send_start) + "ms]")
            send_start = System.currentTimeMillis()

            parser_start = System.currentTimeMillis()
            var receive:ByteArray? = Parser(i_array[0], READ_WAIT_MILLIS)
            if(receive===null){
                return null
            }else{
                return receive
            }
        } catch (e: Exception) {
            onRunError(e)
        }
        return null
    }

    fun Parser(id: Byte, time: Int): ByteArray? {
        var parser_sequence = 0
        var len = 0
        var data_cnt = 0
        var crc_cnt = 0
        var Rxcrc16_2 = ""
        var tmp_array_copy = ByteArray(128) //초기값 0

        val sb = StringBuilder()
        var start_time = System.currentTimeMillis()

        while (true) {
            val arrByte: Byte? = AppData.Main.arrQueue.poll()
            if(arrByte!=null){
                start_time = System.currentTimeMillis()
                // Addr 확인(80, 81)
                if (parser_sequence == 0) {
                    if (arrByte == id) {
                        tmp_array_copy.set(0, arrByte)
                        sb.append(String.format("%02x ", arrByte))
                        parser_sequence = 1
                    }
                } else if (parser_sequence == 1) {
                    tmp_array_copy.set(1, arrByte)
                    sb.append(String.format("%02x ", arrByte))
                    parser_sequence = 2
                } else if (parser_sequence == 2) {
                    if(arrByte>0 && arrByte <128){
                        len = arrByte.toInt()
                        tmp_array_copy.set(2, arrByte)
                        sb.append(String.format("%02x ", arrByte))
                        data_cnt=0
                        parser_sequence = 3
                    }
                } else if (parser_sequence == 3) {
                    tmp_array_copy.set(data_cnt + 3, arrByte)
                    sb.append(String.format("%02x ", arrByte))
                    data_cnt++

                    if (data_cnt == len) {
                        parser_sequence = 4
                        crc_cnt=0
                        Rxcrc16_2 = ""
                    }
                    // crc 확인
                } else if (parser_sequence == 4) {
                    Rxcrc16_2 += String.format("%02x ", arrByte)
                    sb.append(String.format("%02x ", arrByte))
                    crc_cnt++
                    if (crc_cnt == 2) {
                        var crc16:CRC16Modbus = CRC16Modbus()
                        crc16.update(tmp_array_copy, 0, len + 3)
                        var Rxcrc16 = String.format("%02x ", crc16.crcBytes.get(0)) + String.format("%02x ", crc16.crcBytes.get(1))
                        if (Rxcrc16_2 == Rxcrc16) {
                            parser_count = 0
                            Util.status("RX : [" + sb.toString() + "=========" + (System.currentTimeMillis() - parser_start) + "ms]")
                            return tmp_array_copy
                        }
                    }
                }
            }

            // 시간 초과
            if (System.currentTimeMillis() - start_time >= time) {
                Util.status_e("Rx실패 : [" + parser_sequence + "] [" + sb.toString() + "=========" + (System.currentTimeMillis() - start_time) + "ms]")
                break
            }
        }
        return null
    }

    companion object {
        private const val WRITE_WAIT_MILLIS = 2000
        private var READ_WAIT_MILLIS = 500
        var is_start = false
        var is_result = false
        var is_receive = false
        var socket_type = 0
        var mSocket: BluetoothSocket? = null
        var workerThread: Thread? = null
        var readBufferPosition = 0

        @Volatile
        var stopWorker = false
        var mBluetoothAdapter: BluetoothAdapter? = null
        var uuid =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") //Standard SerialPortService ID

        @Throws(IOException::class)
        fun createBluetoothSocket(device: BluetoothDevice?): BluetoothSocket {
            if (Build.VERSION.SDK_INT >= 10) {
                try {
                    val m = device?.javaClass?.getMethod(
                        "createInsecureRfcommSocketToServiceRecord", *arrayOf<Class<*>>(
                            UUID::class.java
                        )
                    )
                    return m?.invoke(device, uuid) as BluetoothSocket
                } catch (e: Exception) {
                }
            }
            return device!!.createRfcommSocketToServiceRecord(uuid)
        }

    }

    init {
        mainLooper = Handler(Looper.getMainLooper())
    }
}
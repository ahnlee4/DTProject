package com.dmcs.op.util

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.dmcs.op.AppData
import java.io.*
import java.net.Socket
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WifiUtil {
    private var isConnected = false
    private var mServerIP: String? = null
    private var mSocket: Socket? = null
    var mOut: OutputStream? = null
    var mIn: InputStream? = null
    private var mReceiverThread: Thread? = null

    fun ConnectThread(serverIP: String, serverPort: Int) : Boolean {
        try {
            mSocket = Socket(serverIP, serverPort)
            //ReceiverThread: java.net.SocketTimeoutException: Read timed out 때문에 주석처리
            //mSocket.setSoTimeout(3000);
            mServerIP = mSocket!!.remoteSocketAddress.toString()
        } catch (e: UnknownHostException) {
            Log.d(TAG, "ConnectThread: can't find host")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "ConnectThread: timeout")
        } catch (e: Exception) {
            Log.e(TAG, "ConnectThread:" + e.message)
        }
        if (mSocket != null) {
            try {
                mOut = mSocket!!.getOutputStream()
                mIn = mSocket!!.getInputStream()
                isConnected = true

                Thread(Runnable {
                    if (isConnected) {
                        Log.d(TAG, "connected to $serverIP")
                        mReceiverThread = Thread(ReceiverThread())
                        mReceiverThread!!.start()
                    } else {
                        Log.d(TAG, "failed to connect to server $serverIP")
                    }
                }).start()
            } catch (e: IOException) {
                Log.e(TAG, "ConnectThread:" + e.message)
            }
        }else{
            return false
        }
        return true
    }

    private inner class ReceiverThread : Runnable {
        override fun run() {
            try {
                var len = 0
                val packetBytes = ByteArray(256)

                while (isConnected) {
                    if (mIn == null) {
                        Log.d(TAG, "ReceiverThread: mIn is null")
                        break
                    }

                    len = mIn!!.available()
                    if (len > 0) {
                        len = mIn!!.read(packetBytes, 0, len)
                        for (i in 0 until len) {
                            AppData.Main.arrQueue.offer(packetBytes[i])
                        }
                    }
                }

                Log.d(TAG, "ReceiverThread: thread has exited")
                if (mOut != null) {
                    mOut!!.flush()
                    mOut!!.close()
                }
                mIn = null
                mOut = null
                if (mSocket != null) {
                    try {
                        mSocket!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "ReceiverThread: $e")
            }
        }
    }

    companion object {
        private const val TAG = "TcpClient"
    }
}
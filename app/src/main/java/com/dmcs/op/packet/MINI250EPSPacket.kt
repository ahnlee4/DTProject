package com.dmcs.op.packet

import com.dmcs.op.AppData
import com.dmcs.op.util.SerialSocket

class MINI250EPSPacket(private val serialSocket: SerialSocket) {

    fun send_250_Version_Read(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.Protocol.MINI250_CON_ID
        i_array[1] = 0x50.toByte()
        i_array[2] = 9
        i_array[3] = ((0x30+AppData.admin_flag).toByte())
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 2, is_receive = true)
    }

    fun send_EPS_Version_Read(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.Protocol.EPSRB20_CON_ID
        i_array[1] = 0x50.toByte()
        i_array[2] = 13;
        i_array[3] = ((0x30+AppData.admin_flag).toByte())
        i_array[4] = 0;
        i_array[5] = 0;
        return serialSocket.PacketSend(*i_array, limit = 2, is_receive = true)
    }


    fun send_Select_Key(key:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0xa0.toByte()
        if(AppData.NewMini.serial_errorf){
            i_array[2] = 0x45.toByte()
        }else{
            i_array[2] = 0x4f.toByte()
        }
        i_array[3] = 0
        i_array[4] = (key and 0xFF).toByte()
        i_array[5] = ((key shr 8) and 0xFF).toByte()
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }


    fun send_Connection(key:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0xb0.toByte()
        if(AppData.NewMini.serial_errorf){
            i_array[2] = 0x45.toByte()
        }else{
            i_array[2] = 0x4f.toByte()
        }
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Memory_Read(len: Byte, key:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0xb0.toByte()
        var data = 0
        if(len<201){
            i_array[2] = 0x4f.toByte()
            data = key
        }else{
            i_array[2] = 0x45.toByte()
            data = 0
        }
        i_array[3] = len
        i_array[4] = (data and 0xFF).toByte()
        i_array[5] = ((data shr 8) and 0xFF).toByte()
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }
}
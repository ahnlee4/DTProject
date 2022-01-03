package com.dmcs.op.packet

import com.dmcs.op.AppData
import com.dmcs.op.util.SerialSocket

class MINI110CANPacket(private val serialSocket: SerialSocket) {
    var TYPE_SAFETY_TIME:Byte = 0x27
    var TYPE_SWITCH3:Byte = 0x45
    var TYPE_BAUDRATE:Byte = 0x48

    fun send_Remote_Start(): ByteArray? {
        val i_array = ByteArray(3)
        i_array[0] = 0xDE.toByte()
        i_array[1] = 0x01.toByte()
        i_array[2] = 0x96.toByte()
        return serialSocket.PacketSend(*i_array, limit = 1, is_receive = false)
    }

    fun send_Remote_Key(key:Int): ByteArray? {
        val i_array = ByteArray(3)
        i_array[0] = 0xDE.toByte()
        i_array[1] = 0x01.toByte()
        i_array[2] = key.toByte()
        return serialSocket.PacketSend(*i_array, limit = 1, is_receive = false)
    }

    fun send_Version_Read(ID_NUMBER: Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 9
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_New_Version(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 11
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Battery_Type_Read(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 10
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Drive_Type(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 13
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Current_Type(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 14
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Parameter_Total_Count(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x74.toByte()
        i_array[2] = 0
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Parameter_safety_off_time(byte: Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x75.toByte()
        i_array[2] = byte
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Error_code(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 1
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Driver_Write(byte: Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x7b.toByte()
        i_array[2] = byte
        if(AppData.admin_flag==1)
            i_array[3] = 0x55.toByte()
        else
            i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    //디지털(Digital) 관련
    fun send_Digital_Switch_Read(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 5
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Digital_Switch_Type_Read(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 0x90.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Digital_Switch_Write(data:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = 0x91.toByte()
        i_array[3] = (data and 0xFF).toByte()
        i_array[4] = ((data shr 8) and 0xFF).toByte()
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    //아날로그(Analog) 관련
    fun send_Analog_Speed_Limit(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x75.toByte()
        i_array[2] = 37.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Analog_Data(type:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = type.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Analog_Para_Read(type:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = (0x60+type).toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Analog_Para_Write(type:Int, data:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x50.toByte()
        i_array[2] = (0x70+type).toByte()
        i_array[3] = (data and 0xFF).toByte()
        i_array[4] = ((data shr 8) and 0xFF).toByte()
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Analog_ALL_Data(max:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x51.toByte()
        i_array[2] = 0x02.toByte()
        i_array[3] = 0x00.toByte()
        i_array[4] = max.toByte()
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    //오류 기록(ErrorHistory) 관련
    fun send_Error_Num_Read(num:Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = num
        if(AppData.ID_NUMBER_MINI250_FLAG){
            i_array[2] = 19
        }else{
            i_array[2] = 14
        }
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Error_Time_High_Read(num:Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = num
        i_array[2] = 13
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Error_Time_Low_Read(num:Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = num
        i_array[2] = 12
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }


    fun send_Error_Read(num:Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = num
        i_array[2] = 11
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Error_Read(num:Byte, option:Byte): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = num
        i_array[2] = option
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Error_Delete(num:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = num.toByte()
        i_array[2] = 99
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    // 파라메터 조회/수정(Parameter Confirm) 관련
    fun send_Param_Accel_Type(): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x75.toByte()
        i_array[2] = 32.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Param_Read(num:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x75.toByte()
        i_array[2] = num.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Param_Min_Read(num:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x76.toByte()
        i_array[2] = num.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Param_Max_Read(num:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x77.toByte()
        i_array[2] = num.toByte()
        i_array[3] = 0
        i_array[4] = 0
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }

    fun send_Param_Write(num:Int, data:Int): ByteArray? {
        val i_array = ByteArray(6)
        i_array[0] = AppData.ID_NUMBER
        i_array[1] = 0x7a.toByte()
        i_array[2] = num.toByte()
        i_array[3] = (data and 0xFF).toByte()
        i_array[4] = ((data shr 8) and 0xFF).toByte()
        i_array[5] = 0
        return serialSocket.PacketSend(*i_array, limit = 5, is_receive = true)
    }
}
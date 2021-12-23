package dmcs.tan

import com.dmcs.op.AppData
import com.dmcs.op.util.ParamItem
import com.dmcs.op.util.Util
import kotlin.math.roundToInt

class ParamUtil {
    enum class ValueFormat {
        None, Zero_Decimal, One_Decimal, Two_Decimal, Three_Decimal, Hex, Zero_OFF, List_Choice
    }

    enum class SignFormat {
        None, Volt, Ampere, Millimetre, Centimeter, Meter, Second, Minute, Hertz, Celsius, Percent, RPM, PPR, Kbps, Inch, OFF
    }

    var vf = ValueFormat.None
    var sf = SignFormat.None

    var data_type = 0
    var value_type = 0 //0: 숫자, 1: 문자(선택형) 2:문자(특수선택형)
    var str_val_list: Array<String>? = null

    var code_58_list = arrayOf<String>("125", "250", "500", "1000")
    var code_61_list = arrayOf<String>("Pb", "Li")
    var code_71_list = arrayOf<String>("OFF", "ON")
    var code_72_list = arrayOf<String>("OFF", "Interlock", "Neutral", "Interlock+Neutral")
    var code_73_list = arrayOf<String>("OFF", "Backward", "Forward+Backward", "Forward")
    var code_74_list = arrayOf<String>("Traction", "Pump")
    var code_75_list = arrayOf<String>("One Time", "Continue")
    var code_76_list = arrayOf<String>("Zero Start", "Full Start")
    var code_77_list = arrayOf<String>("Left Position", "Right Position")
    var code_78_list = arrayOf<String>("Single", "Master", "Slave Single", "Slave Multi")
    var code_78_2_list = arrayOf<String>("Single", "Master", "Slave")
    var code_79_list = arrayOf("OFF")
    var code_81_list = arrayOf<String>("Accel", "Speed")
    var code_82_list = arrayOf<String>("OFF", "Restraint", "BDI", "Restraint+BDI")
    var code_83_list = arrayOf<String>("OFF", "Stop", "Continue")
    var code_84_list = arrayOf<String>("OFF", "Brake+Obstacle", "Accel Limit", "Auxiliary Accel")
    var code_85_list = arrayOf<String>("Manual", "Auto")
    var code_86_list = arrayOf<String>("Speed", "Torque")
    var code_87_list = arrayOf<String>("Release", "Bumper")
    var code_88_list = arrayOf<String>("OFF", "Brake", "Limit")
    var code_89_list = arrayOf<String>("Digital", "Analog")
    var code_91_list = arrayOf<String>("OFF", "Emergency", "Obstacle")
    var code_92_list = arrayOf<String>("Low Active Input", "High Active Input")
    var code_93_list = arrayOf<String>("OFF", "Standard", "Extended")
    var code_94_list = arrayOf<String>("125", "250", "500", "1000")
    var code_95_list = arrayOf<String>("OFF", "Run(Accel,Pedal)", "Obstacle")

    var code_mini_14_list = arrayOf<String>("OFF", "ON", "BDI", "OFF&BDI")
    var code_mini_15_list = arrayOf<String>("OFF", "ON", "AUTO")
    var code_mini_18_list = arrayOf<String>("OFF", "ON")
    var code_mini_22_list = arrayOf<String>("OFF", "ON")
    var code_mini_25_list = arrayOf<String>("OFF", "ON")
    var code_mini_40_list = arrayOf<String>("OFF", "ON")
    var code_mini_32_list = arrayOf<String>("Normal", "Center0", "Center1", "Inverted")
    var code_mini_33_list = arrayOf<String>("Error", "OFF", "Brake", "Neutral")
    var code_mini_37_list = arrayOf<String>("OFF", "Normal", "Pedal", "CAN")
    var code_mini_45_list = arrayOf<String>("Brake", "T-Rorate")
    var code_mini_48_list = arrayOf<String>("OFF", "125", "250", "500", "1000")

    var code_mini250_14_list = arrayOf<String>("OFF", "ON", "BDI", "OFF&BDI")
    var code_mini250_15_list = arrayOf<String>("OFF", "ON", "AUTO")
    var code_mini250_18_list = arrayOf<String>("OFF", "ON")
    var code_mini250_22_list = arrayOf<String>("OFF", "ON")
    var code_mini250_25_list = arrayOf<String>("OFF", "ON", "Slow")
    var code_mini250_32_list = arrayOf<String>("Normal", "Center0", "Center1", "Inverted")
    var code_mini250_33_list = arrayOf<String>("Error", "OFF", "Brake", "Neutral")
    var code_mini250_37_list = arrayOf<String>("OFF", "Normal", "Pedal", "Center1", "R-Turn", "L-Turn")
    var code_mini250_41_list = arrayOf<String>("Brake", "T-Rorate")

    var code_mini_dp_67_list = arrayOf<String>("OFF", "ON")
    var code_mini_dp_68_74_list = arrayOf<String>("OFF", "ON", "AUTO")
    var code_mini_dp_75_list = arrayOf<String>("OFF", "ON")
    var code_mini_dp_76_list = arrayOf<String>("OFF", "LOW", "High")

    fun code_analysis(param:ParamItem) {
        val start = param.unit / 10
        val end = param.unit % 10
        vf = ValueFormat.None
        sf = SignFormat.None
        str_val_list = null

        when (start) {
            1 -> when (end) {
                1 -> vf = ValueFormat.Zero_Decimal
                2 -> vf = ValueFormat.One_Decimal
                3 -> vf = ValueFormat.Two_Decimal
                4 -> vf = ValueFormat.Three_Decimal
            }
            2 -> {
                sf = SignFormat.Volt
                when (end) {
                    1 -> vf = ValueFormat.Zero_Decimal
                    2 -> vf = ValueFormat.One_Decimal
                    3 -> vf = ValueFormat.Two_Decimal
                    4 -> vf = ValueFormat.Three_Decimal
                }
            }
            3 -> when (end) {
                1 -> {
                    sf = SignFormat.Ampere
                    vf = ValueFormat.Zero_Decimal
                }
                5 -> {
                    sf = SignFormat.Millimetre
                    vf = ValueFormat.Zero_Decimal
                }
                6 -> {
                    sf = SignFormat.Centimeter
                    vf = ValueFormat.One_Decimal
                }
                7 -> {
                    sf = SignFormat.Meter
                    vf = ValueFormat.One_Decimal
                }
                8 -> {
                    sf = SignFormat.Meter
                    vf = ValueFormat.Two_Decimal
                }
            }
            4 -> when (end) {
                1 -> {
                    sf = SignFormat.Second
                    vf = ValueFormat.Zero_Decimal
                }
                2 -> {
                    sf = SignFormat.Second
                    vf = ValueFormat.One_Decimal
                }
                3 -> {
                    sf = SignFormat.Second
                    vf = ValueFormat.Two_Decimal
                }
                4 -> {
                    sf = SignFormat.Second
                    vf = ValueFormat.Three_Decimal
                }
                5 -> {
                    sf = SignFormat.Minute
                    vf = ValueFormat.Zero_Decimal
                }
            }
            5 -> when (end) {
                1 -> {
                    sf = SignFormat.Hertz
                    vf = ValueFormat.Zero_Decimal
                }
                2 -> {
                    sf = SignFormat.Celsius
                    vf = ValueFormat.Zero_Decimal
                }
                3 -> {
                    sf = SignFormat.Percent
                    vf = ValueFormat.Zero_Decimal
                }
                4 -> {
                    sf = SignFormat.RPM
                    vf = ValueFormat.Zero_Decimal
                }
                5 -> {
                    sf = SignFormat.Percent
                    vf = ValueFormat.One_Decimal
                }
                6 -> {
                    sf = SignFormat.PPR
                    vf = ValueFormat.Zero_Decimal
                }
                7 -> {
                    sf = SignFormat.Hertz
                    vf = ValueFormat.One_Decimal
                }
                8 -> {
                    if (param.step == 125 || param.step == 1) {
                        vf = ValueFormat.List_Choice
                        str_val_list = code_58_list
                    } else {
                        vf = ValueFormat.Zero_Decimal
                    }
                }
                9 -> {
                    sf = SignFormat.Percent
                    vf = ValueFormat.Two_Decimal
                }
            }
            6 -> when (end) {
                1 -> str_val_list = code_61_list
                2 -> vf = ValueFormat.Hex
                5 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Inch
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Inch
                }
            }
            7 -> when (end) {
                1 -> {
                    str_val_list = code_71_list
                    data_type = 1
                }
                2 -> str_val_list = code_72_list
                3 -> str_val_list = code_73_list
                4 -> str_val_list = code_74_list
                5 -> str_val_list = code_75_list
                6 -> str_val_list = code_76_list
                7 -> str_val_list = code_77_list
                8 -> {
                    str_val_list = if (param.max == 3) {
                        code_78_list
                    } else {
                        code_78_2_list
                    }
                }
                9 -> vf = ValueFormat.Zero_OFF
            }
            8 -> when (end) {
                1 -> str_val_list = code_81_list
                2 -> str_val_list = code_82_list
                3 -> str_val_list = code_83_list
                4 -> str_val_list = code_84_list
                5 -> str_val_list = code_85_list
                6 -> str_val_list = code_86_list
                7 -> str_val_list = code_87_list
                8 -> str_val_list = code_88_list
                9 -> str_val_list = code_89_list
            }
            9 -> when (end) {
                1 -> str_val_list = code_91_list
                2 -> {
                    str_val_list = code_92_list
                    data_type = 1
                }
                3 -> str_val_list = code_93_list
                4 -> str_val_list = code_94_list
                5 -> str_val_list = code_95_list
            }
        }
        value_type = if (vf == ValueFormat.None) 1 else if (vf == ValueFormat.List_Choice) 2 else if (vf == ValueFormat.Hex) 3 else 0

        var param_list: Array<String>? = null
        if (value_type == 0 && param.name!!.indexOf(",") != -1) {
            param_list = param.name!!.split(",".toRegex()).toTypedArray()
            if (param_list.size == param.max + 1) {
                for (i in param_list.indices) {
                    param_list[i] = param_list[i].substring(param_list[i].indexOf("=") + 1).trim { it <= ' ' }
                }
                str_val_list = param_list
                value_type = 1
            }
        }
    }

    fun code_analysis_mini(param:ParamItem) {
        val start = param.unit / 10
        val end = param.unit % 10
        vf = ValueFormat.None
        sf = SignFormat.None
        str_val_list = null

        when (start) {
            0 -> when (end) {
                0 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                1 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                2 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                3 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                4 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                5 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                6 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                7 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                8 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                9 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
            }
            1 -> when (end) {
                0 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                2 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                3 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                4 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_14_list
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_15_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                8 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_18_list
                }
                9 -> {
                    vf = ValueFormat.Two_Decimal
                    sf = SignFormat.Volt
                }
            }
            2 -> when (end) {
                0 -> {
                    vf = ValueFormat.Two_Decimal
                    sf = SignFormat.Volt
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_22_list
                }
                3 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                4 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Ampere
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_25_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Minute
                }
                8 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
            3 -> when (end) {
                0 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Ampere
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_32_list
                }
                3 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_33_list
                }
                4 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                5 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                7 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_37_list
                }
                8 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
            4 -> when (end) {
                0 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_40_list
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                2 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                3 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                4 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_45_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                8 -> {
                    if (param.step == 125) {
                        vf = ValueFormat.List_Choice
                        sf = SignFormat.None
                        str_val_list = code_mini_48_list
                    } else {
                        vf = ValueFormat.Zero_Decimal
                        sf = SignFormat.None
                    }
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
            }
            5 -> when (end) {
                0 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                1 -> {
                    vf = ValueFormat.Three_Decimal
                    sf = SignFormat.Second
                }
            }
        }
        value_type = if (vf == ValueFormat.None) 1 else if (vf == ValueFormat.List_Choice) 2 else if (vf == ValueFormat.Hex) 3 else 0

        if(str_val_list!=null){
            if((param.max+1)-str_val_list!!.size!=0){
                val len = (param.max+1)-str_val_list!!.size
                var temp = str_val_list!!.toMutableList()
                for(i in 0 until len){
                    temp.add("New " + (str_val_list!!.size+i))
                }
                str_val_list = temp.toTypedArray()
            }
        }
    }

    fun code_analysis_mini_dt(param:ParamItem) {
        val start = param.unit / 10
        val end = param.unit % 10
        vf = ValueFormat.None
        sf = SignFormat.None
        str_val_list = null

        when (start) {
            0 -> when (end) {
                0 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                1 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                2 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                3 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                4 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                5 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                6 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                7 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                8 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                9 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
            }
            1 -> when (end) {
                0 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                2 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                3 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                4 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_14_list
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_15_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    if(AppData.Mini.agvf==1){
                        vf = ValueFormat.Two_Decimal
                        sf = SignFormat.Second
                    }else{
                        vf = ValueFormat.One_Decimal
                        sf = SignFormat.Second
                    }
                }
                8 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_18_list
                }
                9 -> {
                    vf = ValueFormat.Two_Decimal
                    sf = SignFormat.Volt
                }
            }
            2 -> when (end) {
                0 -> {
                    vf = ValueFormat.Two_Decimal
                    sf = SignFormat.Volt
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_22_list
                }
                3 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                4 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Ampere
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_25_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Minute
                }
                8 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
            3 -> when (end) {
                0 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Ampere
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_32_list
                }
                3 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_33_list
                }
                4 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                5 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                7 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_37_list
                }
                8 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
            4 -> when (end) {
                0 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_40_list
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                2 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                3 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                4 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                5 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                8 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
            5 -> when (end) {
                0 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                1 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                2 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                3 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                4 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                5 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                7 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                8 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                9 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
            }
            6 -> when (end) {
                0 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                1 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                2 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                3 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                4 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                5 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                7 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_67_list
                }
                8 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
                9 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
            }
            7 -> when (end) {
                0 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
                1 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
                3 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
                4 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_68_74_list
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_75_list
                }
                6 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini_dp_76_list
                }
                7 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                else -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
            }
            else -> {
                vf = ValueFormat.Zero_Decimal
                sf = SignFormat.None
            }
        }
        value_type = if (vf == ValueFormat.None) 1 else if (vf == ValueFormat.List_Choice) 2 else if (vf == ValueFormat.Hex) 3 else 0


        if(str_val_list!=null){
            if((param.max+1)-str_val_list!!.size!=0){
                val len = (param.max+1)-str_val_list!!.size
                var temp = str_val_list!!.toMutableList()
                for(i in 0 until len){
                    temp.add("New " + (str_val_list!!.size+i))
                }
                str_val_list = temp.toTypedArray()
            }
        }
    }

    fun code_analysis_mini250(param:ParamItem) {
        val start = param.unit / 10
        val end = param.unit % 10
        vf = ValueFormat.None
        sf = SignFormat.None
        str_val_list = null

        when (start) {
            0 -> when (end) {
                0 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                1 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                2 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                3 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                4 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                5 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                7 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                8 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                9 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
            }
            1 -> when (end) {
                0 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                2 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                3 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                4 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_14_list
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_15_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                8 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_18_list
                }
                9 -> {
                    vf = ValueFormat.Two_Decimal
                    sf = SignFormat.Volt
                }
            }
            2 -> when (end) {
                0 -> {
                    vf = ValueFormat.Two_Decimal
                    sf = SignFormat.Volt
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_22_list
                }
                3 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                4 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Ampere
                }
                5 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_25_list
                }
                6 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                7 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Minute
                }
                8 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
            3 -> when (end) {
                0 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                1 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Ampere
                }
                2 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_32_list
                }
                3 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_33_list
                }
                4 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Second
                }
                5 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.None
                }
                6 -> {
                    vf = ValueFormat.One_Decimal
                    sf = SignFormat.Volt
                }
                7 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_37_list
                }
                8 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
                9 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Volt
                }
            }
            4 -> when (end) {
                0 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                1 -> {
                    vf = ValueFormat.None
                    sf = SignFormat.None
                    str_val_list = code_mini250_41_list
                }
                2 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                3 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
                4 -> {
                    vf = ValueFormat.Zero_Decimal
                    sf = SignFormat.Percent
                }
            }
        }
        value_type = if (vf == ValueFormat.None) 1 else if (vf == ValueFormat.List_Choice) 2 else if (vf == ValueFormat.Hex) 3 else 0


        if(str_val_list!=null){
            if((param.max+1)-str_val_list!!.size!=0){
                val len = (param.max+1)-str_val_list!!.size
                var temp = str_val_list!!.toMutableList()
                for(i in 0 until len){
                    temp.add("New " + (str_val_list!!.size+i))
                }
                str_val_list = temp.toTypedArray()
            }
        }
    }

    fun getValueFormat(unit: Int): ValueFormat {
        val start = unit / 10
        val end = unit % 10
        when (start) {
            1 -> when (end) {
                1 -> vf = ValueFormat.Zero_Decimal
                2 -> vf = ValueFormat.One_Decimal
                3 -> vf = ValueFormat.Two_Decimal
                4 -> vf = ValueFormat.Three_Decimal
            }
            2 -> when (end) {
                1 -> vf = ValueFormat.Zero_Decimal
                2 -> vf = ValueFormat.One_Decimal
                3 -> vf = ValueFormat.Two_Decimal
                4 -> vf = ValueFormat.Three_Decimal
            }
            3 -> when (end) {
                1 -> vf = ValueFormat.Zero_Decimal
                5 -> vf = ValueFormat.Zero_Decimal
                6 -> vf = ValueFormat.One_Decimal
                7 -> vf = ValueFormat.One_Decimal
                8 -> vf = ValueFormat.Two_Decimal
            }
            4 -> when (end) {
                1 -> vf = ValueFormat.Zero_Decimal
                2 -> vf = ValueFormat.One_Decimal
                3 -> vf = ValueFormat.Two_Decimal
                4 -> vf = ValueFormat.Three_Decimal
                5 -> vf = ValueFormat.Zero_Decimal
            }
            5 -> when (end) {
                1 -> vf = ValueFormat.Zero_Decimal
                2 -> vf = ValueFormat.Zero_Decimal
                3 -> vf = ValueFormat.Zero_Decimal
                4 -> vf = ValueFormat.Zero_Decimal
                5 -> vf = ValueFormat.One_Decimal
                6 -> vf = ValueFormat.Zero_Decimal
                7 -> vf = ValueFormat.One_Decimal
                8 -> vf = ValueFormat.List_Choice
                9 -> vf = ValueFormat.Two_Decimal
            }
            6 -> when (end) {
                1 -> {
                }
                2 -> vf = ValueFormat.Hex
                5 -> vf = ValueFormat.Zero_Decimal
                6 -> vf = ValueFormat.One_Decimal
            }
            7 -> when (end) {
                1 -> {
                }
                2 -> {
                }
                3 -> {
                }
                4 -> {
                }
                5 -> {
                }
                6 -> {
                }
                7 -> {
                }
                8 -> {
                }
                9 -> vf = ValueFormat.Zero_OFF
            }
            8 -> when (end) {
                1 -> {
                }
                2 -> {
                }
                3 -> {
                }
                4 -> {
                }
                5 -> {
                }
                6 -> {
                }
                7 -> {
                }
                8 -> {
                }
                9 -> {
                }
            }
            9 -> when (end) {
                1 -> {
                }
                2 -> {
                }
                3 -> {
                }
                4 -> {
                }
                5 -> {
                }
            }
        }
        return vf
    }

    var current_format = ""
    fun parameter_format_output(value:Int, sign_visible:Boolean): String {
        var reVal = ""
        if (vf == ValueFormat.None) reVal = String.format("%d", value) else if (vf == ValueFormat.Zero_OFF) {
            if (sign_visible) {
                reVal = if (value == 0) "OFF" else String.format("%d", value)
            } else {
                reVal = String.format("%d", value)
                current_format = "%.0f"
            }
        } else if (vf == ValueFormat.Zero_Decimal) {
            reVal = String.format("%d", value)
            current_format = "%.0f"
        } else if (vf == ValueFormat.One_Decimal) {
            reVal = String.format("%.1f", value * 0.1)
            current_format = "%.1f"
        } else if (vf == ValueFormat.Two_Decimal) {
            reVal = String.format("%.2f", value * 0.01)
            current_format = "%.2f"
        } else if (vf == ValueFormat.Three_Decimal) {
            reVal = String.format("%.3f", value * 0.001)
            current_format = "%.3f"
        } else if (vf == ValueFormat.Hex) {
            reVal = String.format("%s", Integer.toHexString(value))
            val len = reVal.length
            for (i in 0 until 4 - len) {
                reVal = "0$reVal"
            }
            current_format = "%s"
        }
        if (sign_visible) {
            reVal += parameter_format_output_sign()
        }
        return reVal
    }

    fun parameter_format_output_sign(): String {
        var reVal = ""
        if (sf == SignFormat.None) reVal += "" else if (sf == SignFormat.Volt) reVal += "[V]" else if (sf == SignFormat.Ampere) reVal += "[A]" else if (sf == SignFormat.Millimetre) reVal += "[mm]" else if (sf == SignFormat.Centimeter) reVal += "[cm]" else if (sf == SignFormat.Meter) reVal += "[m]" else if (sf == SignFormat.Second) reVal += "[sec]" else if (sf == SignFormat.Minute) reVal += "[min]" else if (sf == SignFormat.Hertz) reVal += "[Hz]" else if (sf == SignFormat.Celsius) reVal += "[℃]" else if (sf == SignFormat.Percent) reVal += "[%]" else if (sf == SignFormat.RPM) reVal += "[rpm]" else if (sf == SignFormat.PPR) reVal += "[ppr]" else if (sf == SignFormat.Kbps) reVal += "[Kbps]" else if (sf == SignFormat.Inch) reVal += "[inch]"
        return reVal
    }

    fun parameter_format_input(value: Float): Int {
        var reVal = 0f
        if (vf == ValueFormat.None) reVal = value else if (vf == ValueFormat.Zero_OFF) reVal = value else if (vf == ValueFormat.Zero_Decimal) reVal =
            value else if (vf == ValueFormat.One_Decimal) reVal = value * 10 else if (vf == ValueFormat.Two_Decimal) reVal =
            value * 100 else if (vf == ValueFormat.Three_Decimal) reVal = value * 1000

        return reVal.roundToInt()
    }
}
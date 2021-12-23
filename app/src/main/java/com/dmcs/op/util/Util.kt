package com.dmcs.op.util

import android.R.attr.*
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.dmcs.op.AppData
import com.dmcs.op.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.File
import kotlin.experimental.and


object Util {
    fun mini_check(name: String): Boolean {
        var check = false
        var format = ""

        if (AppData.Mini.sminif == 1) {
            if (AppData.Mini.current_type == 0) {
                AppData.Mini.current_type = 20
            }

            format = "S-MINI%ss%s D_Ver%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
            if (name == format)
                check = true
        } else {
            if (AppData.Main.Mini_Version.toByte() == AppData.Trac_Con_CAN_Version) {
                format = "Mini%ss%s CANstk%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                if (name == format)
                    check = true

                format = "Mini%ss%s CANagv%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                if (name == format)
                    check = true

                format = "Mini%ss%s CAN Ver:%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                if (name == format)
                    check = true

            } else {
                format = "Mini%ss%s Evec_V%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                if (name == format)
                    check = true
                if (AppData.Mini.new_version > 25) {
                    format = "Mini%ss%s Stacker%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                    if (name == format)
                        check = true
                } else {
                    format = "Mini110s%s Stacker%.1f".format(AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                    if (name == format)
                        check = true
                }
                if (AppData.Mini.new_version > 25) {
                    format = "Mini%ss%s AGV_Ver%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                    if (name == format)
                        check = true
                } else {
                    format = "Mini110s%s AGV_Ver%.1f".format(AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                    if (name == format)
                        check = true
                }
                if (AppData.Mini.new_version > 25) {
                    format = "Mini%ss%s MPS_Ver%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                    if (name == format)
                        check = true
                } else {
                    format = "Mini110s%s MPS_Ver%.1f".format(AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                    if (name == format)
                        check = true
                }
                if (AppData.Mini.new_version > 15) {
                    if (AppData.Mini.new_version > 25) {
                        format = "Mini%ss%s EV_Ver:%.1f".format(AppData.Mini.current_type.toString(), AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                        if (name == format)
                            check = true
                    } else {
                        format = "Mini110s%s EV_Ver:%.1f".format(AppData.Mini.batt_type.toString(), AppData.Mini.new_version * 0.1)
                        if (name == format)
                            check = true
                    }
                }
                if (AppData.Mini.batt_type == 12) {
                    format = "Mini110s12 Version1.5"
                    if (name == format)
                        check = true
                } else if (AppData.Mini.batt_type == 24) {
                    format = "Mini110s24 Version1.5"
                    if (name == format)
                        check = true
                } else if (AppData.Mini.batt_type == 36) {
                    format = "Mini160s36 Version1.5"
                    if (name == format)
                        check = true
                }
            }
            if (AppData.Main.Mini_Version == 0x11) {
                format = "Mini110s24 Version1.1"
                if (name == format)
                    check = true
            } else if (AppData.Main.Mini_Version == 0x12) {
                if (AppData.Mini.batt_type == 12) {
                    format = "Mini110s12 Version1.2"
                    if (name == format)
                        check = true
                } else if (AppData.Mini.batt_type == 24) {
                    format = "Mini110s24 Version1.2"
                    if (name == format)
                        check = true
                } else if (AppData.Mini.batt_type == 36) {
                    format = "Mini160s36 Version1.2"
                    if (name == format)
                        check = true
                }
            }
        }
        return check
    }

    fun TestRecording(str:String) {
        val directory = File(Environment.getExternalStorageDirectory().toString() + "/DMCS/Record")
        if(!directory.exists())
            directory.mkdirs()

        var file = File(Environment.getExternalStorageDirectory().toString() + "/DMCS/Record/cord.txt")
        file.writeText(str + "\n")
    }
    fun HideKeyboard(activity: Activity, v: View) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun toHexString(buf: ByteArray, len: Int): String? {
        val sb = StringBuffer()
        sb.append("0x")
        for (i in 0 until len) {
            sb.append(Integer.toHexString(0x0100 + (buf[i] and 0x00FF.toByte())).substring(1))
        }
        return sb.toString()
    }

    fun byte2short(src: ByteArray, index: Int): Int {
        val s1: Int = (src[index + 1].toInt() and 0xFF)
        val s2: Int = (src[index].toInt() and 0xFF)
        return (s1 shl 8) + (s2 shl 0)
    }

    fun intToByteArray(value: Int): ByteArray? {
        val byteArray = ByteArray(4)
        byteArray[3] = (value shr 24 and 0xFF).toByte()
        byteArray[2] = (value shr 16 and 0xFF).toByte()
        byteArray[1] = (value shr 8 and 0xFF).toByte()
        byteArray[0] = (value and 0xFF).toByte()
        return byteArray
    }

    fun mini_error_text(error_code: Int): String {
        var error = ""
        AppData.Mini.error_code = error_code

        if (AppData.Mini.error_code != 0) {
            if (AppData.Mini.sminif == 1) {
                if (AppData.Mini.error_code == 1)
                    error = "E01: Low Device Error"    // M1또는 M2 단자가 GND(B-)와 Short될 경우
                else if (AppData.Mini.error_code == 2)
                    error = "E02: High Device Err "    // M1또는 M2 단자가 Vcc(B+)와 Short될 경우
                else if (AppData.Mini.error_code == 3)
                    error = "E03: High volt input "    // key_input = High Battery Check 32V이상
                else if (AppData.Mini.error_code == 4)
                    error = "E04: Low Battery     "    // B+ input = Low Battery Check 15V이하
                else if (AppData.Mini.error_code == 5)
                    error = "E05: High pedal input"    // high_pedal_disable ON
                else if (AppData.Mini.error_code == 6)
                    error = "E06: Zero Battery    "    // B+ input = Low Battery Check 12V이하
                else if (AppData.Mini.error_code == 7)
                    error = "E07: H-Sink over temp"    // 75도 ~ 87도- Heat Sink over temp
                else if (AppData.Mini.error_code == 8)
                    error = "E08: Drive over temp "    // 80도 이상 - Heat Sink over temp
                else if (AppData.Mini.error_code == 9)
                    error = "E09: Short Brake err "    // M1과 M2를 GND(B-)로 Short시 전압이 발생할 경우
                else if (AppData.Mini.error_code == 10)
                    error = "E10: E-memory error  "    // eeprom Memory error
                else if (AppData.Mini.error_code == 11)
                    error = "E11: Accel Limit err "    // Accel limit error
                else if (AppData.Mini.error_code == 12)
                    error = "E12: New error...... "    // New error
                else if (AppData.Mini.error_code == 13)
                    error = "E13: New error...... "    // New error
                else if (AppData.Mini.error_code == 14)
                    error = "E14: New error...... "    // New error
                else if (AppData.Mini.error_code == 15)
                    error = "E15: New error...... "    // New error
                else if (AppData.Mini.error_code == 16)
                    error = "E16: New error...... "    // New error
            } else {
                if (AppData.Mini.error_code == 1)
                    error = "E01: Batt. charger ON"    // battery charger connector ON check
                else if (AppData.Mini.error_code == 2)
                    error = "E02: Low volt input  "    // key_input = Low Battery Check 15V이하
                else if (AppData.Mini.error_code == 3)
                    error = "E03: High volt input "    // key_input = High Battery Check 32V이상
                else if (AppData.Mini.error_code == 4)
                    error = "E04: Low Battery     "    // B+ input = Low Battery Check 15V이하
                else if (AppData.Mini.error_code == 5)
                    error = "E05: High pedal input"    // high_pedal_disable ON
                else if (AppData.Mini.error_code == 6)
                    error = "E06: Over_current    "    // OVER_CURRENT
                else if (AppData.Mini.error_code == 7)
                    error = "E07: H-Sink over temp"    // 80도 - Heat Sink over temp
                else if (AppData.Mini.error_code == 8)
                    error = "E08: Drive over temp "    // 90도 이상 - Heat Sink over temp
                else if (AppData.Mini.error_code == 9)
                    error = "E09: Brake coil error"    // Brake coil open error
                else if (AppData.Mini.error_code == 10)
                    error = "E10: E-memory error  "    // eeprom Memory error
                else if (AppData.Mini.error_code == 11)
                    error = "E11: Data Read error "    // Data Read error
                else if (AppData.Mini.error_code == 12)
                    error = "E12: Belly On error  "    // Belly Switch on error
                else if (AppData.Mini.error_code == 13)
                    error = "E13: Accel Limit err "    // Accel limit error
                else if (AppData.Mini.error_code == 14)
                    error = "E14: Over Load error "    // Over Load error
                else if (AppData.Mini.error_code == 15)
                    error = "E15: S-Over Current  "    // New error 2011.03.11 수정
                else if (AppData.Mini.error_code == 16)
                    error = "E16: Motor Short Err "    // New error 2014.02.10 추가
                else if (AppData.Mini.error_code == 17)
                    error = "E17: Device Error    "    // New error 2014.03.03 MINI250
                else if (AppData.Mini.error_code == 18)
                    error = "E18:Magnet Coil Short"    // New error 2014.03.03 MINI250
                else if (AppData.Mini.error_code == 19)
                    error = "E19: Return Switch ON"    // New error 2017.01.31 추가 (삼부기계)
                else if (AppData.Mini.error_code == 20)
                    error = "E20: Safety Power Off"    // New error 2018.03.24 추가
                else if (AppData.Mini.error_code == 21)
                    error = "E21: CAN Fault Error "    // New error 2019.07.17 추가
                else if (AppData.Mini.error_code == 22)
                    error = "E22: Master Error... "    // New error 2019.07.17 추가
                else if (AppData.Mini.error_code == 23)
                    error = "E23: New error...... "    // New error
                else if (AppData.Mini.error_code == 24)
                    error = "E24: New error...... "    // New error
                else if (AppData.Mini.error_code == 25)
                    error = "E25: New error...... "    // New error
                else if (AppData.Mini.error_code == 26)
                    error = "E26: New error...... "    // New error
                else if (AppData.Mini.error_code == 27)
                    error = "E27: New error...... "    // New error
                else if (AppData.Mini.error_code == 28)
                    error = "E28: New error...... "    // New error
                else if (AppData.Mini.error_code == 29)
                    error = "E29: New error...... "    // New error
                else if (AppData.Mini.error_code == 30)
                    error = "E30: New error...... "    // New error
                else
                    error = "E99: New error...... "    // New error
            }
        }
        return error
    }

    var bar: Snackbar? = null

    @Synchronized
    fun snack_error_indefinite_show(view: View, message: String) {
        var temp: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        temp.setAction("닫기", View.OnClickListener { temp.dismiss() })
        temp.setActionTextColor(Color.parseColor("#44DB6F"))
        temp.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        temp.view.backgroundTintList = ColorStateList.valueOf((Color.parseColor("#CCDB4455")))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.parseColor("#FFFFFF"))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_action).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setMaxLines(20)
        temp.setAnchorView(MainActivity.mBinding!!.snack)

        snack_dismiss()

        bar = temp
        temp.show()
    }

    @Synchronized
    fun snack_error_show(view: View, message: String) {
        var temp: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        temp.setAction("닫기", View.OnClickListener { temp.dismiss() })
        temp.setActionTextColor(Color.parseColor("#44DB6F"))
        temp.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        temp.view.backgroundTintList = ColorStateList.valueOf((Color.parseColor("#CCDB4455")))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.parseColor("#FFFFFF"))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_action).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setMaxLines(20)
        temp.setAnchorView(MainActivity.mBinding!!.snack)

        snack_dismiss()
        bar = temp
        temp.show()
    }

    @Synchronized
    fun snack_indefinite_show(view: View, message: String) {
        var temp: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        temp.setAction("닫기", View.OnClickListener { temp.dismiss() })
        temp.setActionTextColor(Color.parseColor("#44DB6F"))
        temp.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        temp.view.backgroundTintList = ColorStateList.valueOf((Color.parseColor("#CC2D2D2D")))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.parseColor("#FFFFFF"))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setHorizontallyScrolling(true)
        temp.view.findViewById<TextView>(R.id.snackbar_text).setMovementMethod(ScrollingMovementMethod())
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_action).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setMaxLines(30)
        temp.setAnchorView(MainActivity.mBinding!!.snack)

        snack_dismiss()
        bar = temp
        temp.show()
    }

    @Synchronized
    fun snack_show(view: View, message: String) {
        var temp: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        temp.setAction("닫기", View.OnClickListener { temp.dismiss() })
        temp.setActionTextColor(Color.parseColor("#44DB6F"))
        temp.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        temp.view.backgroundTintList = ColorStateList.valueOf((Color.parseColor("#CC2D2D2D")))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.parseColor("#FFFFFF"))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_action).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setMaxLines(30)
        temp.setAnchorView(MainActivity.mBinding!!.snack)

        snack_dismiss()
        bar = temp
        temp.show()
    }

    @Synchronized
    fun snack_progress_show(activity: Activity, view: View, message: String) {
        var temp: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        temp.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        temp.view.backgroundTintList = ColorStateList.valueOf((Color.parseColor("#CC2D2D2D")))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.parseColor("#FFFFFF"))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_action).setTypeface(ResourcesCompat.getFont(view.context.getApplicationContext(), R.font.one_mobile_pop))
        temp.view.findViewById<TextView>(R.id.snackbar_text).setMaxLines(30)
        val contentLay = temp.view.findViewById<View>(R.id.snackbar_text).parent as ViewGroup
        val item = ProgressBar(activity)
        item.setPadding(0, 10, 0, 10)

        contentLay.addView(item)
        temp.setAnchorView(MainActivity.mBinding!!.snack)

        snack_dismiss()
        bar = temp
        temp.show()
    }

    fun snack_dismiss() {
        bar?.dismiss()
    }

    fun parameter_all_make() {
        if(AppData.Parameter_Map.size==0){
            if (AppData.ID_NUMBER == AppData.Protocol.TAN_ID) {
                var num = 0
                // 파라메터 메인 갯수
                while (AppData.Connect) {
                    var i_array: ByteArray? = MainActivity.mTANPacket.send_param_main_total_count()
                    if (i_array != null) {
                        num = i_array[3].toInt()
                        break
                    }
                }

                // 파라메터 메인 이름목록
                var i = 0
                while (AppData.Connect) {
                    if (i == num)
                        break
                    var i_array = MainActivity.mTANPacket.send_param_main_name(i)
                    if (i_array != null) {
                        var main_key = String(i_array, 3, 20).trim()
                        // 파라메터 서브 갯수
                        i_array = MainActivity.mTANPacket.send_param_sub_total_count(i)
                        if (i_array != null) {
                            var main_child_count = i_array[3].toInt()
                            var main_item = ParamItem(main_key, main_child_count)

                            AppData.Parameter_Map.put(main_key, main_item)
                            main_item.child_map.put("00] 돌아가기", ParamItem("00] 돌아가기", 0))

                            // 파라메터 서브 이름목록
                            var j = 0
                            while (AppData.Connect) {
                                if (j == main_child_count)
                                    break
                                i_array = MainActivity.mTANPacket.send_param_sub_name(i, j)
                                if (i_array != null) {
                                    var sub_key = String(i_array, 3, 20).trim()
                                    // 파라메터 갯수
                                    i_array = MainActivity.mTANPacket.send_param_param_total_count(i, j)
                                    if (i_array != null) {
                                        var sub_child_count = i_array[3].toInt()
                                        var sub_item = ParamItem(sub_key, sub_child_count)

                                        main_item.child_map.put(sub_key, sub_item)
                                        main_item.child_map.get(sub_key)!!.child_map.put("00] 돌아가기", ParamItem("00] 돌아가기", 0))

                                        // 파라메터 정보
                                        var k = 0
                                        while (AppData.Connect) {
                                            if (k == sub_child_count)
                                                break
                                            i_array = MainActivity.mTANPacket.send_param_param_info(i, j, k)
                                            if (i_array != null) {
                                                var param_key = String(i_array, 3, 20).trim()
                                                if (param_key == "" || param_key == null || param_key.isEmpty())
                                                    param_key = sub_key

                                                main_item.child_map.get(sub_key)!!.child_map.put(
                                                    param_key, ParamItem(
                                                        param_key,
                                                        Util.byte2short(i_array, 23),
                                                        Util.byte2short(i_array, 25),
                                                        Util.byte2short(i_array, 27),
                                                        Util.byte2short(i_array, 29),
                                                        Util.byte2short(i_array, 31),
                                                        Util.byte2short(i_array, 33)
                                                    )
                                                )
                                                k++
                                            }
                                        }
                                        j++
                                    }
                                }
                            }
                            i++
                        }
                    }
                }
            } else {
                var up_count = 200
                var down_count = 0
                var baccel_type = 0
                if (AppData.Mini.new_version > 40) {
                    up_count -= 2
                } else if (AppData.Mini.new_version > 37) {
                    up_count = 35
                } else if (AppData.Mini.new_version > 36) {
                    up_count = 34
                } else if (AppData.Mini.new_version > 31) {
                    up_count = 33
                } else if (AppData.Mini.new_version > 28) {
                    up_count = 32
                } else if (AppData.Mini.new_version > 19) {
                    up_count = 31
                } else if (AppData.Mini.new_version > 18) {
                    up_count = 30
                } else if (AppData.Mini.new_version > 16) {
                    up_count = 26
                } else if (AppData.Mini.new_version > 15) {
                    up_count = 25
                } else {
                    up_count = 24
                }
                down_count = up_count + 1

                if (AppData.Mini.new_version > 20) {
                    var i_array: ByteArray? = MainActivity.mMINI110CANPacket.send_Param_Accel_Type()
                    if (i_array != null) {
                        baccel_type = Util.byte2short(i_array, 3)
                    }
                }

                var param_name_list: Array<String>? = null
                if (AppData.ID_NUMBER == AppData.Protocol.MINI_DT_ID) {
                    param_name_list = arrayOf(
                        "01] Batt Empty Level",
                        "02] Batt Full Level",
                        "03] Batt Reset Level",
                        "04] For Accel Inc St1",
                        "05] For Accel Inc St2",
                        "06] For Accel Dec St1",
                        "07] For Accel Dec St2",
                        "08] Rev Accel Inc St1",
                        "09] Rev Accel Inc St2",
                        "10] Rev Accel Dec St1",
                        "11] Rev Accel Dec St2",
                        "12] Step1-Step2 Duty",
                        "13] Forward Max Speed",
                        "14] Reverse Max Speed",
                        "15] Back Horn Solid",
                        "16] M/C Brake Fault",
                        "17] Start Speed Duty",
                        "18] Emergency Stop",
                        "19] High Pedal Fault",
                        "20] Pedal Full Volt / 20] Pedal Zero Volt",
                        "21] Pedal Zero Volt / 21] Pedal Full Volt",
                        "22] Speed Scale Volt",
                        "23] Auto Speed Scale",
                        "24] Brake Delay Time",
                        "25] Motor Max Current",
                        "26] Machine Brake On.",
                        "27] Turtle Max Speed",
                        "28] Safety Off Time",
                        "29] Belly On Time",
                        "30] Belly On Duty / 30] Handle Max Speed",
                        "31] Slope Route Drive",
                        "32] Hill Route Drive",
                        "33] Accel Pedal Type",
                        "34] Accel Limit Over",
                        "35] Neutral Stop Time",
                        "36] Drive Motor Type",
                        "37] Low Battery Volt",
                        "38] Speed Limit Fault",
                        "39] EM Brake On Volt",
                        "40] BDI Low Percent",
                        "41] Smart Key Active",
                        "42] Remote High Duty",
                        "43] Remote Medium Dut",
                        "44] Remote Low Duty",
                        "45] Can Cruze Duty ",
                        "46] Babycar Rev Duty",
                        "47] Board Rev Duty ",
                        "48] Babycar For Duty",
                        "49] Board1 For Duty",
                        "50] Board2 For Duty",
                        "51] Board3 For Duty",
                        "52] Baby For Inc St1",
                        "53] Baby For Inc St2",
                        "54] Baby For Dec St1",
                        "55] Baby For Dec St2",
                        "56] Board1 ForInc St1",
                        "57] Board1 ForInc St2",
                        "58] Board1 ForDec St1",
                        "59] Board1 ForDec St2",
                        "60] Board2 ForInc St1",
                        "61] Board2 ForInc St2",
                        "62] Board2 ForDec St1",
                        "63] Board2 ForDec St2",
                        "64] Board3 ForInc St1",
                        "65] Board3 ForInc St2",
                        "66] Board3 ForDec St1",
                        "67] Board3 ForDec St2",
                        "68] Smart Remote Auto",
                        "69] Baby Start Turtle",
                        "70] BoardStart Turtle",
                        "71] DTcarStart Turtle",
                        "72] Baby Turtle Swit.",
                        "73] BoardTurtle Swit.",
                        "74] DTcar Turtle Swit",
                        "75] Rev Turtle Switch",
                        "76] Rev Limit Active",
                        "77] Rev Accel Active",
                        "78] RevAccel Max Duty"
                    )
                } else if (AppData.ID_NUMBER_MINI250_FLAG) {
                    param_name_list = arrayOf(
                        "01] Batt Empty Level ",    //0
                        "02] Batt Full Level  ",    //1
                        "03] Batt Reset Level ",    //2
                        "04] For Accel Inc St1",    //3
                        "05] For Accel Inc St2",    //4
                        "06] For Accel Dec St1",    //5
                        "07] For Accel Dec St2",    //6
                        "08] Rev Accel Inc St1",    //7
                        "09] Rev Accel Inc St2",    //8
                        "10] Rev Accel Dec St1",    //9
                        "11] Rev Accel Dec St2",    //10
                        "12] Step1-Step2 Duty ",    //11
                        "13] Forward Max Speed",    //12
                        "14] Reverse Max Speed",    //13
                        "15] B/Horn Function ",    //14		// 2012.07.24 bak_horn_solid ==> Back Horn Function
                        "16] EM Brake Fault  ",    //15		//  M/C Brake Fault
                        "17] Start Speed Duty",    //16
                        "18] Emergency Stop  ",    //17
                        "19] High Pedal Fault",    //18
                        "20] Pedal Zero Volt ",    //19
                        "21] Pedal Full Volt ",    //20
                        "22] Speed Scale Volt",    //21
                        "23] Auto Speed Scale",    //22
                        "24] Brake Delay Time",    //23
                        "25] Motor Max Current",    //24
                        "26] Machine Brake On.",    //25
                        "27] Turtle Max Speed",    //26
                        "28] Safety Off Time ",    //27
                        "29] Belly On Time   ",    //28
                        "30] Belly On Duty   ",    //29
                        "31] Slope Route Drive",    //30
                        "32] Hill Route Drive ",    //31
                        "33] Accel Pedal Type ",    //32
                        "34] Accel Limit Over ",    //33
                        "35] Neutral Stop Time",    //34
                        "36] Drive Motor Type ",    //35
                        "37] Low Battery Volt",    //36
                        "38] Speed Limit Fault",    //37
                        "39] EM Brake On Volt",    //38
                        "40] Main M/C On Volt",    //39
                        "41] BDI Low Percent ",    //40		// 2012.07.24 추가
                        "42] Switch3 Input typ",    //41	0=Brake, 1=T-Rorate	// 2019.03.07 추가
                        "43] TRotate Max Speed",    //42	80 10-100	// 2019.03.07 추가
                        "44] STurtle Max Speed",    //43	10  5-100	// 2019.03.07 추가
                        "45] Rotate Max Speed"    //45	50  5-100
                    )
                } else {
                    param_name_list = arrayOf(
                        "01] Batt Empty Level",
                        "02] Batt Full Level",
                        "03] Batt Reset Level",
                        "04] For Accel Inc St1",
                        "05] For Accel Inc St2",
                        "06] For Accel Dec St1",
                        "07] For Accel Dec St2",
                        "08] Rev Accel Inc St1",
                        "09] Rev Accel Inc St2",
                        "10] Rev Accel Dec St1",
                        "11] Rev Accel Dec St2",
                        "12] Step1-Step2 Duty",
                        "13] Forward Max Speed",
                        "14] Reverse Max Speed",
                        "15] B/Horn Function",
                        "16] M/C Brake Fault",
                        "17] Start Speed Duty",
                        "18] Emergency Stop",
                        "19] High Pedal Fault",
                        "20] Pedal Full Volt / 20] Pedal Zero Volt",
                        "21] Pedal Zero Volt / 21] Pedal Full Volt",
                        "22] Speed Scale Volt",
                        "23] Auto Speed Scale",
                        "24] Brake Delay Time",
                        "25] Motor Max Current",
                        "26] Machine Brake On.",
                        "27] Turtle Max Speed",
                        "28] Safety Off Time",
                        "29] Belly On Time",
                        "30] Belly On Duty / 30] Handle Max Speed",
                        "31] Slope Route Drive",
                        "32] Hill Route Drive",
                        "33] Accel Pedal Type",
                        "34] Accel Limit Over",
                        "35] Neutral Stop Time",
                        "36] Drive Motor Type",
                        "37] Low Battery Volt",
                        "38] Speed Limit Fault",
                        "39] EM Brake On Volt",
                        "40] BDI Low Percent",
                        "41] Accel SW Select",
                        "42] Accel SW1 Speed",
                        "43] Accel SW2 Speed",
                        "44] Accel SW3 Speed",
                        "45] Accel SW4 Speed",
                        "46] Switch3 InputType",
                        "47] ForRev Rotate Max",
                        "48] Start Turtle Max",
                        "49] CAN Baud Rate",
                        "50] CAN My ID",
                        "51] CAN Master ID",
                        "52] CAN Fault Delay T"
                    )
                }

                var i = 0
                while (AppData.Connect) {
                    if (i == up_count)
                        break

                    var min = 0
                    var max = 0
                    var value = 0
                    var unit = i
                    var step = 1
                    var special = 0

                    var i_array: ByteArray? = MainActivity.mMINI110CANPacket.send_Param_Read(i)
                    if (i_array != null) {
                        value = Util.byte2short(i_array, 3)

                        i_array = MainActivity.mMINI110CANPacket.send_Param_Min_Read(i)
                        if (i_array != null) {
                            min = Util.byte2short(i_array, 3)

                            i_array = MainActivity.mMINI110CANPacket.send_Param_Max_Read(i)
                            if (i_array != null) {
                                max = Util.byte2short(i_array, 3)

                                var param_split: List<String>? = null
                                if (param_name_list.size <= i) {
                                    param_split = ArrayList<String>()
                                    param_split.add(String.format("%d] New Data...", (i + 1)))
                                } else {
                                    param_split = param_name_list[i].split(" / ")
                                }

                                var key = ""
                                if (param_split?.size == 1) {
                                    key = param_split!![0]
                                } else {
                                    if (i == 19 || i == 20) {
                                        if (AppData.Mini.new_version > 20 && baccel_type == 3) {
                                            key = param_split!![0]
                                        } else {
                                            key = param_split!![1]
                                        }
                                    } else if (i == 29) {
                                        if (AppData.Mini.evecf == 0) {
                                            key = param_split!![0]
                                        } else {
                                            key = param_split!![1]
                                        }
                                    }
                                }

                                AppData.Parameter_Map.put(key.trim(), ParamItem(key.trim(), min, max, step, unit, special, value))
                                i++
                                continue
                            }
                        }
                    }
                    break
                }
            }
        }
    }
}
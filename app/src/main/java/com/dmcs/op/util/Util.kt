package com.dmcs.op.util

import android.R.attr.*
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and


object Util {
    fun log(str:String){
        if(AppData.log.length>=3000){
            AppData.log = AppData.log.substring(AppData.log.indexOf("\n")+1, AppData.log.length)
        }
        AppData.log += str + "\n"
    }

    fun status_log(status: String) {
        Log.d(SimpleDateFormat("hh:mm:ss.SSS ").format(Date()), status)
        log(status)
    }

    fun status(status: String) {
        Log.d(SimpleDateFormat("hh:mm:ss.SSS ").format(Date()), status)
    }

    fun status_e(status: String) {
        Log.e("Status", status)
    }

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
        temp.setAnchorView(view)

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
        temp.setAnchorView(view)

        snack_dismiss()
        bar = temp
        temp.show()
    }

    fun snack_dismiss() {
        bar?.dismiss()
    }
}
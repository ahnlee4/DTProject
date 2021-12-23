package dmcs.tan.FoldingRecycler

import android.animation.ValueAnimator
import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dmcs.op.R
import java.util.*
import kotlin.experimental.and

class FoldingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textview_title: TextView
    var layout_digital_content: LinearLayout
    var imageview_icon: ImageView
    var linearlayout: LinearLayout
    var textview_error_digital1: TextView
    var textview_error_digital2: TextView
    var textview_error_digital3: TextView
    var textview_error_digital4: TextView
    var textview_error_digital5: TextView
    var textview_error_digital6: TextView
    var textview_error_digital7: TextView
    var textview_error_digital8: TextView
    var textview_error_digital9: TextView
    var textview_error_digital10: TextView
    var textview_error_digital11: TextView
    var textview_error_digital12: TextView
    var layout_error_digital1: LinearLayout
    var layout_error_digital2: LinearLayout
    var layout_error_digital3: LinearLayout
    var layout_error_digital4: LinearLayout
    var layout_error_digital5: LinearLayout
    var layout_error_digital6: LinearLayout
    var layout_error_digital7: LinearLayout
    var layout_error_digital8: LinearLayout
    var layout_error_digital9: LinearLayout
    var layout_error_digital10: LinearLayout
    var layout_error_digital11: LinearLayout
    var layout_error_digital12: LinearLayout
    lateinit var onViewHolderItemClickListener2: OnViewHolderItemClickListener2

    fun onBind(data: String, position: Int, selectedItems: SparseBooleanArray) {
        textview_title.setText(data)
        try {
//            Font_HighLight(
//                0, textview_error_digital1, layout_error_digital1,
//                AppData.ErrorHistoryinfo.digital_data_map.get("FWD"), AppData.ErrorHistoryinfo.LineDataLow, 0x01.toByte()
//            )
//            Font_HighLight(
//                1, textview_error_digital2, layout_error_digital2,
//                AppData.ErrorHistoryinfo.digital_data_map.get("BWD"), AppData.ErrorHistoryinfo.LineDataLow, 0x02.toByte()
//            )
//            Font_HighLight(
//                2, textview_error_digital3, layout_error_digital3,
//                AppData.ErrorHistoryinfo.digital_data_map.get("ACC"), AppData.ErrorHistoryinfo.LineDataLow, 0x04.toByte()
//            )
//            Font_HighLight(
//                3, textview_error_digital4, layout_error_digital4,
//                AppData.ErrorHistoryinfo.digital_data_map.get("BRK"), AppData.ErrorHistoryinfo.LineDataLow, 0x08.toByte()
//            )
//            Font_HighLight(
//                4, textview_error_digital5, layout_error_digital5,
//                AppData.ErrorHistoryinfo.digital_data_map.get("TURR"), AppData.ErrorHistoryinfo.LineDataLow, 0x10.toByte()
//            )
//            Font_HighLight(
//                5, textview_error_digital6, layout_error_digital6,
//                AppData.ErrorHistoryinfo.digital_data_map.get("EMER"), AppData.ErrorHistoryinfo.LineDataLow, 0x20.toByte()
//            )
//            Font_HighLight(
//                6, textview_error_digital7, layout_error_digital7,
//                AppData.ErrorHistoryinfo.digital_data_map.get("EMB"), AppData.ErrorHistoryinfo.LineDataLow, 0x40.toByte()
//            )
//            Font_HighLight(
//                7, textview_error_digital8, layout_error_digital8,
//                AppData.ErrorHistoryinfo.digital_data_map.get("RELE"), AppData.ErrorHistoryinfo.LineDataLow, 0x80.toByte()
//            )
//            Font_HighLight(
//                8, textview_error_digital9, layout_error_digital9,
//                AppData.ErrorHistoryinfo.digital_data_map.get("LEFT"), AppData.ErrorHistoryinfo.LineDataHigh, 0x01.toByte()
//            )
//            Font_HighLight(
//                9, textview_error_digital10, layout_error_digital10,
//                AppData.ErrorHistoryinfo.digital_data_map.get("RIGHT"), AppData.ErrorHistoryinfo.LineDataHigh, 0x02.toByte()
//            )
//            Font_HighLight(
//                10, textview_error_digital11, layout_error_digital11,
//                AppData.ErrorHistoryinfo.digital_data_map.get("C-BRK"), AppData.ErrorHistoryinfo.LineDataHigh, 0x04.toByte()
//            )
//            Font_HighLight(
//                11, textview_error_digital12, layout_error_digital12,
//                AppData.ErrorHistoryinfo.digital_data_map.get("XXXX"), AppData.ErrorHistoryinfo.LineDataHigh, 0x08.toByte()
//            )
        } catch (e: Exception) {
        }
        changeVisibility(selectedItems[position])
        changeVisibility_icon(selectedItems[position])
    }

    var maxheight: Int
    private fun changeVisibility(isExpanded: Boolean) {
        val height = if (isExpanded) maxheight else 0
        layout_digital_content.layoutParams.height = height
        layout_digital_content.requestLayout()
        if (height == maxheight) {
            layout_digital_content.visibility = View.VISIBLE
        } else if (height == 0) {
            layout_digital_content.visibility = View.GONE
        }
//        AppData.ErrorHistoryinfo.Digital_Data_Flag = true
    }

    private fun changeVisibility_icon(isExpanded: Boolean) {
        val va = if (isExpanded) ValueAnimator.ofInt(0, 180) else ValueAnimator.ofInt(180, 0)
        va.duration = 500
        va.addUpdateListener { animation ->
            imageview_icon.setRotation(animation.animatedValue as Float)
            imageview_icon.requestLayout()
        }
        va.start()
    }

    abstract class OnViewHolderItemClickListener2{
        abstract fun onViewHolderItemClick()
    }

    fun setOnViewHolderItemClickListener(onViewHolderItemClickListener: OnViewHolderItemClickListener2?) {
        if (onViewHolderItemClickListener != null) {
            this.onViewHolderItemClickListener2 = onViewHolderItemClickListener
        }
    }

    companion object {
        var digitalList = ArrayList<Boolean>()
        fun Font_HighLight(num: Int, v: TextView, l1: LinearLayout, line: String, sw: Byte, b: Byte) {
            var line = line
            try {
                line = line.replace(" ".toRegex(), "")
                val index = line.indexOf("[")
                val name = line.substring(0, index)
                var check = false
                val tmp = (sw and b) as Byte
                check = if (tmp == b) {
                    true
                } else {
                    false
                }
                try {
                    if (digitalList[num] == check) {
                        return
                    } else {
                        digitalList[num] = check
                    }
                } catch (e: Exception) {
                    digitalList.add(num, check)
                }
                if (digitalList[num]) {
                    l1.setBackgroundResource(R.drawable.border_on)
                    v.setTextColor(Color.WHITE)
                } else {
                    l1.setBackgroundResource(R.drawable.border_off)
                    v.setTextColor(Color.WHITE)
                }
                v.text = name
            } catch (e: Exception) {
            }
        }
    }

    init {
        imageview_icon = itemView.findViewById(R.id.id_imageview_icon)
        textview_title = itemView.findViewById(R.id.id_textview_title)
        layout_digital_content = itemView.findViewById(R.id.id_layout_digital_content)
        linearlayout = itemView.findViewById(R.id.linearlayout)
        textview_error_digital1 = itemView.findViewById(R.id.id_textview_error_digital1)
        textview_error_digital2 = itemView.findViewById(R.id.id_textview_error_digital2)
        textview_error_digital3 = itemView.findViewById(R.id.id_textview_error_digital3)
        textview_error_digital4 = itemView.findViewById(R.id.id_textview_error_digital4)
        textview_error_digital5 = itemView.findViewById(R.id.id_textview_error_digital5)
        textview_error_digital6 = itemView.findViewById(R.id.id_textview_error_digital6)
        textview_error_digital7 = itemView.findViewById(R.id.id_textview_error_digital7)
        textview_error_digital8 = itemView.findViewById(R.id.id_textview_error_digital8)
        textview_error_digital9 = itemView.findViewById(R.id.id_textview_error_digital9)
        textview_error_digital10 = itemView.findViewById(R.id.id_textview_error_digital10)
        textview_error_digital11 = itemView.findViewById(R.id.id_textview_error_digital11)
        textview_error_digital12 = itemView.findViewById(R.id.id_textview_error_digital12)
        layout_error_digital1 = itemView.findViewById(R.id.id_layout_error_digital1)
        layout_error_digital2 = itemView.findViewById(R.id.id_layout_error_digital2)
        layout_error_digital3 = itemView.findViewById(R.id.id_layout_error_digital3)
        layout_error_digital4 = itemView.findViewById(R.id.id_layout_error_digital4)
        layout_error_digital5 = itemView.findViewById(R.id.id_layout_error_digital5)
        layout_error_digital6 = itemView.findViewById(R.id.id_layout_error_digital6)
        layout_error_digital7 = itemView.findViewById(R.id.id_layout_error_digital7)
        layout_error_digital8 = itemView.findViewById(R.id.id_layout_error_digital8)
        layout_error_digital9 = itemView.findViewById(R.id.id_layout_error_digital9)
        layout_error_digital10 = itemView.findViewById(R.id.id_layout_error_digital10)
        layout_error_digital11 = itemView.findViewById(R.id.id_layout_error_digital11)
        layout_error_digital12 = itemView.findViewById(R.id.id_layout_error_digital12)
        linearlayout.setOnClickListener { onViewHolderItemClickListener2.onViewHolderItemClick() }
        maxheight = layout_digital_content.layoutParams.height
        digitalList = ArrayList()
    }
}
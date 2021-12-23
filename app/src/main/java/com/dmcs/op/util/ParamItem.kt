package com.dmcs.op.util

class ParamItem{
    var name:String? = null
    var value:Int = 0
    var unit:Int = 0
    var step:Int = 0
    var min:Int = 0
    var max:Int = 0
    var special:Int = 0
    var child_count:Int = 0
    var check:Boolean = false
    var type:Int = 0
    var content:String = ""
    var str_list:Array<String>? = null
    var child_map = HashMap<String, ParamItem>()

    internal class ParamItemComparator : Comparator<ParamItem> {
        override fun compare(o1: ParamItem, o2: ParamItem): Int {
            if (o1.name!!.compareTo(o2.name!!) > 0) return 1
            return if (o1.name!!.compareTo(o2.name!!) < 0) -1 else 0
        }
    }
    constructor () {
    }
    constructor (name:String?, child_count:Int) {
        this.name = name
        this.child_count = child_count
    }
    constructor (name:String?, min:Int, max:Int, step:Int, unit:Int, special:Int, value:Int) {
        this.name = name
        this.value = value
        this.unit = unit
        this.step = step
        this.min = min
        this.max = max
        this.special = special
        this.check = false
    }

    fun setData(name:String?, min:Int, max:Int, step:Int, unit:Int, special:Int, value:Int){
        this.name = name
        this.value = value
        this.unit = unit
        this.step = step
        this.min = min
        this.max = max
        this.special = special
        this.check = false
    }
}

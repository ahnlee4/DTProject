package com.dmcs.op.util

import java.io.File

class FileItem {
    internal class FileItemComparator :Comparator<File>
    {
        override fun compare(o1: File, o2: File): Int {
            if (o1.name.compareTo(o2.name) > 0) return 1
            return if (o1.name.compareTo(o2.name) < 0) -1 else 0
        }
    }
}
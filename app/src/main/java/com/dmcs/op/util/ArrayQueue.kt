package com.dmcs.baseproject.util

class ArrayQueue : Queue {
    private var array // 요소를 담을 배열
            : ByteArray
    private var size // 요소 개수
            : Int
    private var front // 시작 인덱스를 가리키는 변수(빈 공간임을 유의)
            : Int
    private var rear // 마지막 요소의 인덱스를 가리키는 변수
            : Int

    // 생성자1 (초기 용적 할당을 안할 경우)
    constructor() {
        array = ByteArray(DEFAULT_CAPACITY)
        size = 0
        front = 0
        rear = 0
    }

    // 생성자2 (초기 용적 할당을 할 경우)
    constructor(capacity: Int) {
        array = ByteArray(capacity)
        size = 0
        front = 0
        rear = 0
    }

    companion object {
        private const val DEFAULT_CAPACITY = 1024 // 최소(기본) 용적 크기
    }

    private fun resize(newCapacity: Int) {
//        val arrayCapacity: Int = array.size // 현재 용적 크기
//        val newArray = ByteArray(newCapacity) // 용적을 변경한 배열
//
//        /*
//	 * i = new array index
//	 * j = original array
//	 * index 요소 개수(size)만큼 새 배열에 값 복사
//	 */
//        var i = 1
//        var j = front + 1
//        while (i <= size) {
//            newArray[i] = array.get(j % arrayCapacity)
//            i++
//            j++
//        }
//        array = newArray // 새 배열을 기존 array의 배열로 덮어씌움
//        front = 0
//        rear = size
    }
    @Synchronized
    override fun offer(e: Any?): Boolean {
//        if((rear+1) % array.size == front){
//            resize(array.size*2)
//        }

        rear = (rear + 1) % array.size

        array.set(rear, e as Byte)
        size++
        return true
    }

    @Synchronized
    override fun poll(): Byte? {
        if (size == 0) {    // 삭제할 요소가 없을 경우 null 반환
            return null
        }

        front = (front + 1) % array.size // front 를 한 칸 옮긴다.


        size-- // 사이즈 감소

//        // 용적이 최소 크기(64)보다 크고 요소 개수가 1/4 미만일 경우
//        if (array.size > DEFAULT_CAPACITY && size < array.size / 4) {
//
//            // 아무리 작아도 최소용적 미만으로 줄이지는 않도록 한다.
//            resize(Math.max(DEFAULT_CAPACITY, array.size / 2))
//        }

        return array!![front]
    }

    override fun peek(): Byte? {
        // 요소가 없을 경우 null 반환

        // 요소가 없을 경우 null 반환
        return if (size == 0) {
            null
        } else array[(front + 1) % array.size] as Byte

    }

    fun is_size(): Int {
        return size
    }

    fun clear() {
        for (i in 0 until array.size) {
            array[i] = 0
        }
        size = 0
        rear = 0
        front = 0
    }
}
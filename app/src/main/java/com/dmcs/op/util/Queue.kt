package com.dmcs.baseproject.util

/**
 *
 * 자바 Queue Interface입니다.
 *
 * Queue는 com.dmcs.op.util.ArrayQueue, LinkedQueue,
 * Deque, PriorityQueue 에 의해 구현됩니다.
 *
 * @author st_lab
 * @param  the type of elements in this Que
 *
 * @version 1.0
 */
interface Queue {
    /**
     * 큐의 가장 마지막에 요소를 추가합니다.
     *
     * @param e 큐에 추가할 요소
     * @return 큐에 요소가 정상적으로 추가되었을 경우 true를 반환
     */
    fun offer(e: Any?): Boolean

    /**
     * 큐의 첫 번째 요소를 삭제하고 삭제 된 요소를 반환합니다.
     *
     * @return 큐의 삭제 된 요소 반환
     */
    fun poll(): Any?

    /**
     * 큐의 첫 번째 요소를 반환합니다.
     *
     * @return 큐의 첫 번째 요소 반환
     */
    fun peek(): Any?
}
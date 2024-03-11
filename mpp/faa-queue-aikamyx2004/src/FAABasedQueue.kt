import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.AtomicReferenceArray

/**
 * @author Mukhtarov Ainur
 *
 * TODO: Copy the code from `FAABasedQueueSimplified`
 * TODO: and implement the infinite array on a linked list
 * TODO: of fixed-size `Segment`s.
 */
class FAABasedQueue<E> : Queue<E> {
    private var head = AtomicReference(Segment(0))
    private var tail = head
    private val enqIdx = AtomicLong(0)
    private val deqIdx = AtomicLong(0)

    override fun enqueue(element: E) {
        while (true) {
            val curTail = tail.get()
            val i = enqIdx.getAndIncrement()
            val s = findSegment(start = curTail, id = i / SEGMENT_SIZE)
            if (s.cells.compareAndSet(i.toInt() % SEGMENT_SIZE, null, element)) {
                return
            }
        }
    }

    private fun moveTailForward(s: Segment) {
        while (true) {
            val curTail = tail.get()
            val next = curTail.next.get()
            if (curTail.id >= s.id) break
            tail.compareAndSet(curTail, next)
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun dequeue(): E? {
        while (true) {
            if (!shouldTryToDequeue()) {
                return null
            }
            val curHead = head.get()
            val i = deqIdx.getAndIncrement()
            val s = findSegment(start = curHead, id = i / SEGMENT_SIZE)

            if (s.cells.compareAndSet(i.toInt() % SEGMENT_SIZE, null, POISONED)) {
                continue
            }
            return s.cells.getAndSet(i.toInt() % SEGMENT_SIZE, POISONED) as E
        }
    }


    private fun moveHeadForward(s: Segment) {
        while (true) {
            val curHead = head.get()
            val next = curHead.next.get()
            if (curHead.id >= s.id) break
            head.compareAndSet(curHead, next)
        }
    }

    private fun shouldTryToDequeue(): Boolean {
        while (true) {
            val curDeqIdx = deqIdx.get()
            val curEnqIdx = enqIdx.get()
            if (curDeqIdx != deqIdx.get())
                continue
            return curDeqIdx < curEnqIdx
        }
    }


    private fun findSegment(start: Segment, id: Long): Segment {
        var s = start
        while (s.id < id) {
            s.next.compareAndSet(null, Segment(s.id + 1))
            s = s.next.get()!!
        }
        return s
    }
}

private class Segment(val id: Long) {
    val next = AtomicReference<Segment?>(null)
    val cells = AtomicReferenceArray<Any?>(SEGMENT_SIZE)
}

// DO NOT CHANGE THIS CONSTANT
private const val SEGMENT_SIZE = 2
private val POISONED = Any()
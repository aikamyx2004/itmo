import java.util.concurrent.atomic.AtomicReference

/**
@author Mukhtarov Ainur
 */
class MSQueue<E> : Queue<E> {
    private val head: AtomicReference<Node<E>>
    private val tail: AtomicReference<Node<E>>

    init {
        val dummy = Node<E>(null)
        head = AtomicReference(dummy)
        tail = AtomicReference(dummy)
    }

    override fun enqueue(element: E) {
        while (true) {
            val node = Node(element)
            val curTail = tail.get()
            if (curTail.next.compareAndSet(null, node)) {
                tail.compareAndSet(curTail, node);
                return
            } else {
                tail.compareAndSet(curTail, curTail.next.get());
            }
        }
    }

    override fun dequeue(): E? {
        while (true) {
            val curHead = head.get()
            val curHeadNext = curHead.next.get() ?: return null

            if (head.compareAndSet(curHead, curHeadNext)) {
                val res = curHeadNext.element
                curHeadNext.element = null
                return res
            }
        }
    }

    // FOR TEST PURPOSE, DO NOT CHANGE IT.
    override fun validate() {
        check(tail.get().next.get() == null) {
            "At the end of the execution, `tail.next` must be `null`"
        }
        check(head.get().element == null) {
            "At the end of the execution, the dummy node shouldn't store an element"
        }
    }

    private class Node<E>(
        var element: E?
    ) {
        val next = AtomicReference<Node<E>?>(null)
    }
}

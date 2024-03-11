import java.util.concurrent.atomic.AtomicReference

/**
 * @author Mukhtarov Ainur
 */
class Solution(private val env: Environment) : Lock<Solution.Node> {
    private val tail: AtomicReference<Node> = AtomicReference(null)
    override fun lock(): Node {
        val my = Node()

        val pred = tail.getAndSet(my)
        if (pred != null) {
            pred.next.set(my)
            while (my.locked.get()) {
                env.park()
            }
        }

        return my
    }

    override fun unlock(node: Node) {
        if (node.next.get() == null) {
            if (tail.compareAndSet(node, null)) {
                return
            } else {
                while (node.next.get() == null) {
                    env.park()
                }
            }
        }
        node.next.get().locked.set(false)
        env.unpark(node.next.get().thread)
    }

    class Node {
        val thread = Thread.currentThread()
        val locked = AtomicReference(true)
        val next: AtomicReference<Node> = AtomicReference(null)
    }
}
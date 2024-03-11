package dijkstra

import java.util.*
import java.util.concurrent.Phaser
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

private val NODE_DISTANCE_COMPARATOR = Comparator<Node> { o1, o2 -> Integer.compare(o1!!.distance, o2!!.distance) }

// Returns `Integer.MAX_VALUE` if a path has not been found.
fun shortestPathParallel(start: Node) {
    val workers = Runtime.getRuntime().availableProcessors()
    // The distance to the start node is `0`
    start.distance = 0
    val q = MQueues(workers, NODE_DISTANCE_COMPARATOR) // TODO replace me with a multi-queue based PQ!
    q.add(start);
    val active = AtomicInteger(1);
    // Run worker threads and wait until the total work is done
    val onFinish = Phaser(workers + 1) // `arrive()` should be invoked at the end by each worker
    repeat(workers) {
        thread {
            threadRun(active, q)
            onFinish.arrive()
        }
    }
    onFinish.arriveAndAwaitAdvance()
}

private fun threadRun(active: AtomicInteger, q: MQueues<Node>) {
    while (active.get() > 0) {
        val u: Node = q.delete() ?: continue
        for (e in u.outgoingEdges) {
            while (true) {
                val d = u.distance + e.weight;
                val v = e.to;
                val vDist = v.distance
                if (d >= vDist) {
                    break
                }
                if (v.casDistance(vDist, d)) {
                    q.add(v)
                    active.getAndIncrement()
                    break
                }
            }
        }
        active.getAndDecrement()

    }
}

class MQueues<E>(workers: Int, nodeDistanceComparator: Comparator<E>) {
    private val length: Int = 4 * workers
    private val queueLock: Array<ReentrantLock> = Array(length) { ReentrantLock() }
    private val comparator = nodeDistanceComparator;

    private val queues: Array<PriorityQueue<E>> = Array(length) {
        PriorityQueue(nodeDistanceComparator)
    }

    fun add(node: E) {
        while (true) {
            val index = randomCellIndex();
            if (!queueLock[index].tryLock()) {
                continue
            }
            val q = queues[index];
            q.add(node);
            queueLock[index].unlock();
            return
        }
    }

    fun delete(): E? {
        while (true) {
            val index1 = randomCellIndex();
            val index2 = randomCellIndex();
            if (index1 >= index2) continue
            var ind = index1;
            val n1 = queues[index1].peek()
            val n2 = queues[index2].peek()
            if (n1 == null || (n2 != null && comparator.compare(n1, n2) > 0)) {
                ind = index2;
            }
            if (!queueLock[ind].tryLock()) {
                continue
            }
            val res = queues[ind].poll()
            queueLock[ind].unlock();
            return res;
        }
    }

    private fun randomCellIndex(): Int =
        ThreadLocalRandom.current().nextInt(length)
}

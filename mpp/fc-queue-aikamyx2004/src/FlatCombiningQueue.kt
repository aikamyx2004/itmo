import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReferenceArray

class FlatCombiningQueue<E> : Queue<E> {
    private val queue = ArrayDeque<E>() // sequential queue
    private val combinerLock = AtomicBoolean(false) // unlocked initially
    private val tasksForCombiner = AtomicReferenceArray<Any?>(TASKS_FOR_COMBINER_SIZE)

    override fun enqueue(element: E) {
        // TODO: Make this code thread-safe using the flat-combining technique.
        // TODO: 1.  Try to become a combiner by
        // TODO:     changing `combinerLock` from `false` (unlocked) to `true` (locked).
        // TODO: 2a. On success, apply this operation and help others by traversing
        // TODO:     `tasksForCombiner`, performing the announced operations, and
        // TODO:      updating the corresponding cells to `Result`.
        // TODO: 2b. If the lock is already acquired, announce this operation in
        // TODO:     `tasksForCombiner` by replacing a random cell state from
        // TODO:      `null` with the element. Wait until either the cell state
        // TODO:      updates to `Result` (do not forget to clean it in this case),
        // TODO:      or `combinerLock` becomes available to acquire.
        var used = false;
        val index = randomCellIndex();
        while (true) {
            if (used && tasksForCombiner.get(index) != element) {
                tasksForCombiner.set(index, null);
                return;
            }
            if (combinerLock.compareAndSet(false, true)) {
                if (!used) {
                    queue.addLast(element)
                }
                doTasks(index);
                combinerLock.compareAndSet(true, false);
                return
            }
            if (!used) {
                if (tasksForCombiner.compareAndSet(index, null, element)) {
                    used = true;
                }
            }
            if (tasksForCombiner.compareAndSet(index, Result(element), null)) {
                return;
            }
        }

    }

    private fun doTasks(index: Int): E? {
        var res: E? = null;
        for (e in 0 until TASKS_FOR_COMBINER_SIZE) {
            val task = tasksForCombiner.get(e);
            if (task is Result<*>) {
                if (e == index) {
                    res = task.value as E?;
                }
                continue
            }
            if (task is Dequeue) {
                val curRes = queue.removeFirstOrNull();
                if (tasksForCombiner.compareAndSet(e, task, Result(curRes))) {
                    if (e == index && curRes != null)
                        res = curRes
                }
                continue
            }
            if (task == null) {
                continue
            }
            // add operation

            val elem = task as E;
            if (tasksForCombiner.compareAndSet(e, elem, Result(elem))) {
                queue.addLast(elem)
            }
        }
        return res;
    }

    override fun dequeue(): E? {
        // TODO: Make this code thread-safe using the flat-combining technique.
        // TODO: 1.  Try to become a combiner by
        // TODO:     changing `combinerLock` from `false` (unlocked) to `true` (locked).
        // TODO: 2a. On success, apply this operation and help others by traversing
        // TODO:     `tasksForCombiner`, performing the announced operations, and
        // TODO:      updating the corresponding cells to `Result`.
        // TODO: 2b. If the lock is already acquired, announce this operation in
        // TODO:     `tasksForCombiner` by replacing a random cell state from
        // TODO:      `null` with `Dequeue`. Wait until either the cell state
        // TODO:      updates to `Result` (do not forget to clean it in this case),
        // TODO:      or `combinerLock` becomes available to acquire.
        var used = false;
        val index = randomCellIndex();
        while (true) {
            if (used && tasksForCombiner.get(index) != Dequeue) {
                val res = tasksForCombiner.getAndSet(index, null) as Result<E>;
                return res.value;
            }
            if (combinerLock.compareAndSet(false, true)) {
                var res: E?;
                res = doTasks(index);
                if (!used) {
                    res = queue.removeFirstOrNull()
                }
                combinerLock.compareAndSet(true, false)
                return res;
            }
            if (!used) {
                if (tasksForCombiner.compareAndSet(index, null, Dequeue)) {
                    used = true;
                }
            }
            val res = tasksForCombiner.get(index);
            if (res == null || res is Dequeue) continue
            if (used && res is Result<*>) {
                if (tasksForCombiner.compareAndSet(index, res, null))
                    assert(res.value != Dequeue);
                return res.value as E?;
            }

        }
    }

    private fun randomCellIndex(): Int =
        ThreadLocalRandom.current().nextInt(tasksForCombiner.length())
}

private const val TASKS_FOR_COMBINER_SIZE = 3 // Do not change this constant!
private const val REPEAT_CYCLE = 3;

// TODO: Put this token in `tasksForCombiner` for dequeue().
// TODO: enqueue()-s should put the inserting element.
private object Dequeue

// TODO: Put the result wrapped with `Result` when the operation in `tasksForCombiner` is processed.
private class Result<V>(
    val value: V
)
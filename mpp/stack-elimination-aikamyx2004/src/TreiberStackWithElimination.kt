import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicReferenceArray

/**
 * @author Mukhtarov Ainur
 */
open class TreiberStackWithElimination<E> : Stack<E> {
    private val stack = TreiberStack<E>()

    private val eliminationArray = AtomicReferenceArray<Any?>(ELIMINATION_ARRAY_SIZE)

    override fun push(element: E) {
        if (tryPushElimination(element)) return
        stack.push(element)
    }

    protected open fun tryPushElimination(element: E): Boolean {
        val index = randomCellIndex()
        if (!eliminationArray.compareAndSet(index, CELL_STATE_EMPTY, element)) {
            return false
        }
        repeat(ELIMINATION_WAIT_CYCLES) {
            if (eliminationArray.compareAndSet(index, CELL_STATE_RETRIEVED, CELL_STATE_EMPTY)) {
                return true
            }
        }
        return !eliminationArray.compareAndSet(index, element, CELL_STATE_EMPTY)
    }

    override fun pop(): E? = tryPopElimination() ?: stack.pop()

    private fun tryPopElimination(): E? {
        val index = randomCellIndex()
        eliminationArray.compareAndSet(index, CELL_STATE_RETRIEVED, CELL_STATE_EMPTY);
        val expr = eliminationArray[index] ?: return null;
        if (!eliminationArray.compareAndSet(index, expr, CELL_STATE_RETRIEVED)) {
            return null;
        }
        if (expr == CELL_STATE_RETRIEVED) {
            return null;
        }
        return expr as E;
    }

    private fun randomCellIndex(): Int =
        ThreadLocalRandom.current().nextInt(eliminationArray.length())

    companion object {
        private const val ELIMINATION_ARRAY_SIZE = 2 // Do not change!
        private const val ELIMINATION_WAIT_CYCLES = 1 // Do not change!

        // Initially, all cells are in EMPTY state.
        private val CELL_STATE_EMPTY = null

        // `tryPopElimination()` moves the cell state
        // to `RETRIEVED` if the cell contains element.
        private val CELL_STATE_RETRIEVED = Any()
    }
}

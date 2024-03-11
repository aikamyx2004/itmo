import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Bank implementation.
 *
 *
 * @author Mukhtarov Ainur
 */
class BankImpl(n: Int) : Bank {
    private val accounts: Array<Account> = Array(n) { Account() }

    override val numberOfAccounts: Int
        get() = accounts.size

    override fun getAmount(index: Int): Long {
        var amount: Long
        accounts[index].lock
            .withLock {
                amount = accounts[index].amount
            }
        return amount
    }

    override val totalAmount: Long
        get() {
            for (i in accounts.indices) {
                accounts[i].lock.lock()
            }
            val totalAmount: Long = accounts.sumOf { account ->
                account.amount
            }

            for (i in accounts.indices) {
                accounts[i].lock.unlock()
            }

            return totalAmount
        }

    override fun deposit(index: Int, amount: Long): Long {
        require(amount > 0) { "Invalid amount: $amount" }
        val accAmount: Long
        accounts[index].lock
            .withLock {
                val account = accounts[index]
                check(!(amount > Bank.MAX_AMOUNT || account.amount + amount > Bank.MAX_AMOUNT)) { "Overflow" }
                account.amount += amount
                accAmount = account.amount
            }
        return accAmount
    }

    override fun withdraw(index: Int, amount: Long): Long {
        require(amount > 0) { "Invalid amount: $amount" }
        val accAmount: Long
        accounts[index].lock
            .withLock {
                val account = accounts[index]
                check(account.amount - amount >= 0) { "Underflow" }
                account.amount -= amount
                accAmount = account.amount
            }
        return accAmount
    }

    override fun transfer(fromIndex: Int, toIndex: Int, amount: Long) {
        require(amount > 0) { "Invalid amount: $amount" }
        require(fromIndex != toIndex) { "fromIndex == toIndex" }
        var lock1: ReentrantLock = accounts[fromIndex].lock
        var lock2: ReentrantLock = accounts[toIndex].lock
        if (toIndex < fromIndex) {
            lock1 = accounts[toIndex].lock
            lock2 = accounts[fromIndex].lock
        }
        lock1.withLock {
            lock2.withLock {
                val from = accounts[fromIndex]
                val to = accounts[toIndex]
                check(amount <= from.amount) { "Underflow" }
                check(!(amount > Bank.MAX_AMOUNT || to.amount + amount > Bank.MAX_AMOUNT)) { "Overflow" }
                from.amount -= amount
                to.amount += amount
            }
        }
    }

    /**
     * Private account data structure.
     */
    class Account {
        /**
         * Amount of funds in this account.
         */
        var amount: Long = 0
        val lock: ReentrantLock = ReentrantLock()
    }
}
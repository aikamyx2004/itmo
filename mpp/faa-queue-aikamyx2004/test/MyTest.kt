import kotlin.test.Test

class MyTest {
    @Test
    fun test(){
        var t = FAABasedQueue<Int>()
        t.enqueue(1);
        t.dequeue()
    }
}
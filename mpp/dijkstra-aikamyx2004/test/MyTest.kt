package dijkstra

import kotlin.test.Test

class MyTest {
//    @Test
//    fun testQueue() {
//        var a = MQueues<Int>(2, Integer::compare);
//        a.add(1);
//        assert(a.delete() == 1);
//        a.add(2);
//        assert(a.delete() == 2);
//    }

    @Test
    fun testDijkstra() {
        var g = randomConnectedGraph(2, 1, 2)
        shortestPathParallel(g[0]);
        if (g[0].outgoingEdges.size != 0)
            assert(g[1].distance == g[0].outgoingEdges[0].weight)
        print(1);
    }
}
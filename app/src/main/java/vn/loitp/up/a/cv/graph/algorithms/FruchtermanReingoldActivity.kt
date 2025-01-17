package vn.loitp.up.a.cv.graph.algorithms

import dev.bandb.graphview.decoration.edge.ArrowEdgeDecoration
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.energy.FruchtermanReingoldLayoutManager
import vn.loitp.up.a.cv.graph.GraphActivity

class FruchtermanReingoldActivity : GraphActivity() {

    public override fun setLayoutManager() {
        binding.recycler.layoutManager = FruchtermanReingoldLayoutManager(this, 1000)
    }

    public override fun setEdgeDecoration() {
        binding.recycler.addItemDecoration(ArrowEdgeDecoration())
    }

    public override fun createGraph(): Graph {
        val graph = Graph()
        val a = Node(nodeText)
        val b = Node(nodeText)
        val c = Node(nodeText)
        val d = Node(nodeText)
        val e = Node(nodeText)
        val f = Node(nodeText)
        val g = Node(nodeText)
        val h = Node(nodeText)
        graph.addEdge(a, b)
        graph.addEdge(a, c)
        graph.addEdge(a, d)
        graph.addEdge(c, e)
        graph.addEdge(d, f)
        graph.addEdge(f, c)
        graph.addEdge(g, c)
        graph.addEdge(h, g)
        return graph
    }
}

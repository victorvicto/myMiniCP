/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * aint with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2017. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.util;

import java.util.Arrays;
import java.util.Stack;
import java.util.function.BiConsumer;

// class written by Guillaume Derval
public class GraphUtil {
    public static interface Graph {
        /**
         * @return the number of nodes in this graph. They are indexed from 0 to n-1.
         */
        int n();

        /**
         * @param idx the node to consider
         * @return the nodes ids that have an edge going from then to node idx
         */
        Iterable<Integer> in(int idx);

        /**
         * @param idx the node to consider
         * @return the nodes ids that have an edge going from node idx to them.
         */
        Iterable<Integer> out(int idx);
    }

    /**
     * Transpose the graph
     *
     * @param graph
     * @return
     */
    public static Graph transpose(Graph graph) {
        return new Graph() {
            @Override
            public int n() {
                return graph.n();
            }

            @Override
            public Iterable<Integer> in(int idx) {
                return graph.out(idx);
            }

            @Override
            public Iterable<Integer> out(int idx) {
                return graph.in(idx);
            }
        };
    }

    /**
     * Returns the SCC of the graph
     * For at each index, an integer representing the scc id of the node
     */
    public static int[] stronglyConnectedComponents(Graph graph) {
        //Compute the suffix order
        Stack<Integer> firstOrder = new Stack<>();
        int[] visited = new int[graph.n()];
        Arrays.fill(visited, 0);
        for (int i = 0; i < graph.n(); i++) {
            if (visited[i] == 0) {
                dfsNode(graph, (suffix, b) -> {if(suffix) firstOrder.push(b);}, visited, i);
            }
        }

        //Reverse the order, and do the dfs of the transposed graph
        Arrays.fill(visited, 0);
        int [] scc = new int[graph.n()];
        Counter cpt = new Counter();
        Graph tranposed = GraphUtil.transpose(graph);

        while (!firstOrder.empty()) {
            int next = firstOrder.pop();
            if(visited[next] == 0) {
                cpt.incr();
                dfsNode(tranposed, (suffix, x) -> {if(!suffix) scc[x] = cpt.getValue();}, visited, next);
            }
        }
        return scc;
    }

    private static void dfsNode(Graph graph, BiConsumer<Boolean, Integer> action, int[] visited, int start) {
        Stack<Integer> todo = new Stack<>();
        todo.add(start);

        // seen = 1
        // visited = 2
        // closed = 3
        visited[start] = 1; //seen
        while (!todo.isEmpty()) {
            int cur = todo.peek();
            if(visited[cur] == 1) {
                action.accept(false, cur);
                for (int next : graph.out(cur)) {
                    if (visited[next] == 0) {
                        todo.add(next);
                        visited[next] = 1; //seen
                    }
                }
                visited[cur] = 2; //visited
            }
            else if(visited[cur] == 2) {
                action.accept(true, cur);
                visited[cur] = 3; //closed
                todo.pop();
            }
        }
    }
}

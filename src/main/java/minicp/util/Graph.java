package minicp.util;

import minicp.engine.core.IntVar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph extends GraphUtil{
    private int n;
    private List<Set<Integer>> in;
    private List<Set<Integer>> out;

    public Graph () {
        n = 0;
    }

    public void MakeGraph (int[] matched, IntVar[] x,int minVal, int maxVal) {
        int baselen = matched.length;
        int maxDomain = maxVal-minVal;
        this.n = baselen + maxDomain + 1;
        this.in = new ArrayList<Set<Integer>>();
        this.out = new ArrayList<Set<Integer>>();
        for (int k=0; k<n; k++) {
            in.add(new HashSet<Integer>());
            out.add(new HashSet<Integer>());
        }
        for (int i=0; i<x.length; i++) {
            for (int xVal : x[i].getValues()) {
                if (matched[i]==xVal) {
                    in.get(i).add(xVal+baselen-minVal);
                    out.get(xVal+baselen-minVal).add(i);
                } else {
                    out.get(i).add(xVal+baselen-minVal);
                    in.get(xVal+baselen-minVal).add(i);
                }
            }
        }
        for (int i = baselen; i<baselen+maxDomain; i++) {
            if (out.get(i).isEmpty()) {
                in.get(baselen+maxDomain).add(i);
                out.get(i).add(baselen+maxDomain);
            } else {
                out.get(baselen+maxDomain).add(i);
                in.get(i).add(baselen+maxDomain);
            }
        }
    }

    /**
     * @return the number of nodes in this graph. They are indexed from 0 to n-1.
     */
    int n() {
        return n;
    }

    /**
     * @param idx the node to consider
     * @return the nodes ids that have an edge going from then to node idx
     */
    Iterable<Integer> in(int idx) {
        return in.get(idx);
    }

    /**
     * @param idx the node to consider
     * @return the nodes ids that have an edge going from node idx to them.
     */
    Iterable<Integer> out(int idx) {
        return out.get(idx);
    }
}
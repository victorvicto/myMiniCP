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
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2017. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.engine.constraints;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.Graph;
import minicp.util.GraphUtil;
import minicp.util.GraphUtil.*;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of the algorithm described in
 * "A filtering algorithm for constraints of difference in CSPs" J-C. Régin, AAAI-94
 * Hint: use MaximumMatching and GraphUtil.stronglyConnectedComponents
 */
public class AllDifferentAC extends Constraint {

    private IntVar[] x;
    private MaximumMatching mm;
    private int baselen;
    private int minVal;
    private int maxVal;

    public AllDifferentAC(IntVar... x) {
        super(x[0].getSolver());
        this.x = x;
        mm = new MaximumMatching(this.x);
    }

    @Override
    public void post() throws InconsistencyException {
        baselen = x.length;
        minVal = Integer.MAX_VALUE;
        maxVal = Integer.MIN_VALUE;
        for (IntVar xi : x) {
            if (minVal>xi.getMin())
                minVal = xi.getMin();
            if (maxVal<xi.getMax())
                maxVal = xi.getMax();
        }
        for (IntVar xi : x) {
            xi.propagateOnBind(this);
        }
        propagate();
    }



    @Override
    public void propagate() throws InconsistencyException {
        int[] maxMatch = new int[x.length];
        mm.compute(maxMatch);
        Graph g = new Graph();
        g.MakeGraph(maxMatch, x, minVal, maxVal);
        int[] scc = GraphUtil.stronglyConnectedComponents(g);
        for (int i=0; i<x.length; i++) {
            for (int xVal : x[i].getValues()) {
                if (maxMatch[i]!=xVal) {
                    if (scc[i]!=scc[xVal+baselen-minVal]) {
                        x[i].remove(xVal);
                    }
                }
            }
        }
    }
}

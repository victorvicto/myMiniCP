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

package minicp.examples;

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import minicp.util.InputReader;

import java.util.*;

import static minicp.cp.Factory.*;
import static minicp.cp.Heuristics.firstFail;
import static minicp.search.Selector.branch;
import static minicp.search.Selector.selectMax;
import static minicp.search.Selector.selectMin;

public class QAP {

    public static class Pair {
        public IntVar i;
        public IntVar j;
        public int weight;

        public Pair(IntVar i, IntVar j, int weight) {
            this.i = i;
            this.j = j;
            this.weight = weight;
        }

        public int getSize() {
            return i.getSize();
        }

        public int getWeight() {
            return  weight;
        }

        /*public int getMin() {
            int [] ival = i.getValues();
            int [] jval = j.getValues();
            int min = 2147483647;
            int minVal = ival[0];
            for (int ii : ival) {
                for (int jj : jval) {
                    if(dist[ii][jj]<min) {
                        min = dist[ii][jj];
                        minVal = ii;
                    }
                }
            }
            return minVal;
        }*/
    }

    public static void main(String[] args) throws InconsistencyException {

        // ---- read the instance -----

        InputReader reader = new InputReader("data/qap.txt");

        int n = reader.getInt();
        // Weights
        int [][] w = new int[n][n];
        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++) {
                w[i][j] = reader.getInt();
            }
        }
        // Distance
        int [][] d = new int[n][n];
        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++) {
                d[i][j] = reader.getInt();
            }
        }

        // ----- build the model ---

        Solver cp = makeSolver();
        IntVar[] x = makeIntVarArray(cp, n, n);

        cp.post(allDifferent(x));

        List<Pair> listOfPairs = new ArrayList<Pair>();
        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++) {
                Pair p = new Pair(x[i],x[j],w[i][j]);
                listOfPairs.add(p);
            }
        }

        Pair [] pp = listOfPairs.toArray(new Pair[n*n]);

        // DFSearch dfs = makeDfs(cp,firstFail(x));
        DFSearch dfs = makeDfs(cp,
                selectMax(pp,
                        y -> y.getSize() > 1, // filter
                        y -> y.getWeight(), // variable selector
                        xi -> {
                            int [] ival = xi.i.getValues();
                            int [] jval = xi.j.getValues();
                            int min = 2147483647;
                            int minVal = ival[0];
                            for (int ii : ival) {
                                for (int jj : jval) {
                                    if(d[ii][jj]<min) {
                                        min = d[ii][jj];
                                        minVal = ii;
                                    }
                                }
                            }
                            int v = minVal;
                            return branch(() -> equal(xi.i,v),
                                    () -> notEqual(xi.i,v));
                        }
                ));


        // build the objective function
        IntVar[] weightedDist = new IntVar[n*n];
        int ind = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                weightedDist[ind] = mul(element(d,x[i],x[j]),w[i][j]);
                ind++;
            }
        }
        IntVar objective = sum(weightedDist);
        cp.post(minimize(objective,dfs));



        dfs.onSolution(() -> System.out.println("objective:"+objective.getMin()));

        SearchStatistics stats = dfs.start();

        System.out.println(stats);

    }
}
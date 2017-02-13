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
import minicp.util.InconsistencyException;
import minicp.search.SearchStatistics;

import java.util.Arrays;
import java.util.stream.IntStream;

import static minicp.search.Selector.*;
import static minicp.cp.Factory.*;

public class NQueens {

    public static void main(String[] args) throws InconsistencyException {

        int n = 8;
        Solver cp = makeSolver();

        IntVar[] q = makeIntVarArray(cp, n, n);
        IntVar[] ql = makeIntVarArray(cp,n,i -> plus(q[i],i));
        IntVar[] qr = makeIntVarArray(cp,n,i -> plus(q[i],-i));
        //IntVar [] ql = IntStream.range(0,n).mapToObj(i -> plus(q[i], i)).toArray(IntVar[]::new);
        //IntVar [] qr = IntStream.range(0,n).mapToObj(i -> plus(q[i],-i)).toArray(IntVar[]::new);

        cp.post(allDifferent(q));
        cp.post(allDifferent(ql));
        cp.post(allDifferent(qr));

        SearchStatistics stats = makeDfs(cp,
                selectMin(q,
                        qi -> qi.getSize() > 1,
                        qi -> qi.getSize(),
                        qi -> {
                            int v = qi.getMin();
                            return branch(
                                    () -> {
                                        equal(qi,v);
                                    },
                                    () -> {
                                        notEqual(qi,v);
                                    }
                            );
                        }
                )
        ).onSolution(() ->
                System.out.println("solution:"+ Arrays.toString(q))
        ).start();

        System.out.format("#Solutions: %s\n", stats.nSolutions);
        System.out.format("Statistics: %s\n", stats);

    }
}
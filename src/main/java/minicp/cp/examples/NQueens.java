/*
 * This file is part of mini-cp.
 *
 * mini-cp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mini-cp.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2016 L. Michel, P. Schaus, P. Van Hentenryck
 */

package minicp.cp.examples;

import minicp.cp.core.IntVar;
import minicp.cp.core.Solver;
import minicp.util.InconsistencyException;
import minicp.search.SearchStatistics;

import java.util.Arrays;
import java.util.stream.IntStream;

import static minicp.search.Selector.*;
import static minicp.cp.Factory.*;

public class NQueens {

    public static void main(String[] args) throws InconsistencyException {
        Solver cp = makeSolver();
        int n = 8;

        IntVar[] q = makeIntVarArray(cp, n, n);
        IntVar [] ql = IntStream.range(0,n).mapToObj(i -> plus(q[i],i)).toArray(IntVar[]::new);
        IntVar [] qr = IntStream.range(0,n).mapToObj(i -> minus(q[i],i)).toArray(IntVar[]::new);

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
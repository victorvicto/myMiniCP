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

import minicp.cp.constraints.Equal;
import minicp.cp.constraints.NotEqual;
import minicp.cp.core.IntVar;
import minicp.cp.core.Solver;
import minicp.util.InconsistencyException;
import minicp.util.Box;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;

import java.util.Arrays;

import static minicp.search.Selector.*;
import static minicp.cp.Factory.*;

public class NQueens {

    public static void main(String[] args) throws InconsistencyException {
        Solver cp = makeSolver();
        int n = 8;
        IntVar[] q = makeIntVarArray(cp, n, n);

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                cp.post(notEqual(q[i], q[j]));
                cp.post(notEqual(q[i], q[j], i - j));
                cp.post(notEqual(q[i], q[j], j - i));
            }

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
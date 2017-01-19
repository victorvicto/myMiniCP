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


import minicp.cp.constraints.AllDifferentBinary;
import minicp.cp.constraints.Sum;
import minicp.cp.core.IntVar;
import minicp.cp.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import static minicp.cp.Factory.*;

import java.util.Arrays;

import static minicp.cp.core.Heuristics.*;

public class MagicSquare {

    // https://en.wikipedia.org/wiki/Magic_square
    public static void main(String[] args) throws InconsistencyException {

        int n = 6;
        int M = n * (n * n + 1) / 2;

        Solver cp = new Solver();
        IntVar[][] x = new IntVar[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                x[i][j] = makeIntVar(cp, 1, n*n);
            }
        }


        IntVar [] xFlat = new IntVar[x.length * x.length];
        for (int i = 0; i < x.length; i++) {
            System.arraycopy(x[i],0,xFlat,i * x.length,x.length);
        }


        // AllDifferent
        cp.add(new AllDifferentBinary(xFlat));

        // Sum on lines
        for (int i = 0; i < n; i++) {
            cp.add(new Sum(x[i],M));
        }

        // Sum on columns
        for (int j = 0; j < x.length; j++) {
            IntVar[] column = new IntVar[n];
            for (int i = 0; i < x.length; i++)
                column[i] = x[i][j];
            cp.add(new Sum(column,M));
        }

        // Sum on diagonals
        IntVar[] diagonalLeft = new IntVar[n];
        IntVar[] diagonalRight = new IntVar[n];
        for (int i = 0; i < x.length; i++) {
            diagonalLeft[i] = x[i][i];
            diagonalRight[i] = x[n-i-1][i];
        }
        cp.add(new Sum(diagonalLeft, M));
        cp.add(new Sum(diagonalRight, M));



        DFSearch dfs = new DFSearch(cp.getState(),firstFail(xFlat));
        dfs.onSolution(() -> {
                    for (int i = 0; i < n; i++) {
                        System.out.println(Arrays.toString(x[i]));
                    }
                }
        );

        SearchStatistics stats = dfs.start(stat -> stat.nSolutions >= 1); // stop on first solution

        System.out.println(stats);
    }

}

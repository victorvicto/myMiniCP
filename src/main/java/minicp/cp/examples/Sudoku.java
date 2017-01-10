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
import minicp.cp.constraints.EqualVal;
import minicp.cp.constraints.DifferentVal;
import minicp.cp.core.IntVar;
import minicp.cp.core.Solver;
import minicp.cp.core.Status;
import minicp.search.Branching;
import minicp.search.DFSearch;
import minicp.search.Choice;

import java.util.Optional;
import java.util.Random;
import java.util.Arrays;

public class Sudoku {

    public static void main(String[] args) {

        int[][] puzzle =
                       {{4,2,3,5,6,7,8,0,1},
                        {5,6,1,0,8,4,2,3,7},
                        {0,8,7,2,3,1,4,5,6},
                        {7,4,8,6,5,0,3,1,2},
                        {3,1,5,7,4,2,6,8,0},
                        {6,0,2,8,1,3,7,4,5},
                        {8,5,0,4,2,6,1,7,3},
                        {1,7,6,3,0,8,5,2,4},
                        {2,3,4,1,7,5,0,6,8}};


        Random rand = new Random(0);

        Solver cp = new Solver();

        IntVar [][] x = new IntVar[9][9];

        try {

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    x[i][j] = new IntVar(cp, 9);
                    if (rand.nextInt() < 95) {
                        cp.add(new EqualVal(x[i][j], puzzle[i][j]));
                    }
                }
            }

            // allDifferent on a line
            for (IntVar[] line : x) {
                cp.add(new AllDifferentBinary(line));
            }


            // allDifferent on a column
            for (int j = 0; j < x.length; j++) {
                IntVar[] column = new IntVar[9];
                for (int i = 0; i < x.length; i++)
                    column[i] = x[i][j];
                cp.add(new AllDifferentBinary(column));
            }

            // allDifferent on a 3x3 block

        } catch (Status e) {
            System.out.println("inconsistency detected in the model");
            return;
        }


        IntVar [] xFlat = new IntVar[x.length * x.length];
        for (int i = 0; i < x.length; i++) {
            System.arraycopy(x[i],0,xFlat,i * x.length,x.length);
        }

        // count the number of solution
        int [] nSols = new int[1];
        cp.onSolution(() -> {
            nSols[0] += 1;
        });

        Choice myBranching = ()  -> {
                Optional<IntVar> unBoundVarOpt = Arrays.stream(xFlat).filter(var -> !var.isBound()).findFirst();
                if (!unBoundVarOpt.isPresent()) {
                    return Branching.EMPTY;
                } else {
                    IntVar unBoundVar = unBoundVarOpt.get();
                    int v = unBoundVar.getMin();
                    return Branching.branch(
                            () -> { // left branch
                                cp.add(new EqualVal(unBoundVar, v));
                            },
                            () -> { // right branch
                                cp.add(new DifferentVal(unBoundVar, v));
                            });
                }
            };

        DFSearch dfs = new DFSearch(cp,myBranching);


        // start the search
        dfs.start();

        System.out.println("#Solutions:"+nSols[0]);


    }

}

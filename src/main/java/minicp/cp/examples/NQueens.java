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

import minicp.cp.constraints.DifferentVal;
import minicp.cp.constraints.EqualVal;
import minicp.cp.core.IntVar;
import minicp.cp.core.Model;
import minicp.cp.constraints.DifferentVar;
import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.search.DFSearch;
import minicp.search.Inconsistency;

import java.util.Arrays;
import java.util.Optional;

public class NQueens {

    public static void main(String[] args) {
        Model cp = new Model();
        int n = 8;
        IntVar [] q = new IntVar[n];

        try {
            for (int i = 0; i < n; i++)
                q[i] = new IntVar(cp, n);

            for (int i = 0; i < n; i++)
                for (int j = i + 1; j < n; j++) {
                    cp.add(new DifferentVar(q[i], q[j]));
                    cp.add(new DifferentVar(q[i], q[j],i-j));
                    cp.add(new DifferentVar(q[i], q[j],j-i));
                }

            Branching myBranching = new Branching() {
                @Override
                public Alternative[] getAlternatives() {
                    Optional<IntVar> result = Arrays.stream(q).filter(var -> !var.isBound()).findFirst();
                    if (!result.isPresent()) {
                        return SOLUTION;
                    } else {
                        IntVar qk = result.get();
                        int v = qk.getMin();
                        return branch(
                                () -> { // left branch
                                    cp.add(new EqualVal(qk, v));
                                },
                                () -> { // right branch
                                    cp.add(new DifferentVal(qk, v));
                                });
                    }
                }
            };


            DFSearch dfs = new DFSearch(cp,myBranching);

            // count the number of solution
            int [] nSols = new int[1];
            dfs.onSolution(() -> {
                nSols[0] += 1;
            });

            // start the search
            dfs.start();

            System.out.println("#Solutions:"+nSols[0]);


        } catch(Inconsistency c) {
            System.out.println("inconsistency detected in the model");
        }
    }

}

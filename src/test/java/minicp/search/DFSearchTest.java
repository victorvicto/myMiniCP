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

package minicp.search;import minicp.reversible.ReversibleInt;
import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.search.DFSearch;
import minicp.search.DFSearchNode;
import org.junit.Test;

import java.util.Arrays;




public class DFSearchTest {

    @Test
    public void testDFS() {
        DFSearchNode node = new DFSearchNode();
        ReversibleInt i = new ReversibleInt(node,0);
        boolean [] values = new boolean[4];

        Branching myBranching = new Branching() {
            @Override
            public Alternative[] getAlternatives() {
                if (i.getValue() >= values.length)
                    return SOLUTION;
                else return branch (
                        ()-> {
                            // left branch
                            values[i.getValue()] = false;
                            i.increment();
                        },
                        ()-> {
                            // right branch
                            values[i.getValue()] = true;
                            i.increment();
                        }
                );
            }
        };

        DFSearch dfs = new DFSearch(node,myBranching);

        int [] nSols = new int[1];

        dfs.onSolution(() -> {
            System.out.println(Arrays.toString(values));
            nSols[0] += 1;
        });


        dfs.start();

        assert(nSols[0] == 16);



    }



}

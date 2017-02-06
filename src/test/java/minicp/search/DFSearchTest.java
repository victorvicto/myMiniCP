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

package minicp.search;

import minicp.cp.core.Solver;
import minicp.reversible.ReversibleInt;

import static minicp.search.Selector.*;
import minicp.util.Counter;
import minicp.util.InconsistencyException;
import org.junit.Test;


public class DFSearchTest {

    @Test
    public void testExample1() {
        Solver cp = new Solver();
        ReversibleInt i = new ReversibleInt(cp.getTrail(),0);
        int [] values = new int[3];

        Choice myBranching = () -> {
                if (i.getValue() >= values.length)
                    return TRUE;
                else return branch(
                        ()-> { // left branch
                            values[i.getValue()] = 0;
                            i.increment();
                        },
                        ()-> { // right branch
                            values[i.getValue()] = 1;
                            i.increment();
                        }
                );
            };



        DFSearch dfs = new DFSearch(cp.getTrail(),myBranching);

        dfs.onSolution(() -> {
            // System.out.println(Arrays.toString(values));
        });


        SearchStatistics stats = dfs.start();

        assert(stats.nSolutions == 8);
        assert(stats.nFailures == 0);
        assert(stats.nNodes == (8+4+2));
    }



    @Test
    public void testDFS() {
        Solver cp = new Solver();
        ReversibleInt i = new ReversibleInt(cp.getTrail(),0);
        boolean [] values = new boolean[4];

        Choice myBranching = () -> {
                if (i.getValue() >= values.length)
                    return TRUE;
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
            };


        Counter nSols = new Counter();


        DFSearch dfs = new DFSearch(cp.getTrail(),myBranching);

        dfs.onSolution(() -> {
           nSols.incr();
        });



        SearchStatistics stats = dfs.start();

        assert(nSols.getValue() == 16);
        assert(stats.nSolutions == 16);
        assert(stats.nFailures == 0);
        assert(stats.nNodes == (16+8+4+2));

    }

    @Test
    public void testDFSSearchLimit() {
        Solver cp = new Solver();
        ReversibleInt i = new ReversibleInt(cp.getTrail(),0);
        boolean [] values = new boolean[4];

        Choice myBranching = () -> {
                if (i.getValue() >= values.length) {
                    return branch(() -> {throw new InconsistencyException();});
                }
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
            };



        DFSearch dfs = new DFSearch(cp.getTrail(),myBranching);

        Counter nFails = new Counter();
        dfs.onFail(() -> {
            nFails.incr();
        });


        // stop search after 2 solutions
        SearchStatistics stats = dfs.start(stat -> stat.nFailures >= 3);

        assert(stats.nSolutions == 0);
        assert(stats.nFailures == 3);

    }



}

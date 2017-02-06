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

package minicp.cp.constraints;

import minicp.cp.core.IntVar;
import minicp.cp.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import org.junit.Test;

import static minicp.cp.Factory.makeIntVar;
import static minicp.cp.core.Heuristics.firstFail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class Element2DTest {

    @Test
    public void element2dTest1() {

        try {

            Solver cp = new Solver();
            IntVar x = makeIntVar(cp, -2, 40);
            IntVar y = makeIntVar(cp, -3, 10);
            IntVar z = makeIntVar(cp, 2, 40);

            int[][] T = new int[][]{
                    {9, 8, 7, 5, 6},
                    {9, 1, 5, 2, 8},
                    {8, 3, 1, 4, 9},
                    {9, 1, 2, 8, 6},
            };

            cp.post(new Element2D(T, x, y, z));

            assertEquals(0, x.getMin());
            assertEquals(0, y.getMin());
            assertEquals(3, x.getMax());
            assertEquals(4, y.getMax());
            assertEquals(2, z.getMin());
            assertEquals(9, z.getMax());

            z.removeAbove(7);
            cp.fixPoint();

            assertEquals(1, y.getMin());

            x.remove(0);
            cp.fixPoint();

            assertEquals(6, z.getMax());
            assertEquals(3, x.getMax());

            y.remove(4);
            cp.fixPoint();

            assertEquals(5, z.getMax());
            assertEquals(2, z.getMin());

            y.remove(2);
            cp.fixPoint();

            assertEquals(4, z.getMax());
            assertEquals(2, z.getMin());


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }

    @Test
    public void element2dTest2() {

        try {

            Solver cp = new Solver();
            IntVar x = makeIntVar(cp, -2, 40);
            IntVar y = makeIntVar(cp, -3, 10);
            IntVar z = makeIntVar(cp, -20, 40);

            int[][] T = new int[][]{
                    {9, 8, 7, 5, 6},
                    {9, 1, 5, 2, 8},
                    {8, 3, 1, 4, 9},
                    {9, 1, 2, 8, 6},
            };

            cp.post(new Element2D(T, x, y, z));

            DFSearch dfs = new DFSearch(cp.getTrail(), firstFail(x, y, z));
            dfs.onSolution(() ->
                    assertEquals(T[x.getMin()][y.getMin()], z.getMin())
            );
            SearchStatistics stats = dfs.start();

            assertEquals(20, stats.nSolutions);


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }

}

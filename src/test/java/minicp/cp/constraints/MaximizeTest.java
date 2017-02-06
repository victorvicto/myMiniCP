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
import minicp.util.NotImplementedException;
import org.junit.Test;

import static minicp.cp.Factory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static minicp.search.Selector.*;

public class MaximizeTest {

    @Test
    public void maximizeTest() {
        try {
            try {

                Solver cp = makeSolver();
                IntVar y = makeIntVar(cp, 10,20);

                IntVar[] x = new IntVar[]{y};
                DFSearch dfs = makeDfs(cp,() -> y.isBound() ? TRUE : branch(() -> equal(y,y.getMin()),() -> notEqual(y,y.getMin())));

                cp.post(maximize(y,dfs));

                SearchStatistics stats = dfs.start();

                assertEquals(stats.nSolutions,11);


            } catch (InconsistencyException e) {
                fail("should not fail");
            }
        } catch (NotImplementedException e) {
            e.print();
        }

    }






}

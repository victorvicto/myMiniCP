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
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import org.junit.Test;

import static minicp.cp.Factory.*;
import static minicp.cp.core.Heuristics.firstFail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class AllDifferentTest {

    @Test
    public void allDifferentTest1() {

        Solver cp  = makeSolver();

        IntVar [] x = makeIntVarArray(cp,5,5);

        try {
            cp.post(allDifferent(x));
            equal(x[0],0);
            for (int i = 1; i < x.length; i++) {
                assertEquals(4,x[i].getSize());
                assertEquals(1,x[i].getMin());
            }

        } catch (InconsistencyException e) {
            assert(false);
        }
    }


    @Test
    public void allDifferentTest2() {

        Solver cp  = makeSolver();

        IntVar [] x = makeIntVarArray(cp,5,5);

        try {
            cp.post(allDifferent(x));

            SearchStatistics stats = makeDfs(cp,firstFail(x)).start();
            assertEquals(120,stats.nSolutions);

        } catch (InconsistencyException e) {
            assert(false);
        }
    }

}

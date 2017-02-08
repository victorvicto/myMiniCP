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

package minicp.engine.constraints;

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.util.InconsistencyException;
import org.junit.Test;

import static org.junit.Assert.*;
import static minicp.cp.Factory.*;


public class NotEqualTest {

    @Test
    public void notEqualTest() {
        Solver cp  = makeSolver();

        IntVar x = makeIntVar(cp,10);
        IntVar y = makeIntVar(cp,10);

        try {
            cp.post(notEqual(x,y));

            equal(x,6);

            assertFalse(y.contains(6));
            assertEquals(9,y.getSize());

        } catch (InconsistencyException e) {
            assert(false);
        }
        assertFalse(y.contains(6));
    }

}

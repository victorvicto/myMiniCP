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
import minicp.util.InconsistencyException;
import org.junit.Test;

import static org.junit.Assert.*;
import static minicp.cp.Factory.*;


public class DiffVarTest {

    @Test
    public void diffVar() {
        Solver cp  = new Solver();

        IntVar x = makeIntVar(cp,10);
        IntVar y = makeIntVar(cp,10);

        try {
            cp.post(new DifferentVar(x,y));

            cp.post(new EqualVal(x,6));

        } catch (InconsistencyException e) {
            assert(false);
        }


        assertFalse(y.contains(6));

    }


}

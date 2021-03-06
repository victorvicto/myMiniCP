/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2018. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.engine.constraints;

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;
import org.junit.Test;

import static minicp.cp.Factory.makeIntVar;
import static minicp.cp.Factory.makeSolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class LessOrEqualTest {


    @Test
    public void simpleTest0() {
        try {
            Solver cp = makeSolver();
            IntVar x = makeIntVar(cp, -5, 5);
            IntVar y = makeIntVar(cp, -10, 10);

            cp.post(new LessOrEqual(x, y));

            assertEquals(-5, y.getMin());

            y.removeAbove(3);
            cp.fixPoint();

            assertEquals(9, x.getSize());
            assertEquals(3, x.getMax());

            x.removeBelow(-4);
            cp.fixPoint();

            assertEquals(-4, y.getMin());


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }

}

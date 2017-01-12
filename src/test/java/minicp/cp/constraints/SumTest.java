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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import minicp.cp.core.Status;


public class SumTest {

    @Test
    public void sum1() {
        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, 0, 5), new IntVar(cp, 1, 5), new IntVar(cp, 0, 5)};
            IntVar y = new IntVar(cp, 0, 100);
            cp.add(new Sum(x, y));

            assertEquals(1, y.getMin());
            assertEquals(15, y.getMax());

        } catch (Status e) {
            System.out.format("Error: %s\n",e.toString());
        }

    }

    @Test
    public void sum2() {
        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, -5, 5), new IntVar(cp, 1, 2), new IntVar(cp, 0, 1)};
            IntVar y = new IntVar(cp, 0, 100);
            cp.add(new Sum(x, y));

            assertEquals(-3, x[0].getMin());
            assertEquals(0, y.getMin());
            assertEquals(8, y.getMax());

        } catch (Status e) {
            fail("should not fail");
        }
    }

    @Test
    public void sum3() {
        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, -5, 5), new IntVar(cp, 1, 2), new IntVar(cp, 0, 1)};
            IntVar y = new IntVar(cp, 5, 5);
            cp.add(new Sum(x, y));

            x[0].removeBelow(1);
            x[1].assign(0);
            cp.fixPoint();

            assertEquals(5,x[0].getMax());
            assertEquals(0,x[2].getMin());
            assertEquals(4,x[2].getMax());


        } catch (Status e) {
            fail("should not fail:" + e);
        }
    }




}

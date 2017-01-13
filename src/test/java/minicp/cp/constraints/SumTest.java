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
import minicp.util.NotImplementedException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static minicp.cp.core.Heuristics.*;
import minicp.util.InconsistencyException;

import java.util.Arrays;


public class SumTest {

    @Test
    public void sum1() {
        try {
            try {

                Solver cp = new Solver();
                IntVar y = new IntVar(cp, -100, 100);
                IntVar[] x = new IntVar[]{new IntVar(cp, 0, 5), new IntVar(cp, 1, 5), new IntVar(cp, 0, 5)};
                cp.add(new Sum(x, y));

                assertEquals(1, y.getMin());
                assertEquals(15, y.getMax());

            } catch (InconsistencyException e) {
                fail("should not fail");
            }
        } catch (NotImplementedException e) {
            e.print();
        }

    }

    @Test
    public void sum2() {
        try {
            try {

                Solver cp = new Solver();
                IntVar[] x = new IntVar[]{new IntVar(cp, -5, 5), new IntVar(cp, 1, 2), new IntVar(cp, 0, 1)};
                IntVar y = new IntVar(cp, 0, 100);
                cp.add(new Sum(x, y));

                assertEquals(-3, x[0].getMin());
                assertEquals(0, y.getMin());
                assertEquals(8, y.getMax());

            } catch (InconsistencyException e) {
                fail("should not fail");
            }
        } catch (NotImplementedException e) {
            e.print();
        }
    }

    @Test
    public void sum3() {
        try {
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


            } catch (InconsistencyException e) {
                fail("should not fail");
            }
        } catch (NotImplementedException e) {
            e.print();
        }
    }


    @Test
    public void sum4() {

        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, 0, 5), new IntVar(cp, 0, 2), new IntVar(cp, 0, 1)};
            cp.add(new Sum(x, 0));

            assertEquals(0, x[0].getMax());
            assertEquals(0, x[1].getMax());
            assertEquals(0, x[2].getMax());


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }

    @Test
    public void sum5() {
        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, -5, 0), new IntVar(cp, -5, 0), new IntVar(cp, -3, 0)};
            cp.add(new Sum(x, 0));

            assertEquals(0, x[0].getMin());
            assertEquals(0, x[1].getMin());
            assertEquals(0, x[2].getMin());


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }

    @Test
    public void sum6() {
        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, -5, 0), new IntVar(cp, -5, 0), new IntVar(cp, -3, 3)};
            cp.add(new Sum(x, 0));
            assertEquals(-3, x[0].getMin());
            assertEquals(-3, x[1].getMin());

            x[2].removeAbove(0);
            cp.fixPoint();

            assertEquals(0, x[0].getMin());
            assertEquals(0, x[1].getMin());
            assertEquals(0, x[2].getMin());


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }

    @Test
    public void sum7() {
        try {

            Solver cp = new Solver();
            IntVar[] x = new IntVar[]{new IntVar(cp, -5, 0), new IntVar(cp, -5, 0), new IntVar(cp, -3, 3)};
            cp.add(new Sum(x, 0));
            assertEquals(-3, x[0].getMin());
            assertEquals(-3, x[1].getMin());

            x[2].remove(1);
            x[2].remove(2);
            x[2].remove(3);
            x[2].remove(4);
            x[2].remove(5);
            cp.fixPoint();

            assertEquals(0, x[0].getMin());
            assertEquals(0, x[1].getMin());
            assertEquals(0, x[2].getMin());

        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }


    @Test
    public void sum8() {
        try {

            Solver cp = new Solver();

            // {0,0,0},  1
            // {-2,1,1}  3
            // {2,-1,-1} 3
            // {-1,1,0}  6
            // {0,-3,3}  6
            // {2,-2,0}  6
            // {-1,1,0}  6
            // {1,2,-3}  6


            IntVar[] x = new IntVar[]{new IntVar(cp, -3, 3), new IntVar(cp, -3, 3), new IntVar(cp, -3, 3)};
            cp.add(new Sum(x, 0));

            DFSearch search = new DFSearch(cp.getContext(),firstFail(x));

            SearchStatistics stats = search.start();

            assertEquals(37,stats.nSolutions);


        } catch (InconsistencyException e) {
            fail("should not fail");
        }
    }




}

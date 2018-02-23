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

package minicp.engine.core;

import minicp.util.InconsistencyException;
import org.junit.Test;

import static minicp.cp.Factory.makeIntVar;
import static minicp.cp.Factory.minus;
import static minicp.cp.Factory.mul;
import static org.junit.Assert.*;


public class IntVarViewOppositeTest {

    public boolean propagateCalled = false;

    @Test
    public void testIntVar() {
        Solver cp  = new Solver();

        IntVar x = minus(makeIntVar(cp,-3,4)); // domain is {-4..3}

        assertEquals(-4,x.getMin());
        assertEquals(3,x.getMax());
        assertEquals(8,x.getSize());

        cp.getTrail().push();


        try {

            assertFalse(x.isBound());

            x.remove(0);
            assertFalse(x.contains(0));
            x.remove(2);
            assertTrue(x.contains(1));
            assertTrue(x.contains(-4));
            assertEquals(6,x.getSize());
            x.removeAbove(0);
            assertEquals(-1,x.getMax());
            x.removeBelow(-2);
            assertEquals(-2,x.getMin());
            x.assign(-1);
            assertTrue(x.isBound());
            assertEquals(-1,x.getMax());



        } catch (InconsistencyException e) {
            e.printStackTrace();
            fail("should not fail here");
        }

        try {
            x.assign(8);
            fail( "should have failed" );
        } catch (InconsistencyException expectedException) {}

        cp.getTrail().pop();

        assertEquals(8,x.getSize());
        assertTrue(x.contains(-4));

    }


    @Test
    public void onDomainChangeOnBind() {
        propagateCalled = false;
        Solver cp  = new Solver();

        IntVar x = minus(makeIntVar(cp,10));
        IntVar y = minus(makeIntVar(cp,10));

        Constraint cons = new Constraint(cp) {

            @Override
            public void post() throws InconsistencyException {
                x.whenBind(() -> propagateCalled = true);
                y.whenDomainChange(() -> propagateCalled = true);
            }
        };

        try {
            cp.post(cons);
            x.remove(9);
            cp.fixPoint();
            assertFalse(propagateCalled);
            x.assign(-4);
            cp.fixPoint();
            assertTrue(propagateCalled);
            propagateCalled = false;
            y.remove(9);
            cp.fixPoint();
            assertFalse(propagateCalled);
            y.remove(-9);
            cp.fixPoint();
            assertTrue(propagateCalled);

        } catch (InconsistencyException inconsistency) {
            fail("should not fail");
        }
    }


    @Test
    public void onBoundChange() {
        propagateCalled = false;
        Solver cp = new Solver();

        IntVar x = minus(makeIntVar(cp, 10));

        Constraint cons = new Constraint(cp) {

            @Override
            public void post() throws InconsistencyException {
                x.whenBoundsChange(() -> propagateCalled = true);
            }
        };

        try {
            cp.post(cons);
            x.remove(-8);
            cp.fixPoint();
            assertFalse(propagateCalled);
            x.remove(-9);
            cp.fixPoint();
            assertTrue(propagateCalled);

            propagateCalled = false;
            x.assign(-4);
            cp.fixPoint();
            assertTrue(propagateCalled);


        } catch (InconsistencyException inconsistency) {
            fail("should not fail");
        }
    }


}

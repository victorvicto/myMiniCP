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

package minicp.cp.core;

import minicp.util.NotImplementedException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class IntVarTest {

    public boolean propagateCalled = false;

    @Test
    public void testIntVar() {
        Solver cp  = new Solver();

        IntVar x = new IntVar(cp,10);
        IntVar y = new IntVar(cp,10);

        cp.getContext().push();


        try {

        assertFalse(x.isBound());
        x.remove(5);
        assertEquals(9,x.getSize());
        x.assign(7);
        assertEquals(1,x.getSize());
        assertTrue(x.isBound());
        assertEquals(7,x.getMin());
        assertEquals(7,x.getMax());

        } catch(Status e) { fail("should not fail here");}

        try {
            x.assign(8);
            fail( "should have failed" );
        } catch (Status expectedException) {}



        cp.getContext().pop();
        cp.getContext().push();

        assertFalse(x.isBound());
        assertEquals(10,x.getSize());

        for (int i = 0; i < 10; i++) {
            assertTrue(x.contains(i));
        }
        assertFalse(x.contains(-1));

    }

    @Test
    public void onDomainChangeOnBind() {

        Solver cp  = new Solver();

        IntVar x = new IntVar(cp,10);
        IntVar y = new IntVar(cp,10);

        Constraint cons = new Constraint() {

            @Override
            public void setup() throws Status {
                x.propagateOnBind(this);
                y.propagateOnDomainChange(this);
            }
            @Override
            public void propagate() throws Status {
                propagateCalled = true;
            }
        };

        try {
            cp.add(cons);
            x.remove(8);
            cp.getEngine().fixPoint();
            assertFalse(propagateCalled);
            x.assign(4);
            cp.getEngine().fixPoint();
            assertTrue(propagateCalled);
            propagateCalled = false;
            y.remove(10);
            cp.getEngine().fixPoint();
            assertFalse(propagateCalled);
            y.remove(9);
            cp.getEngine().fixPoint();
            assertTrue(propagateCalled);

        } catch (Status inconsistency) {
            fail("should not fail");
        }
    }

    @Test
    public void arbitraryRangeDomains() {

        try {

            Solver cp  = new Solver();

            IntVar x = new IntVar(cp,-10,10);

            cp.getContext().push();


            try {

                assertFalse(x.isBound());
                x.remove(-9);
                x.remove(-10);


                assertEquals(19,x.getSize());
                x.assign(-4);
                assertEquals(1,x.getSize());
                assertTrue(x.isBound());
                assertEquals(-4,x.getMin());

            } catch(Status e) { fail("should not fail here");}

            try {
                x.assign(8);
                fail( "should have failed" );
            } catch (Status expectedException) {}


            cp.getContext().pop();

            assertEquals(20,x.getSize());

            for (int i = -10; i < 10; i++) {
                assertTrue(x.contains(i));
            }
            assertFalse(x.contains(-11));


        } catch(Status s) {
            System.out.println("Error: " + s.toString());
        } catch (NotImplementedException e) {
            e.print();
        }
    }


    @Test
    public void arbitrarySetDomains() {

        try {

            Solver cp  = new Solver();


            Set<Integer> dom = new HashSet<>(Arrays.asList(-7,-10,6,9,10,12));

            IntVar x = new IntVar(cp,dom);

            cp.getContext().push();

            try {

                for (int i = -15; i < 15; i++) {
                    if (dom.contains(i)) assertTrue(x.contains(i));
                    else assertFalse(x.contains(i));
                }

                x.assign(-7);
            } catch(Status e) { fail("should not fail here");}

            try {
                x.assign(-10);
                fail( "should have failed" );
            } catch (Status expectedException) {}


            cp.getContext().pop();

            for (int i = -15; i < 15; i++) {
                if (dom.contains(i)) assertTrue(x.contains(i));
                else assertFalse(x.contains(i));
            }
            assertEquals(6,x.getSize());


        } catch (Status e) {
            System.out.format("Error: %s\n",e);
        }
    }


    @Test
    public void onBoundChange() {

        try {

            Solver cp = new Solver();

            IntVar x = new IntVar(cp, 10);
            IntVar y = new IntVar(cp, 10);

            Constraint cons = new Constraint() {

                @Override
                public void setup() throws Status {
                    x.propagateOnBoundChange(this);
                    y.propagateOnDomainChange(this);
                }

                @Override
                public void propagate() throws Status {
                    propagateCalled = true;
                }
            };

            try {
                cp.add(cons);
                x.remove(8);
                cp.getEngine().fixPoint();
                assertFalse(propagateCalled);
                x.remove(9);
                cp.getEngine().fixPoint();
                assertTrue(propagateCalled);
                propagateCalled = false;
                x.assign(4);
                cp.getEngine().fixPoint();
                assertTrue(propagateCalled);
                propagateCalled = false;
                y.remove(10);
                cp.getEngine().fixPoint();
                assertFalse(propagateCalled);
                y.remove(2);
                cp.getEngine().fixPoint();
                assertTrue(propagateCalled);

            } catch (Status inconsistency) {
                fail("should not fail");
            }

        } catch (NotImplementedException e) {
            e.print();
        }
    }


    @Test
    public void removeAbove() {

        try {

            Solver cp = new Solver();

            IntVar x = new IntVar(cp, 10);

            Constraint cons = new Constraint() {

                @Override
                public void setup() throws Status {
                    x.propagateOnBoundChange(this);
                }

                @Override
                public void propagate() throws Status {
                    propagateCalled = true;
                }
            };

            try {
                cp.add(cons);
                x.remove(8);
                cp.getEngine().fixPoint();
                assertFalse(propagateCalled);
                assertEquals(7,x.removeAbove(8));
                cp.getEngine().fixPoint();
                assertTrue(propagateCalled);

            } catch (Status inconsistency) {
                fail("should not fail");
            }

        } catch (NotImplementedException e) {
            e.print();
        }
    }

    @Test
    public void removeBelow() {

        try {

            Solver cp = new Solver();

            IntVar x = new IntVar(cp, 10);

            Constraint cons = new Constraint() {

                @Override
                public void setup() throws Status {
                    x.propagateOnBoundChange(this);
                }

                @Override
                public void propagate() throws Status {
                    propagateCalled = true;
                }
            };

            try {
                cp.add(cons);
                x.remove(3);
                cp.getEngine().fixPoint();
                assertFalse(propagateCalled);
                assertEquals(4,x.removeBelow(3));
                cp.getEngine().fixPoint();
                assertTrue(propagateCalled);
                propagateCalled = false;

                assertEquals(5,x.removeBelow(5));
                cp.getEngine().fixPoint();
                assertTrue(propagateCalled);
                propagateCalled = false;


            } catch (Status inconsistency) {
                fail("should not fail");
            }

        } catch (NotImplementedException e) {
            e.print();
        }
    }






}

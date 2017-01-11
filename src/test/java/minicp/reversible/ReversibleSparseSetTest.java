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

package minicp.reversible;

import minicp.cp.core.Status;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ReversibleSparseSetTest {


    @Test
    public void testExample() {

        ReversibleContext rc = new ReversibleContext();
        ReversibleSparseSet set = new ReversibleSparseSet(rc,9);

        rc.push();

        set.remove(4);
        set.remove(6);

        assertFalse(set.contains(4));
        assertFalse(set.contains(6));

        rc.pop();

        assertTrue(set.contains(4));
        assertTrue(set.contains(6));

    }

    @Test
    public void testReversibleSparseSet() {

        ReversibleContext rc = new ReversibleContext();
        ReversibleSparseSet set = new ReversibleSparseSet(rc,10);

        rc.push();

        set.remove(1);
        set.remove(0);

        assertTrue(set.getMin() == 2);

        set.remove(8);
        set.remove(9);

        assertTrue(set.getMax() == 7);

        rc.pop();
        rc.push();

        assertEquals(10, set.getSize());

        for (int i = 0; i < 10; i++) {
            assertTrue(set.contains(i));
        }
        assertFalse(set.contains(10));

        assertTrue(set.getMin() == 0);
        assertTrue(set.getMax() == 9);

        set.removeAllBut(2);

        for (int i = 0; i < 10; i++) {
            if (i != 2) assertFalse(set.contains(i));
        }
        assertTrue(set.contains(2));

        rc.pop();
        rc.push();

        assertEquals(10, set.getSize());

    }

    @Test
    public void testRangeConstructor() {

        try {

            ReversibleContext rc = new ReversibleContext();
            ReversibleSparseSet set = new ReversibleSparseSet(rc,-5,5);

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

            rc.push();

            set.remove(-4);
            set.remove(-5);
            set.remove(4);
            set.remove(5);

            assertEquals(-3,set.getMin());
            assertEquals(3,set.getMax());

            rc.push();

            set.removeAllBut(-1);
            assertEquals(-1,set.getMin());
            assertEquals(-1,set.getMax());


            rc.pop();
            rc.pop();

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

        } catch (Status e) {
            System.out.println("error:" + e);
        }
    }

    @Test
    public void testRemoveBelow() {

        try {

            ReversibleContext rc = new ReversibleContext();
            ReversibleSparseSet set = new ReversibleSparseSet(rc,-5,5);

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

            rc.push();


            set.remove(-2);
            set.remove(-1);
            set.removeBelow(-2);



            assertEquals(0,set.getMin());
            assertEquals(5,set.getMax());

            rc.push();

            set.removeBelow(2);

            assertEquals(1,set.getMin());

            rc.pop();
            rc.pop();

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

        } catch (Status e) {
            System.out.println("error: " + e);
        }
    }

    @Test
    public void testRemoveAbove() {

        try {

            ReversibleContext rc = new ReversibleContext();
            ReversibleSparseSet set = new ReversibleSparseSet(rc,-5,5);

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

            rc.push();


            set.remove(1);
            set.remove(2);
            set.removeAbove(2);

            assertEquals(-5,set.getMin());
            assertEquals(0,set.getMax());

            rc.push();

            set.removeAbove(-2);

            assertEquals(-3,set.getMax());

            rc.pop();
            rc.pop();

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

        } catch (Status e) {
            System.out.println("error:" + e);
        }
    }
}

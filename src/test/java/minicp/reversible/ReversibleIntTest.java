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
 * MErsHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mini-cp.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2016 L. Michel, P. Schaus, P. Van Hentenryck
 */

package minicp.reversible;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ReversibleIntTest {

    @Test
    public void testExample() {

        ReversibleState rs = new ReversibleState();

        // Two reversible int's inside the reversible context rs
        ReversibleInt a = new ReversibleInt(rs,5);
        ReversibleInt b = new ReversibleInt(rs,9);

        a.setValue(7);
        b.setValue(13);

        // Record current state a=7, b=1 and increase the level to 0
        rs.push();
        assertEquals(0,rs.getLevel());

        a.setValue(10);
        b.setValue(13);
        a.setValue(11);

        // Record current state a=11, b=13 and increase the level to 1
        rs.push();
        assertEquals(1,rs.getLevel());

        a.setValue(4);
        b.setValue(9);

        // Restore the state recorded at the top level 1: a=11, b=13
        // and remove the state of that level
        rs.pop();

        assertEquals(11,a.getValue());
        assertEquals(13,b.getValue());
        assertEquals(0,rs.getLevel());

        // Restore the state recorded at the top level 0: a=7, b=13
        // and remove the state of that level
        rs.pop();

        assertEquals(7,a.getValue());
        assertEquals(13,b.getValue());
        assertEquals(-1,rs.getLevel());

    }


    @Test
    public void testReversibleInt() {
        ReversibleState rs = new ReversibleState();
        ReversibleInt a = new ReversibleInt(rs,5);
        ReversibleInt b = new ReversibleInt(rs,5);
        assertTrue(a.getValue() == 5);
        a.setValue(7);
        b.setValue(13);
        assertTrue(a.getValue() == 7);

        rs.push();

        a.setValue(10);
        assertTrue(a.getValue() == 10);
        a.setValue(11);
        assertTrue(a.getValue() == 11);
        b.setValue(16);
        b.setValue(15);

        rs.pop();
        assertTrue(a.getValue() == 7);
        assertTrue(b.getValue() == 13);

    }

    @Test
    public void testPopAll() {

        ReversibleState rs = new ReversibleState();
        ReversibleInt a = new ReversibleInt(rs,5);
        ReversibleInt b = new ReversibleInt(rs,5);

        rs.push();

        a.setValue(7);
        b.setValue(13);
        a.setValue(13);

        rs.push();

        a.setValue(5);
        b.setValue(10);

        ReversibleInt c = new ReversibleInt(rs,5);

        rs.push();

        a.setValue(8);
        b.setValue(1);
        c.setValue(10);

        rs.popAll();
        rs.push();

        assertEquals(5,a.getValue());
        assertEquals(5,b.getValue());
        assertEquals(5,c.getValue());


        a.setValue(10);
        b.setValue(13);
        b.setValue(16);

        rs.push();

        a.setValue(8);
        b.setValue(10);

        rs.pop();


        assertEquals(10,a.getValue());
        assertEquals(16,b.getValue());
        assertEquals(5,c.getValue());

        rs.popAll();

        assertEquals(5,a.getValue());
        assertEquals(5,b.getValue());
        assertEquals(5,c.getValue());

    }


    @Test
    public void testPopUntill() {

        ReversibleState rs = new ReversibleState();
        ReversibleInt a = new ReversibleInt(rs,5);
        ReversibleInt b = new ReversibleInt(rs,5);

        a.setValue(7);
        b.setValue(13);
        a.setValue(13);

        rs.push(); // level 0

        a.setValue(5);
        b.setValue(10);

        ReversibleInt c = new ReversibleInt(rs,5);

        rs.push(); // level 1

        a.setValue(8);
        b.setValue(1);
        c.setValue(10);

        rs.push(); // level 2

        a.setValue(10);
        b.setValue(13);
        b.setValue(16);

        rs.push(); // level 3

        a.setValue(8);
        b.setValue(10);

        rs.popUntil(0);

        assertEquals(0,rs.getLevel());

        rs.push(); // level 1

        assertEquals(1,rs.getLevel());
        assertEquals(5,a.getValue());
        assertEquals(10,b.getValue());
        assertEquals(5,c.getValue());

        a.setValue(8);
        b.setValue(10);
        b.setValue(8);
        b.setValue(10);

        rs.popUntil(0);

        assertEquals(0,rs.getLevel());
        assertEquals(5,a.getValue());
        assertEquals(10,b.getValue());
        assertEquals(5,c.getValue());


    }

}

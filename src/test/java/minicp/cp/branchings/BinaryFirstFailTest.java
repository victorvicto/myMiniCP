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

package minicp.cp.branchings;

import minicp.cp.core.IntVar;
import minicp.cp.core.Model;
import minicp.reversible.ReversibleBool;
import minicp.reversible.ReversibleContext;
import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.search.Inconsistency;
import minicp.util.NotImplementedException;
import org.junit.Test;

import static org.junit.Assert.*;


public class BinaryFirstFailTest {

    @Test
    public void selectRightVariable() {

        try {
            try {
                Model cp = new Model();
                IntVar[] x = new IntVar[]{new IntVar(cp, 0, 5), new IntVar(cp, 0, 2), new IntVar(cp, 0, 4)};

                Branching b = new BinaryFirstFail(x,i -> x[i].getMax());
                Alternative [] alts = b.getAlternatives();

                cp.push();
                alts[0].execute();
                assertEquals(1,x[1].getSize());
                assertEquals(2,x[1].getMin());
                assertEquals(6,x[0].getSize());
                assertEquals(5,x[2].getSize());
                cp.pop();
                alts[1].execute();
                assertFalse(x[1].contains(2));
                assertEquals(2,x[1].getSize());
                assertEquals(6,x[0].getSize());
                assertEquals(5,x[2].getSize());
            } catch(Inconsistency e) {fail("should not fail");}
        } catch (NotImplementedException e) {
            e.print();
        }





    }



}

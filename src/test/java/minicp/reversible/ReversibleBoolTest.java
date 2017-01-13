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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ReversibleBoolTest {

    @Test
    public void testReversibleBool() {
        ReversibleState rc = new ReversibleState();

        ReversibleBool b1 = new ReversibleBool(rc,true);
        ReversibleBool b2 = new ReversibleBool(rc,false);

        rc.push();

        b1.setValue(true);
        b1.setValue(false);
        b1.setValue(true);

        b2.setValue(false);
        b2.setValue(true);

        rc.pop();

        assertTrue(b1.getValue());
        assertFalse(b2.getValue());

    }



}

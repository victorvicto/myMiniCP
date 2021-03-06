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
 * Copyright (c)  2017. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.reversible;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class  ReversibleBoolTest {

    @Test
    public void testReversibleBool() {
        Trail trail = new Trail();

        ReversibleBool b1 = new ReversibleBool(trail,true);
        ReversibleBool b2 = new ReversibleBool(trail,false);

        trail.push();

        b1.setValue(true);
        b1.setValue(false);
        b1.setValue(true);

        b2.setValue(false);
        b2.setValue(true);

        trail.pop();

        assertTrue(b1.getValue());
        assertFalse(b2.getValue());

    }



}

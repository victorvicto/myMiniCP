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


package minicp.engine.constraints;

import minicp.cp.Factory;
import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.reversible.ReversibleInt;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Element1D extends Constraint {

    /**
     * T[y] = z
     * @param T
     * @param y
     * @param z
     */
    public Element1D(int[] T, IntVar y, IntVar z) {
        super(y.getSolver());
        // TODO if Element1D extends Element2D
        throw new NotImplementedException("Element1D");
    }

    @Override
    public void post() throws InconsistencyException {
        // TODO (if not extending Element2D)
        throw new NotImplementedException("Element1D");

    }
}

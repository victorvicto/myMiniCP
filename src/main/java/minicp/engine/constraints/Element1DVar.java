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

package minicp.engine.constraints;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

public class Element1DVar extends Constraint {

    private final IntVar[] T;
    private final IntVar y;
    private final IntVar z;



    public Element1DVar(IntVar[] T, IntVar y, IntVar z) {
        super(y.getSolver());
        this.T = T;
        this.y = y;
        this.z = z;
    }

    @Override
    public void post() throws InconsistencyException {
        // TODO
        throw new NotImplementedException("Element1DVar");
    }

    @Override
    public void propagate() throws InconsistencyException {
        // TODO
        throw new NotImplementedException("Element1DVar");
    }
}

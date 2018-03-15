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

public class Absolute extends Constraint {

    private IntVar x;
    private IntVar y;

    /**
     * Build a constraint y = |x|
     *
     * @param x
     * @param y
     */
    public Absolute(IntVar x, IntVar y) {
        super(x.getSolver());
        this.x = x;
        this.y = y;
    }

    @Override
    public void post() throws InconsistencyException {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    public void propagate() throws InconsistencyException {
        // TODO
        throw new NotImplementedException();
    }

}

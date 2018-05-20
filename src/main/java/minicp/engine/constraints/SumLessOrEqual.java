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

import minicp.engine.core.BoolVar;
import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.reversible.ReversibleInt;
import minicp.util.InconsistencyException;

public class SumLessOrEqual extends Constraint { // b <=> x <= c

    private final IntVar[] x;
    private final int c;

    public SumLessOrEqual(IntVar[] x, int c) {
        super(x[0].getSolver());
        this.x = x;
        this.c = c;
    }

    @Override
    public void post() throws InconsistencyException {
        int tot = 0;
        for (IntVar xi : x) {
            if (xi.isBound()) {
                tot += xi.getMin();
            }
        }
        if (tot>c) {
            throw new InconsistencyException();
        }
        if (tot==c) {
            for (IntVar xi : x) {
                if (!xi.isBound()) {
                    xi.assign(0);
                }
            }
        }
        for (IntVar xi : x) {
            xi.propagateOnBind(this);
        }
    }

    @Override
    public void propagate() throws InconsistencyException {
        int tot = 0;
        for (IntVar xi : x) {
            if (xi.isBound()) {
                tot += xi.getMin();
            }
        }
        if (tot>c) {
            throw new InconsistencyException();
        }
        if (tot==c) {
            for (IntVar xi : x) {
                if (!xi.isBound()) {
                    xi.assign(0);
                }
            }
        }
    }
}

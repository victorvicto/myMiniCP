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

package minicp.engine.constraints;

import minicp.engine.core.BoolVar;
import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;

public class IsEqual extends Constraint { // b <=> x == c

    private final BoolVar b;
    private final IntVar x;
    private final int c;

    public IsEqual(BoolVar b, IntVar x, int c) {
        super(x.getSolver());
        this.b = b;
        this.x = x;
        this.c = c;
    }

    @Override
    public void post() throws InconsistencyException {
        if (b.isTrue()) {
            x.assign(c);
        } else if (b.isFalse()) {
            x.remove(c);
        } else if (x.isBound()) {
            b.assign(x.getMin() == c);
        } else if (!x.contains(c)) {
            b.assign(0);
        } else {
            b.whenBind(() -> {
                if (b.isTrue()) x.assign(c);
                else x.remove(c);
            });
            x.whenBind(() ->
                b.assign(x.getMin() == c)
            );
            x.whenDomainChange(() -> {
                if (!x.contains(c))
                    b.assign(0);
            });
        }
    }
}

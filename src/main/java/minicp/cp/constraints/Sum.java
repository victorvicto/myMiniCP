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

package minicp.cp.constraints;

import minicp.cp.core.Constraint;
import minicp.cp.core.IntVar;
import minicp.cp.core.Status;

public class Sum extends Constraint {

    private IntVar [] x;
    private IntVar y;

    /**
     * Create a sum constraint that holds iff
     * x[0]+x[1]+...+x[x.length-1] = y
     * @param x
     * @param y
     */
    public Sum(IntVar [] x, IntVar y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setup() throws Status {
        throw new Status(Status.Type.NotImplemented);
    }

    @Override
    public void propagate() throws Status {
        throw new Status(Status.Type.NotImplemented);
    }

}

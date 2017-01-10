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
import minicp.cp.core.Engine;

public class AllDifferentBinary extends Constraint {

    private IntVar [] x;

    public AllDifferentBinary(IntVar ... x) {
        this.x = x;
    }

    @Override
    public void setup() throws Status {
        Engine cp = x[0].getEngine();
        for (int i = 0; i < x.length; i++) {
            for (int j = i+1; j < x.length; j++) {
                cp.add(new DifferentVar(x[i],x[j]),false);
            }
        }
    }

}

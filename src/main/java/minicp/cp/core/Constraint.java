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

package minicp.cp.core;
import minicp.reversible.ReversibleBool;
import minicp.util.InconsistencyException;

public abstract class Constraint {

    protected final Solver cp;
    protected boolean scheduled = false;
    protected final ReversibleBool active;

    public Constraint(Solver cp) {
        this.cp = cp;
        active = new ReversibleBool(cp.getContext(),true);
    }

    public boolean isActive() {
        return active.getValue();
    }

    public void deactivate() {
        active.setValue(false);
    }

    public abstract void setup() throws InconsistencyException;
    public void propagate() throws InconsistencyException {}
}

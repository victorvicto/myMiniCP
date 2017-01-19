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
import minicp.cp.core.IntVarImpl;
import minicp.reversible.ReversibleInt;
import minicp.search.DFSearch;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.Arrays;

public class Minimize extends Constraint {

    public int bound = Integer.MAX_VALUE;
    public final IntVar x;
    public final DFSearch dfs;

    public Minimize(IntVar x, DFSearch dfs) {
        super(x.getSolver());
        this.x = x;
        this.dfs = dfs;
    }

    protected void tighten() {
        if (!x.isBound()) throw new RuntimeException("objective not bound");
        this.bound = x.getMax() - 1;
    }


    @Override
    public void setup() throws InconsistencyException {
        x.whenBoundsChange(() -> {
            x.removeAbove(bound);
        });
        // Ensure that the constraint is scheduled on backtrack
        dfs.onSolution(() -> {
            tighten();
            cp.schedule(this);
        });
        dfs.onFail(() -> cp.schedule(this));
    }

}

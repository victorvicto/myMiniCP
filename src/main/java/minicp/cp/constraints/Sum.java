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
import minicp.reversible.ReversibleInt;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.Arrays;

public class Sum extends Constraint {

    private  int[] unBounds;
    private ReversibleInt nUnBounds;
    private ReversibleInt sumBounds;
    private IntVar [] x;
    private int n;

    public Sum(IntVar [] x, IntVar y) {
        this(Arrays.copyOf(x, x.length + 1));
        this.x[x.length] = y ; // TODO: should be a view on Y
        throw new NotImplementedException();
    }


    public Sum(IntVar [] x, int y) {
        this(Arrays.copyOf(x, x.length + 1));
        this.x[x.length] = new IntVar(cp,-y,-y);
    }

    /**
     * Create a sum constraint that holds iff
     * x[0]+x[1]+...+x[x.length-1] = 0
     * @param x
     */
    public Sum(IntVar [] x) {
        super(x[0].getSolver());
        this.x = x;
        this.n = x.length;
        nUnBounds = new ReversibleInt(cp.getContext(),n);
        sumBounds = new ReversibleInt(cp.getContext(),0);
        unBounds = new int[n];
        for (int i = 0; i < n; i++) {
            unBounds[i] = i;
        }
    }

    @Override
    public void setup() throws InconsistencyException {
        for (IntVar var: x) {
            var.propagateOnBoundChange(this);
            //var.propagateOnDomainChange(this);
        }
        propagate();
    }

    @Override
    public void propagate() throws InconsistencyException {
        // Filter the unbound vars and update the partial sum
        int nU = nUnBounds.getValue();
        int partialSum = sumBounds.getValue();
        for (int i = nU - 1; i >= 0; i--) {
            int idx = unBounds[i];
            IntVar y = x[idx];
            if (y.isBound()) {
                // Update partial sum
                partialSum += y.getMin();
                // Swap the variable
                unBounds[i] = unBounds[nU - 1];
                unBounds[nU - 1] = idx;
                nU--;
            }
        }
        sumBounds.setValue(partialSum);
        nUnBounds.setValue(nU);

        int sumMax = partialSum;
        int sumMin = partialSum;
        for (int i = nU - 1; i >= 0; i--) {
            int idx = unBounds[i];
            sumMax += x[idx].getMax();
            sumMin += x[idx].getMin();
        }
        for (int i = nU - 1; i >= 0; i--) {
            int idx = unBounds[i];
            x[idx].removeAbove(-(sumMin-x[idx].getMin()));
            x[idx].removeBelow(-(sumMax-x[idx].getMax()));
        }

    }

}

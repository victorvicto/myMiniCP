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

    private final int[] T;
    private final IntVar y, z;
    private int n;

    private final ReversibleInt low;
    private final ReversibleInt up;
    private final ArrayList<Element1D.Tripple> yz;

    private class Tripple implements Comparable<Element1D.Tripple> {
        protected final int y,z;

        private Tripple(int y, int z) {
            this.y = y;
            this.z = z;
        }

        @Override
        public int compareTo(Element1D.Tripple t) {
            return z - t.z;
        }
    }

    /**
     * T[y] = z
     * @param T
     * @param y
     * @param z
     */
    public Element1D(int[] T, IntVar y, IntVar z) {
        super(y.getSolver());

        this.T = T;
        this.y = y;
        this.z = z;
        n = T.length;

        this.yz = new ArrayList<Element1D.Tripple>();
        for (int i = 0; i < T.length; i++) {
            yz.add(new Element1D.Tripple(i,T[i]));
        }
        Collections.sort(yz);
        low = new ReversibleInt(cp.getTrail(),0);
        up = new ReversibleInt(cp.getTrail(),yz.size()-1);
    }

    @Override
    public void post() throws InconsistencyException {
        y.removeBelow(0);
        y.removeAbove(n-1);

        y.propagateOnDomainChange(this);
        z.propagateOnBoundChange(this);
        propagate();
    }

    @Override
    public void propagate() throws InconsistencyException {
        int l = low.getValue();
        int u = up.getValue();
        int zMin = z.getMin();
        int zMax = z.getMax();

        while (yz.get(l).z < zMin || !y.contains(yz.get(l).y)) {
            y.remove(yz.get(l).y);
            l++;
            if (l > u) throw new InconsistencyException();
        }
        while (yz.get(u).z > zMax || !y.contains(yz.get(u).y)) {
            y.remove(yz.get(u).y);
            u--;
            if (l > u) throw new InconsistencyException();
        }
        z.removeBelow(yz.get(l).z);
        z.removeAbove(yz.get(u).z);
        low.setValue(l);
        up.setValue(u);
    }
}

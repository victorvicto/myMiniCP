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

public class Maximum extends Constraint {

    private final IntVar[] x;
    private final IntVar y;

    /**
     * y = maximum(x[0],x[1],...,x[n])
     * @param x
     * @param y
     */
    public Maximum(IntVar[] x, IntVar y) {
        super(x[0].getSolver());
        assert (x.length > 0);
        this.x = x;
        this.y = y;
    }


    @Override
    public void post() throws InconsistencyException {
        int maxminx = -2147483647;
        int maxx = -2147483647;
        int m = 0;
        int maxy = y.getMax();
        int miny = y.getMin();
        int numIntersect = 0;
        IntVar lastIntersecting = null;
        for(IntVar xi : x) {
            xi.removeAbove(maxy);
            m = xi.getMin();
            if (m > maxminx)
                maxminx = m;
            m = xi.getMax();
            if (m >= miny) {
                numIntersect++;
                lastIntersecting = xi;
            }
            if (m > maxx)
                maxx = m;
            xi.propagateOnDomainChange(this);
        }
        y.removeBelow(maxminx);
        y.removeAbove(maxx);
        miny = y.getMin();
        if(numIntersect==1)
            lastIntersecting.removeBelow(miny);
        y.propagateOnDomainChange(this);
    }

    @Override
    public void propagate() throws InconsistencyException {
        int maxminx = -2147483647;
        int maxx = -2147483647;
        int m = 0;
        int maxy = y.getMax();
        int miny = y.getMin();
        int numIntersect = 0;
        IntVar lastIntersecting = null;
        for(IntVar xi : x) {
            xi.removeAbove(maxy);
            m = xi.getMin();
            if (m > maxminx)
                maxminx = m;
            m = xi.getMax();
            if (m >= miny) {
                numIntersect++;
                lastIntersecting = xi;
            }
            if (m > maxx)
                maxx = m;
        }
        y.removeBelow(maxminx);
        y.removeAbove(maxx);
        miny = y.getMin();
        if(numIntersect==1)
            lastIntersecting.removeBelow(miny);
    }
}

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

import static minicp.cp.Factory.*;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.engine.constraints.Profile.Rectangle;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.ArrayList;

public class Cumulative extends Constraint {

    private final IntVar[] start;
    private final int[] duration;
    private final IntVar[] end;
    private final int[] demand;
    private final int capa;
    private final boolean postMirror;


    public Cumulative(IntVar[] start, int[] duration, int[] demand, int capa) throws InconsistencyException {
        this(start, duration, demand, capa, true);
    }

    private Cumulative(IntVar[] start, int[] duration, int[] demand, int capa, boolean postMirror) throws InconsistencyException {
        super(start[0].getSolver());
        this.start = start;
        this.duration = duration;
        this.end = makeIntVarArray(cp,start.length, i -> plus(start[i],duration[i]));
        this.demand = demand;
        this.capa = capa;
        this.postMirror = postMirror;
    }


    @Override
    public void post() throws InconsistencyException {
        for (int i = 0; i < start.length; i++) {
            start[i].propagateOnBoundChange(this);
        }

        if (postMirror) {
            IntVar[] startMirror = makeIntVarArray(cp, start.length, i -> minus(end[i]));
            cp.post(new Cumulative(startMirror, duration, demand, capa, false), false);
        }

        propagate();
    }

    @Override
    public void propagate() throws InconsistencyException {


        Profile profile = buildProfile();
        // TODO: check that the profile is not exceeding the capa
        // TODO: otherwise throw an INCONSISTENCY

        for (int i = 0; i < profile.size(); i++) {
            // TODO: check
            if (profile.get(i).height > capa) {
                throw InconsistencyException.INCONSISTENCY;
            }
        }

        for (int i = 0; i < start.length; i++) {
            if (!start[i].isBound()) {
                // j is the index of the profile rectangle overlapping t
                int j = profile.rectangleIndex(start[i].getMin());
                // TODO: push j to the right
                // hint:
                // You need to check that at every-point on the interval
                // [start[i].getMin() ... start[i].getMin()+duration[i]-1] there is enough space.
                // You may have to look-ahead on the next profile rectangle(s)
                // Be careful that the activity you currently push may have contributed to the profile.

            }
        }
        throw new NotImplementedException("Cumulative");
    }

    public Profile buildProfile() throws InconsistencyException {
        ArrayList<Rectangle> mandatoryParts = new ArrayList<Rectangle>();
        for (int i = 0; i < start.length; i++) {
            // TODO: add mandatory part of activity i if any
        }
        return new Profile(mandatoryParts.toArray(new Profile.Rectangle[0]));
    }

}

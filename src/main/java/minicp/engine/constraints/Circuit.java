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
import minicp.reversible.ReversibleInt;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

public class Circuit extends Constraint {

    private final IntVar [] x;
    private final ReversibleInt [] dest;
    private final ReversibleInt [] orig;
    private final ReversibleInt [] lengthToDest;

    /**
     * x represents an Hamiltonian circuit on the cities {0..x.length-1}
     * where x[i] is the city visited after city i
     * @param x
     */
    public Circuit(IntVar [] x) {
        super(x[0].getSolver());
        this.x = x;
        dest = new ReversibleInt[x.length];
        orig = new ReversibleInt[x.length];
        lengthToDest = new ReversibleInt[x.length];
        for (int i = 0; i < x.length; i++) {
            dest[i] = new ReversibleInt(cp.getTrail(),i);
            orig[i] = new ReversibleInt(cp.getTrail(),i);
            lengthToDest[i] = new ReversibleInt(cp.getTrail(),0);
        }
    }


    @Override
    public void post() throws InconsistencyException {
        cp.post(allDifferent(x));

        for(int i = 0; i < x.length; i++) {
            if (x[i].isBound()) {
                bind(i);
            }
        }

        for (int i = 0; i < x.length; i++) {

            if (lengthToDest[orig[i].getValue()].getValue() < x.length) {
                int[] vals = x[i].getValues();
                for(int j = 0; j < vals.length; j++) {
                    if (dest[vals[j]] == dest[i]){
                        x[i].remove(vals[j]);
                    }
                }
            }
        }

        for (int i = 0; i < x.length; i++) {
            int finalI = i;
            x[i].whenBind( () ->{
                bind(finalI);
            });
        }
    }


    private void bind(int i) throws InconsistencyException {
        for (int j = 0; j < x.length; j++){
            if (dest[j].getValue() == i) {
                dest[j] = dest[x[i].getMin()];
                lengthToDest[j].setValue( lengthToDest[j].getValue() + 1 + lengthToDest[x[i].getMin()].getValue());
            }
            if (orig[j].getValue() == x[i].getMin()) {
                orig[j] = orig[i];
            }
        }
    }
}

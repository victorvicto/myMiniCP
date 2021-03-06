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
import minicp.engine.core.IntVarImpl;
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

        if (x.length > 1) {
            for (int i = 0; i < x.length; i++) {
                x[i].remove(i);
                x[i].removeBelow(0);
                x[i].removeAbove(x.length-1);
                if (x[i].isBound()) bind(i);
            }
        } else {
            x[0].removeBelow(0);
            x[0].removeAbove(0);
        }

        for (int i = 0; i < x.length; i++){
            int fina = i;
            x[i].whenBind(() -> {
                bind(fina);
            });
        }

    }




    private void bind(int i) throws InconsistencyException {

        for (int j=0; j<x.length; j++){

            if (dest[j].getValue() == i) {
                dest[j].setValue(dest[x[i].getMin()].getValue());
                lengthToDest[j].setValue(lengthToDest[j].getValue()+1+lengthToDest[x[i].getMax()].getValue());
            }

            if (orig[j].getValue() == x[i].getMax()) orig[j].setValue(orig[i].getValue());
        }

        if (lengthToDest[orig[i].getValue()].getValue() < x.length - 1){
            x[dest[i].getValue()].remove(orig[i].getValue());
        }
    }
}

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

public class Element1DVar extends Constraint {

    private final IntVar[] T;
    private final IntVar y;
    private final IntVar z;
    private int smallestTvar = 2147483647;
    private int biggestTvar = -2147483647;



    public Element1DVar(IntVar[] T, IntVar y, IntVar z) {
        super(y.getSolver());
        this.T = T;
        this.y = y;
        this.z = z;
    }

    @Override
    public void post() throws InconsistencyException {
        y.removeBelow(0);
        y.removeAbove(T.length-1);

        int[] yVals = y.getValues();
        for(int i=0; i<yVals.length; i++) {
            int[] Tvals = T[yVals[i]].getValues();
            boolean hasValueInZ = false;
            for(int j=0; j<Tvals.length; j++) {
                if(z.contains(Tvals[j]))
                    hasValueInZ = true;
            }
            if(!hasValueInZ)
                y.remove(yVals[i]);
        }

        yVals = y.getValues();
        for(int i=0; i<yVals.length; i++) {
            int[] Tvals = T[yVals[i]].getValues();
            for(int j=0; j<Tvals.length; j++) {
                if(!z.contains(Tvals[j])){
                    T[yVals[i]].remove(Tvals[j]);
                }
            }
        }
        int[] zVals = z.getValues();
        for(int i=0; i<zVals.length; i++) {
            boolean existsInT = false;
            for(int j=0; j<yVals.length; j++){
                if(T[yVals[j]].contains(zVals[i])) {
                    existsInT = true;
                    break;
                }
            }
            if(!existsInT)
                z.remove(zVals[i]);
        }

        yVals = y.getValues();
        for(int i=0; i<yVals.length; i++) {
            if(T[yVals[i]].getMax()>biggestTvar){
                biggestTvar = T[yVals[i]].getMax();
            }
            if(T[yVals[i]].getMin()<smallestTvar){
                smallestTvar = T[yVals[i]].getMin();
            }
        }
        z.removeAbove(biggestTvar);
        z.removeBelow(smallestTvar);

        y.propagateOnDomainChange(this);
        z.propagateOnBoundChange(this);
    }

    @Override
    public void propagate() throws InconsistencyException {
        int[] yVals = y.getValues();
        for(int i=0; i<yVals.length; i++) {
            if(T[yVals[i]].getMin()>z.getMax()) {
                y.remove(yVals[i]);
            } else if(T[yVals[i]].getMax()<z.getMin()) {
                y.remove(yVals[i]);
            }
        }

        yVals = y.getValues();
        for(int i=0; i<yVals.length; i++) {
            T[yVals[i]].removeBelow(z.getMin());
            T[yVals[i]].removeAbove(z.getMax());
        }

        yVals = y.getValues();
        int newsmallestTvar = 2147483647;
        int newbiggestTvar = -2147483647;
        for(int i=0; i<yVals.length; i++) {
            if(T[yVals[i]].getMax()>newbiggestTvar){
                newbiggestTvar = T[yVals[i]].getMax();
            }
            if(T[yVals[i]].getMin()<newsmallestTvar){
                newsmallestTvar = T[yVals[i]].getMin();
            }
        }
        if(newbiggestTvar<biggestTvar) {
            z.removeAbove(newbiggestTvar);
            biggestTvar = newbiggestTvar;
        }
        if(newsmallestTvar>smallestTvar) {
            z.removeBelow(newsmallestTvar);
            smallestTvar = newsmallestTvar;
        }
    }
}
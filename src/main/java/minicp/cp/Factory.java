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

package minicp.cp;

import minicp.cp.constraints.DifferentVar;
import minicp.cp.constraints.Element2D;
import minicp.cp.constraints.Sum;
import minicp.cp.core.*;
import minicp.util.InconsistencyException;


import java.util.Set;

public class Factory {

    static public IntVar mul(IntVar x, int a) {
        if (a == 0) return makeIntVar(x.getSolver(),0,0);
        else return new IntVarViewMul(x,a);
    }

    static public IntVar minus(IntVar x) {
        return new IntVarViewOpposite(x);
    }

    static public IntVar makeIntVar(Solver cp, int n) {
        return new IntVarImpl(cp,n);
    }

    static public IntVar makeIntVar(Solver cp, int min, int max) {
        return new IntVarImpl(cp,min,max);
    }

    static public IntVar makeIntVar(Solver cp, Set<Integer> values) {
        return new IntVarImpl(cp,values);
    }

    // Factory
    static public IntVar[] makeIntVarArray(Solver cp, int n, int sz) {
        IntVar[] rv = new IntVar[n];
        for (int i = 0; i < n; i++)
            rv[i] = makeIntVar(cp, sz);
        return rv;
    }
    static public Constraint makeDifferentVar(IntVar x, IntVar y,int c) { return new DifferentVar(x,y,c);}
    static public Constraint makeDifferentVar(IntVar x, IntVar y)       { return new DifferentVar(x,y,0);}


    static public IntVar element(int[][] T, IntVar x, IntVar y) throws InconsistencyException {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < T.length; i++) {
            for (int j = 0; j < T[i].length; j++) {
                min = Math.min(min,T[i][j]);
                max = Math.max(max,T[i][j]);
            }
        }
        IntVar z = makeIntVar(x.getSolver(),min,max);
        x.getSolver().add(new Element2D(T,x,y,z));
        return z;
    }

    static public IntVar sum(IntVar[] x) throws InconsistencyException {
        int sumMin = 0;
        int sumMax = 0;
        for (int i = 0; i < x.length; i++) {
            sumMin += x[i].getMin();
            sumMax += x[i].getMax();
        }
        Solver cp = x[0].getSolver();
        IntVar s = makeIntVar(cp,sumMin,sumMax);
        cp.add(new Sum(x,s));
        return s;
    }


}

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

import minicp.engine.constraints.*;
import minicp.engine.core.*;
import minicp.search.Choice;
import minicp.search.DFSearch;
import minicp.util.InconsistencyException;


import java.util.Set;

public class Factory {

    static public Solver makeSolver() {
        return new Solver();
    }

    static public IntVar mul(IntVar x, int a) {
        if (a == 0) return makeIntVar(x.getSolver(),0,0);
        else if (a < 0) {
            return minus(new IntVarViewMul(x,-a));
        } else {
            return new IntVarViewMul(x,a);
        }
    }

    static public IntVar minus(IntVar x) {
        return new IntVarViewOpposite(x);
    }

    static public IntVar plus(IntVar x, int v) {
        return new IntVarViewOffset(x,v);
    }

    static public IntVar minus(IntVar x, int v) {
        return new IntVarViewOffset(x,-v);
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
    @FunctionalInterface
    public interface BodyClosure {
        IntVar call(int i);
    }
    static public IntVar[] makeIntVarArray(Solver cp,int n,BodyClosure body) {
        IntVar[] rv = new IntVar[n];
        for(int i = 0; i < n;i++)
            rv[i] = body.call(i);
        return rv;
    }

    static public DFSearch makeDfs(Solver cp, Choice branching) {
        return new DFSearch(cp.getTrail(),branching);
    }



    // -------------- constraints -----------------------

    static public void equal(IntVar x, int v) throws InconsistencyException {
        x.assign(v);
        x.getSolver().fixPoint();
    }

    static public void notEqual(IntVar x, int v) throws InconsistencyException {
        x.remove(v);
        x.getSolver().fixPoint();
    }

    static public Constraint notEqual(IntVar x, IntVar y) {
        return new NotEqual(x,y);
    }

    static public Constraint minimize(IntVar x, DFSearch dfs) {
        return new Minimize(x,dfs);
    }

    static public Constraint maximize(IntVar x, DFSearch dfs) {
        return new Minimize(minus(x),dfs);
    }

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
        x.getSolver().post(new Element2D(T,x,y,z));
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
        cp.post(new Sum(x,s));
        return s;
    }

    static public Constraint sum(IntVar[] x, IntVar y) {
        return new Sum(x,y);
    }

    static public Constraint sum(IntVar[] x, int y) {
        return new Sum(x,y);
    }

    static public Constraint allDifferent(IntVar[] x) {
        return new AllDifferentBinary(x);
    }


}

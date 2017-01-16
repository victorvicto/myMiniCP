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
import minicp.cp.core.*;
import minicp.util.NotImplementedException;

import java.util.Set;

public class Factory {

    static public IntVar mul(IntVar x, int a) {
        return new IntVarViewMul(x,a);
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
    static public Constraint makeDifferentVar(IntVar x,IntVar y,int c) { return new DifferentVar(x,y,c);}
    static public Constraint makeDifferentVar(IntVar x,IntVar y)       { return new DifferentVar(x,y,0);}
}

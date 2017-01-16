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


package minicp.cp.core;


import minicp.util.InconsistencyException;

public class IntVarViewMul implements IntVar {

    private final int a;
    private final IntVar x;

    public IntVarViewMul(IntVar x, int a) {
        assert(a > 0);
        this.a = a;
        this.x = x;
    }

    @Override
    public Solver getSolver() {
        return x.getSolver();
    }

    @Override
    public void whenDomainChange(ConstraintClosure.Closure c) {
        x.whenDomainChange(c);
    }

    @Override
    public void whenBind(ConstraintClosure.Closure c) {
        x.whenBind(c);
    }

    @Override
    public void whenBoundsChange(ConstraintClosure.Closure c) {
        x.whenBoundsChange(c);
    }

    @Override
    public void propagateOnDomainChange(Constraint c) {
        x.propagateOnDomainChange(c);
    }

    @Override
    public void propagateOnBind(Constraint c) {
        x.propagateOnBind(c);
    }

    @Override
    public void propagateOnBoundChange(Constraint c) {
        x.propagateOnBoundChange(c);
    }

    @Override
    public int getMin() {
        return a * x.getMin();
    }

    @Override
    public int getMax() {
        return a * x.getMax();
    }

    @Override
    public int getSize() {
        return x.getSize();
    }

    @Override
    public boolean isBound() {
        return x.isBound();
    }

    @Override
    public boolean contains(int v) {
        return (v % a != 0) ? false : x.contains(v / a);
    }

    @Override
    public void remove(int v) throws InconsistencyException {
        if (v % a == 0) {
            x.remove(v / a);
        }
    }

    @Override
    public void assign(int v) throws InconsistencyException {
        if (v % a == 0) {
            x.assign(v / a);
        } else {
            throw new InconsistencyException();
        }
    }

    @Override
    public int removeBelow(int v) throws InconsistencyException {
        return (x.removeBelow(ceilDiv(v, a))) * a;
    }

    @Override
    public int removeAbove(int v) throws InconsistencyException {
        return (x.removeAbove(floorDiv(v, a))) * a;
    }


    // Java's division always rounds to the integer closest to zero, but we need flooring/ceiling versions.
    private int floorDiv(int a, int b) {
        int q = a / b;
        return (a < 0 && q * b != a) ? q - 1 : q;
    }

    private int ceilDiv(int a, int b) {
        int q = a / b;
        return (a > 0 && q * b != a) ? q + 1 : q;
    }
}

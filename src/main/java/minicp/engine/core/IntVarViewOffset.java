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


package minicp.engine.core;


import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

public class IntVarViewOffset implements IntVar {

    private final IntVar x;
    private final int o;

    public IntVarViewOffset(IntVar x, int offset) { // y = x + o
        this.x = x;
        this.o = offset;
    }

    @Override
    public Solver getSolver() {
        return x.getSolver();
    }

    @Override
    public void whenDomainChange(ConstraintClosure.Filtering c) {
        x.whenDomainChange(c);
    }

    @Override
    public void whenBind(ConstraintClosure.Filtering c) {
        x.whenBind(c);
    }

    @Override
    public void whenBoundsChange(ConstraintClosure.Filtering c) {
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
        return x.getMin() + o;
    }

    @Override
    public int getMax() {
        return x.getMax() + o;
    }

    @Override
    public int getSize() {
        return x.getSize();
    }

    public int[] getValues() {
        int s = x.getSize();
        int[] d = x.getValues();
        for(int i=0; i<s; i++){
            d[i] += o;
        }
        return d;
    }

    @Override
    public int fillArray(int[] dest) {
        int s = x.getSize();
        int[] d = this.getValues();
        System.arraycopy(d, 0, dest, 0, s);
        return s;
    }

    @Override
    public boolean isBound() {
        return x.isBound();
    }

    @Override
    public boolean contains(int v) {
        return x.contains(v - o);
    }

    @Override
    public void remove(int v) throws InconsistencyException {
        x.remove(v - o);
    }

    @Override
    public void assign(int v) throws InconsistencyException {
        x.assign(v - o);
    }

    @Override
    public int removeBelow(int v) throws InconsistencyException {
        return x.removeBelow(v - o);
    }

    @Override
    public int removeAbove(int v) throws InconsistencyException {
        return x.removeAbove(v - o);
    }

}

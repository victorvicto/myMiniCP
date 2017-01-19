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

public class IntVarViewOpposite implements IntVar {

    private final IntVar x;

    public IntVarViewOpposite(IntVar x) {
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
        return -x.getMax();
    }

    @Override
    public int getMax() {
        return -x.getMin();
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
        return x.contains(-v);
    }

    @Override
    public void remove(int v) throws InconsistencyException {
        x.remove(-v);
    }

    @Override
    public void assign(int v) throws InconsistencyException {
        x.assign(-v);
    }

    @Override
    public int removeBelow(int v) throws InconsistencyException {
        return - x.removeAbove(-v);
    }

    @Override
    public int removeAbove(int v) throws InconsistencyException {
        return - x.removeBelow(-v);
    }

}

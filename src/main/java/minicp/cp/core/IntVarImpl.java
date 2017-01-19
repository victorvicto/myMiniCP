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

import minicp.reversible.ReversibleStack;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.security.InvalidParameterException;
import java.util.Set;

public class IntVarImpl implements IntVar {

    private Solver cp;
    private IntDomain domain;
    private ReversibleStack<Constraint> onDomain;
    private ReversibleStack<Constraint> onBind;
    private ReversibleStack<Constraint> onBounds;
    private DomainListener domListener = new DomainListener() {
        @Override
        public void bind() {
            scheduleAll(onDomain);
            scheduleAll(onBind);
        }

        @Override
        public void change(int domainSize) {
            scheduleAll(onDomain);
            if (domainSize == 1)
                scheduleAll(onBind);
        }

        @Override
        public void removeBelow(int domainSize) {
            scheduleAll(onBounds);
            if (domainSize == 1)
                scheduleAll(onBind);
        }

        @Override
        public void removeAbove(int domainSize) {
            scheduleAll(onBounds);
            if (domainSize == 1)
                scheduleAll(onBind);
        }
    };

    /**
     * Create a variable with the elements {0,...,n-1}
     * as initial domain
     * @param cp
     * @param n > 0
     */
    public IntVarImpl(Solver cp, int n) {
        this(cp,0,n-1);
    }

    /**
     * Create a variable with the elements {min,...,max}
     * as initial domain
     * @param cp
     * @param min
     * @param max >= min
     */
    public IntVarImpl(Solver cp, int min, int max) {
        if (min > max) throw new InvalidParameterException("at least one value in the domain");
        this.cp = cp;
        cp.registerVar(this);
        domain = new SparseSetDomain(cp.getState(),min,max);
        onDomain = new ReversibleStack<>(cp.getState());
        onBind  = new ReversibleStack<>(cp.getState());
        onBounds = new ReversibleStack<>(cp.getState());
    }

    public Solver getSolver() {
        return cp;
    }

    /**
     * Create a variable with values as initial domain
     * @param cp
     * @param values
     */
    public IntVarImpl(Solver cp, Set<Integer> values) {
        throw new NotImplementedException();
    }


    public boolean isBound() {
        return domain.getSize() == 1;
    }

    @Override
    public String toString() {
        return domain.toString();
    }

    public void whenDomainChange(ConstraintClosure.Closure c) {
        onDomain.push(new ConstraintClosure(cp,c));
    }

    public void whenBind(ConstraintClosure.Closure c) {
        onBind.push(new ConstraintClosure(cp,c));
    }

    public void whenBoundsChange(ConstraintClosure.Closure c) {
        onBounds.push(new ConstraintClosure(cp,c));
    }
    public void propagateOnDomainChange(Constraint c) {
        onDomain.push(c);
    }

    public void propagateOnBind(Constraint c) {
        onBind.push(c);
    }

    public void propagateOnBoundChange(Constraint c) { onBounds.push(c);}

    private void scheduleAll(ReversibleStack<Constraint> constraints) {
        for (int i = 0; i < constraints.size(); i++)
            cp.schedule(constraints.get(i));
    }

    public int getMin() {
        return domain.getMin();
    }

    public int getMax() {
        return domain.getMax();
    }

    public int getSize() {
        return domain.getSize();
    }

    public boolean contains(int v) {
        return domain.contains(v);
    }

    public void remove(int v) throws InconsistencyException {
        domain.remove(v, domListener);
    }

    public void assign(int v) throws InconsistencyException {
        domain.removeAllBut(v, domListener);
    }

    public int removeBelow(int v) throws InconsistencyException {
        return domain.removeBelow(v, domListener);
    }

    public int removeAbove(int v) throws InconsistencyException {
        return domain.removeAbove(v, domListener);
    }

}

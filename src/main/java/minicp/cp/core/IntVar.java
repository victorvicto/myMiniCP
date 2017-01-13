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

public class IntVar {

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
    public IntVar(Solver cp, int n) {
        if (n <= 0) throw new InvalidParameterException("at least one value in the domain");
        this.cp = cp;
        cp.registerVar(this);
        domain = new SparseSetDomain(cp.getContext(),n);
        onDomain = new ReversibleStack<>(cp.getContext());
        onBind  = new ReversibleStack<>(cp.getContext());
        onBounds = new ReversibleStack<>(cp.getContext());
    }

    public Solver getSolver() {
        return cp;
    }

    /**
     * Create a variable with the elements {min,...,max}
     * as initial domain
     * @param cp
     * @param min
     * @param max >= min
     */
    public IntVar(Solver cp, int min, int max) {
        if (min > max) throw new InvalidParameterException("at least one value in the domain");
        throw new NotImplementedException();
    }

    /**
     * Create a variable with values as initial domain
     * @param cp
     * @param values
     */
    public IntVar(Solver cp, Set<Integer> values) {
        throw new NotImplementedException();
    }


    /**
     * @return true if and only if the domain has a single value
     */
    public boolean isBound() {
        return domain.getSize() == 1;
    }

    @Override
    public String toString() {
        return domain.toString();
    }

    /**
     * Ask that c.propagate() is called whenever the domain change
     * of this variable changes
     * @param c
     */
    public void whenDomainChange(ConstraintClosure.Closure c) {
        onDomain.push(new ConstraintClosure(cp,c));
    }

    /**
     * Ask that c.propagate() is called whenever the domain
     * of this variable is reduced to a single value
     * @param c
     */
    public void whenBind(ConstraintClosure.Closure c) {
        onBind.push(new ConstraintClosure(cp,c));
    }

    /**
     * Ask that c.propagate() is called whenever
     * the max or min value of the domain of this variable changes
     * @param c
     */
    public void whenBoundsChange(ConstraintClosure.Closure c) throws InconsistencyException {
        onBounds.push(new ConstraintClosure(cp,c));
    }

    /**
     * Ask that c.propagate() is called whenever the domain change
     * of this variable changes
     * @param c
     */
    public void propagateOnDomainChange(Constraint c) {
        onDomain.push(c);
    }

    /**
     * Ask that c.propagate() is called whenever the domain
     * of this variable is reduced to a single value
     * @param c
     */
    public void propagateOnBind(Constraint c) {
        onBind.push(c);
    }

    /**
     * Ask that c.propagate() is called whenever
     * the max or min value of the domain of this variable changes
     * @param c
     */
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

    /**
     * @param v
     * @return true iff the value v is the domain
     */
    public boolean contains(int v) { return domain.contains(v); }

    /**
     * Remove the value v from the domain
     * @param v
     * @throws InconsistencyException
     */
    public void remove(int v) throws InconsistencyException {
        domain.remove(v, domListener);
    }

    /**
     * Assign the value v i.e.
     * remove every value different from v
     * @param v
     * @throws InconsistencyException
     */
    public void assign(int v) throws InconsistencyException {
        domain.removeAllBut(v, domListener);
    }

    /**
     * Remove all the values < value
     * @param value
     * @return the new minimum
     * @throws InconsistencyException
     */
    public int removeBelow(int value) throws InconsistencyException {
        throw new NotImplementedException();
    }

    /**
     * Remove all the values > valu
     * @param value
     * @return the new maximum
     * @throws InconsistencyException
     */
    public int removeAbove(int value) throws InconsistencyException {
        throw new NotImplementedException();
    }

}

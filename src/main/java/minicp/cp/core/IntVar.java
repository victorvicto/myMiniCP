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


import minicp.reversible.ReversibleSparseSet;
import minicp.reversible.ReversibleStack;
import minicp.util.NotImplementedException;


import java.security.InvalidParameterException;
import java.util.Set;

public class IntVar {

    private Engine engine;
    private ReversibleSparseSet domain;
    private ReversibleStack<Constraint> onDomainChange;
    private ReversibleStack<Constraint> onBind;

    /**
     * Create a variable with the elements {0,...,n-1}
     * as initial domain
     * @param cp
     * @param n > 0
     */
    public IntVar(Solver cp, int n) {
        if (n <= 0) throw new InvalidParameterException("at least one value in the domain");
        engine = cp.getEngine();
        engine.registerVar(this);
        domain = new ReversibleSparseSet(engine.getContext(),n);
        onDomainChange = new ReversibleStack<Constraint>(engine.getContext());
        onBind = new ReversibleStack<Constraint>(engine.getContext());
    }

    /**
     * Create a variable with the elements {min,...,max}
     * as initial domain
     * @param cp
     * @param min
     * @param max >= min
     */
    public IntVar(Solver cp, int min, int max) throws Status {
        if (min > max) throw new InvalidParameterException("at least one value in the domain");
        throw new Status(Status.Type.NotImplemented);
    }

    /**
     * Create a variable with values as initial domain
     * @param cp
     * @param values
     */
    public IntVar(Solver cp, Set<Integer> values) throws Status {
        throw new Status(Status.Type.NotImplemented);
    }

    /**
     * Ask that c.propagate() is called whenever the domain change
     * of this variable changes
     * @param c
     */
    public void propagateOnDomainChange(Constraint c) {
        onDomainChange.push(c);
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
    public void propagateOnBoundChange(Constraint c) throws Status {
        throw new Status(Status.Type.NotImplemented);
    }

    private void enQueueAll(ReversibleStack<Constraint> constraints) {
        for (int i = 0; i < constraints.size(); i++) {
            engine.enqueue(constraints.get(i));
        }
    }

    public Engine getEngine() { return engine; }

    public int getMin() { return domain.getMin(); };

    public int getMax() { return domain.getMax(); }

    public int getSize() { return domain.getSize(); }

    /**
     * @param v
     * @return true iff the value v is the domain
     */
    public boolean contains(int v) { return domain.contains(v); }

    /**
     * Remove the value v from the domain
     * @param v
     * @throws Status
     */
    public void remove(int v) throws Status {
        if (domain.contains(v)) {
            domain.remove(v);
            enQueueAll(onDomainChange);
            if (domain.getSize() == 1) {
                enQueueAll(onBind);
            }
        }
        if (domain.isEmpty()) throw new Status(Status.Type.Failure);
    }

    /**
     * Assign the value v i.e.
     * remove every value different from v
     * @param v
     * @throws Status
     */
    public void assign(int v) throws Status {
        if (domain.contains(v)) {
            if (domain.getSize() != 1) {
                domain.removeAllBut(v);
                enQueueAll(onDomainChange);
                enQueueAll(onBind);
            }
        }
        else {
            domain.removeAll();
            throw new Status(Status.Type.Failure);
        }
    }

    /**
     * Remove all the values < value
     * @param value
     * @return the new minimum
     * @throws Status
     */
    public int removeBelow(int value) throws Status {
        throw new Status(Status.Type.NotImplemented);
    }

    /**
     * Remove all the values > valu
     * @param value
     * @return the new maximum
     * @throws Status
     */
    public int removeAbove(int value) throws Status {
        throw new Status(Status.Type.NotImplemented);
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
}

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
import minicp.search.DFSearch;
import minicp.search.Inconsistency;
import minicp.util.NotImplementedException;


import java.security.InvalidParameterException;
import java.util.Set;

public class IntVar {

    Store store;
    ReversibleSparseSet domain;
    ReversibleStack<Constraint> onDomainChange;
    ReversibleStack<Constraint> onBind;

    /**
     * Create a variable with the elements {0,...,n-1}
     * as initial domain
     * @param store
     * @param n > 0
     */
    public IntVar(Store store, int n) {
        if (n <= 0) throw new InvalidParameterException("at least one value in the domain");
        this.store = store;
        domain = new ReversibleSparseSet(store,n);
        onDomainChange = new ReversibleStack<Constraint>(store);
        onBind = new ReversibleStack<Constraint>(store);
    }

    /**
     * Create a variable with the elements {min,...,max}
     * as initial domain
     * @param store
     * @param min
     * @param max >= min
     */
    public IntVar(Store store, int min, int max) {
        if (min > max) throw new InvalidParameterException("at least one value in the domain");
        throw new NotImplementedException();
    }

    /**
     * Create a variable with values as initial domain
     * @param store
     * @param values
     */
    public IntVar(Store store, Set<Integer> values) {
        throw new NotImplementedException();
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
    public void propagateOnBoundChange(Constraint c) {
        throw new NotImplementedException();
    }

    private void enQueueAll(ReversibleStack<Constraint> constraints) {
        for (int i = 0; i < constraints.size(); i++) {
            store.enqueue(constraints.get(i));
        }
    }

    public Store getStore() { return store; }

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
     * @throws Inconsistency
     */
    public void remove(int v) throws Inconsistency {
        if (domain.contains(v)) {
            domain.remove(v);
            enQueueAll(onDomainChange);
            if (domain.getSize() == 1) {
                enQueueAll(onBind);
            }
        }
        if (domain.isEmpty()) throw DFSearch.INCONSISTENCY;
    }

    /**
     * Assign the value v i.e.
     * remove every value different from v
     * @param v
     * @throws Inconsistency
     */
    public void assign(int v) throws Inconsistency {
        if (domain.contains(v)) {
            if (domain.getSize() != 1) {
                domain.removeAllBut(v);
                enQueueAll(onDomainChange);
                enQueueAll(onBind);
            }
        }
        else {
            domain.removeAll();
            throw DFSearch.INCONSISTENCY;
        }
    }

    /**
     * Remove all the values < value
     * @param value
     * @return the new minimum
     * @throws Inconsistency
     */
    public int removeBelow(int value) throws Inconsistency {
        throw new NotImplementedException();
    }

    /**
     * Remove all the values > valu
     * @param value
     * @return the new maximum
     * @throws Inconsistency
     */
    public int removeAbove(int value) throws Inconsistency {
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
}

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

import minicp.reversible.Trail;
import minicp.reversible.ReversibleSparseSet;
import minicp.util.InconsistencyException;


public class SparseSetDomain extends IntDomain {
    private ReversibleSparseSet domain;
    private int offset;


    public SparseSetDomain(Trail trail, int min, int max) {
        offset = min;
        domain = new ReversibleSparseSet(trail, max-min+1);
    }

    public int getMin() {
        return domain.getMin() + offset;
    }

    public int getMax() {
        return domain.getMax() + offset;
    }

    public int getSize() {
        return domain.getSize();
    }

    public boolean contains(int v) {
        return domain.contains(v - offset);
    }

    public boolean isBound() {
        return domain.getSize() == 1;
    }

    public void remove(int v, DomainListener x) throws InconsistencyException {
        if (domain.contains(v - offset)) {
            boolean maxChanged = getMax() == v;
            boolean minChanged = getMin() == v;
            domain.remove(v - offset);
            if (domain.getSize() == 0) throw new InconsistencyException();
            x.change(domain.getSize());
            if (maxChanged) x.removeAbove(domain.getSize());
            if (minChanged) x.removeBelow(domain.getSize());
            if (domain.getSize() == 1) x.bind();
        }
    }

    public void removeAllBut(int v, DomainListener x) throws InconsistencyException {
        if (domain.contains(v - offset)) {
            if (domain.getSize() != 1) {
                boolean maxChanged = getMax() == v;
                boolean minChanged = getMin() == v;
                domain.removeAllBut(v - offset);
                x.bind();
                x.change(domain.getSize());
                if (maxChanged) x.removeAbove(domain.getSize());
                if (minChanged) x.removeBelow(domain.getSize());
            }
        }
        else {
            domain.removeAll();
            throw new InconsistencyException();
        }
    }

    public int removeBelow(int value, DomainListener x) throws InconsistencyException {
        if (domain.getMin() + offset < value) {
            domain.removeBelow(value - offset);
            x.removeBelow(domain.getSize());
            x.change(domain.getSize());
        }
        if (domain.getSize() == 0)  throw new InconsistencyException();
        else return domain.getMin() + offset;
    }

    public int removeAbove(int value, DomainListener x) throws InconsistencyException {
        if (domain.getMax() + offset > value) {
            domain.removeAbove(value - offset);
            x.removeAbove(domain.getSize());
            x.change(domain.getSize());
        }
        if (domain.getSize() == 0)  throw new InconsistencyException();
        else return domain.getMax() + offset;
    }
}

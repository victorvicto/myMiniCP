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

import minicp.reversible.ReversibleState;
import minicp.reversible.ReversibleSparseSet;
import minicp.util.InconsistencyException;


public class SparseSetDomain implements IntDomain {
    private ReversibleSparseSet domain;
    private int offset;

    public SparseSetDomain(ReversibleState reversibleState, int n) {
        domain = new ReversibleSparseSet(reversibleState, n);
    }


    public SparseSetDomain(ReversibleState reversibleState, int min, int max) {
        offset = min;
        domain = new ReversibleSparseSet(reversibleState, max-min+1);
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
        return domain.contains(v-offset);
    }

    public boolean isBound() {
        return domain.getSize() == 1;
    }

    public String toString() {
        return domain.toString();
    }

    public void remove(int v, DomainListener x) throws InconsistencyException {
        if (domain.contains(v-offset)) {
            domain.remove(v-offset);
            if (domain.getSize() == 0) throw new InconsistencyException();
            x.change(domain.getSize());
        }
    }

    public void removeAllBut(int v, DomainListener x) throws InconsistencyException {
        if (domain.contains(v-offset)) {
            if (domain.getSize() != 1) {
                domain.removeAllBut(v-offset);
                x.bind();
            }
        }
        else {
            domain.removeAll();
            throw new InconsistencyException();
        }
    }

    public void removeBelow(int value, DomainListener x) throws InconsistencyException {
        domain.removeBelow(value-offset);
        x.removeBelow(domain.getSize());
    }

    public void removeAbove(int value, DomainListener x) throws InconsistencyException {
        domain.removeAbove(value-offset);
        x.removeAbove(domain.getSize());
    }
}

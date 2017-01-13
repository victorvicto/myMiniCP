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


public abstract class IntDomain {

    public abstract int getMin();

    public abstract  int getMax();

    public abstract  int getSize();

    public abstract  boolean contains(int v);

    public abstract  boolean isBound();

    public abstract  void remove(int v, DomainListener x) throws InconsistencyException;

    public abstract  void removeAllBut(int v, DomainListener x) throws InconsistencyException;

    public abstract  int removeBelow(int value, DomainListener x) throws InconsistencyException;

    public abstract  int removeAbove(int value, DomainListener x) throws InconsistencyException;

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("{");
        for (int i = getMin(); i <= getMax() - 1; i++) {
            if (contains((i))) {
                b.append(i);
                b.append(',');
            }
        }
        if (getSize() > 0) b.append(getMax());
        b.append("}");
        return b.toString();
    }
}

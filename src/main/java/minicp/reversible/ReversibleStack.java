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

package minicp.reversible;

import java.util.ArrayList;

public class ReversibleStack<E> {

    ReversibleInt size;
    ArrayList<E> stack;

    public ReversibleStack(ReversibleState rc) {
        size = new ReversibleInt(rc,0);
        stack = new ArrayList<E>();
    }

    public void push(E elem) {
        stack.add(size.getValue(),elem);
        size.increment();
    }

    public int size() { return size.getValue(); }

    public E get(int index) { return stack.get(index); }
}

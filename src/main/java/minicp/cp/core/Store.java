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


import minicp.search.DFSearch;
import minicp.search.DFSearchNode;
import minicp.search.Inconsistency;

import java.util.Stack;

public class Store extends DFSearchNode {

    Stack<Constraint> propagationQueue = new Stack<>();

    public void enqueue(Constraint c) {
        if (!c.inQueue) {
            c.inQueue = true;
            propagationQueue.add(c);
        }
    }


    public void fixPoint() throws Inconsistency {
        boolean failed = false;
        while (!propagationQueue.isEmpty()) {
            Constraint c = propagationQueue.pop();
            c.inQueue = false;
            if (!failed) {
                try { c.propagate(); }
                catch (Inconsistency e) {
                    failed = true;
                }
            }
        }
        if (failed) throw DFSearch.INCONSISTENCY;
    }


    public void add(Constraint c) throws Inconsistency {
        add(c,true);
    }

    public void add(Constraint c, boolean fixPoint) throws Inconsistency {
        c.setUp();
        if (fixPoint) {
            fixPoint();
        }
    }

}


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

import minicp.reversible.ReversibleContext;
import minicp.search.DFSearch;

import java.util.Stack;
import java.util.Vector;

public class Engine {
    private ReversibleContext context = new ReversibleContext();
    private Stack<Constraint> propagationQueue = new Stack<>();
    private Vector<IntVar>  vars = new Vector<>(2);
    public void registerVar(IntVar x) {
        vars.add(x);
    }

    public void push() { context.push();}
    public void pop()  { context.pop();}
    public ReversibleContext getContext() { return context;}


    public void enqueue(Constraint c) {
        if (!c.inQueue) {
            c.inQueue = true;
            propagationQueue.add(c);
        }
    }

    public void fixPoint() throws Status {
        boolean failed = false;
        while (!propagationQueue.isEmpty()) {
            Constraint c = propagationQueue.pop();
            c.inQueue = false;
            if (!failed) {
                try { c.propagate(); }
                catch (Status e) {
                    failed = true;
                }
            }
        }
        if (failed) throw new Status(Status.Type.Failure);
    }

    public void add(Constraint c) throws Status {
        add(c,true);
    }

    public void add(Constraint c, boolean enforceFixPoint) throws Status {
        c.setup();
        if (enforceFixPoint) fixPoint();
    }
}


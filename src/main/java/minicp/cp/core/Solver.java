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
import minicp.util.InconsistencyException;

import java.util.Stack;
import java.util.Vector;

public class Solver {

    private Trail trail = new Trail();
    private Stack<Constraint> propagationQueue = new Stack<>();
    private Vector<IntVar>  vars = new Vector<>(2);
    public void registerVar(IntVar x) {
        vars.add(x);
    }

    public void push() { trail.push();}
    public void pop()  { trail.pop();}

    public Trail getTrail() { return trail;}


    public void schedule(Constraint c) {
        if (!c.scheduled && c.isActive()) {
            c.scheduled = true;
            propagationQueue.add(c);
        }
    }

    public void fixPoint() throws InconsistencyException {
        boolean failed = false;
        while (!propagationQueue.isEmpty()) {
            Constraint c = propagationQueue.pop();
            c.scheduled = false;
            if (!failed) {
                try { c.propagate(); }
                catch (InconsistencyException e) {
                    failed = true;
                }
            }
        }
        if (failed) throw new InconsistencyException();
    }

    public void post(Constraint c) throws InconsistencyException {
        post(c,true);
    }

    public void post(Constraint c, boolean enforceFixPoint) throws InconsistencyException {
        c.post();
        if (enforceFixPoint) fixPoint();
    }

}


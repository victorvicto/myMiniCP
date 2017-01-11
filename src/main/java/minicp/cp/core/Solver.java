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
import minicp.search.Choice;

import java.util.LinkedList;
import java.util.List;

public class Solver {
    private Engine     engine = new Engine();
    private Explorer explorer = new Explorer();
    private List<SolutionListener> solutionListeners = new LinkedList<SolutionListener>();

    @FunctionalInterface
    public interface SolutionListener {
        void solutionFound();
    }
    public void onSolution(SolutionListener listener) {
        solutionListeners.add(listener);
    }
    public void notifySolutionFound() {
        solutionListeners.forEach(s -> s.solutionFound());
    }
    public Engine getEngine() { return engine;}
    // Delegations to the engine.
    public ReversibleContext getContext() { return engine.getContext();}
    public void add(Constraint c) throws Status { engine.add(c);}
    public void add(Constraint c, boolean enforceFixPoint) throws Status { engine.add(c,enforceFixPoint);}
    // Delegation to the explorer.
    public <T> Choice selectMin(T[] x,Explorer.Filter<T> p,Explorer.ValueFun<T> f,Explorer.BranchOn<T> body) {
        return explorer.selectMin(x,p,f,body);
    }
}

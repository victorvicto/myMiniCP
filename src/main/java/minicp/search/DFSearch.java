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

package minicp.search;


import java.util.LinkedList;
import java.util.List;

public class DFSearch {

    public static final Inconsistency INCONSISTENCY = new Inconsistency() {
        public Object feedBack() { return null;}
    };


    public static interface SolutionListener {
        public void solutionFound();
    }

    private List<SolutionListener> solutionListeners;
    private Branching branching;
    private DFSearchNode node;

    public DFSearch(DFSearchNode root, Branching branching) {
        this.node = root;
        this.branching = branching;
        solutionListeners = new LinkedList<SolutionListener>();
    }

    public void onSolution(SolutionListener listener) {
        solutionListeners.add(listener);
    }

    private void notifySolutionFound() {
        solutionListeners.forEach(s -> s.solutionFound());
    }

    public void start() {
        dfs();
    }

    public void dfs() {
        Alternative [] alternatives = branching.getAlternatives();
        if (alternatives.length == 0)
            notifySolutionFound();
        else {
            for (Alternative alt : alternatives) {
                node.push();
                try {
                    alt.execute();
                    dfs();
                } catch (Inconsistency e) {}
                node.pop();
            }
        }
    }

}

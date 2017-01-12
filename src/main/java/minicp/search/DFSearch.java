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

import minicp.cp.core.Status;
import minicp.reversible.ReversibleContext;

import java.util.LinkedList;
import java.util.List;

public class DFSearch {

    private Choice branching;
    private ReversibleContext context;

    private List<SolutionListener> solutionListeners = new LinkedList<SolutionListener>();


    // @Laurent:  I think solution listener must remain in DFSSearch
    // because this is the place where the solution concept is defined (solution = no alternative and not failure)
    @FunctionalInterface
    public interface SolutionListener {
        void solutionFound();
    }
    public DFSearch onSolution(SolutionListener listener) {
        solutionListeners.add(listener);
        return this;
    }
    public void notifySolutionFound() {
        solutionListeners.forEach(s -> s.solutionFound());
    }

    public DFSearch(ReversibleContext context, Choice branching) {
        this.context = context;
        this.branching = branching;
    }

    public SearchStatistics start(SearchLimit limit) {
        SearchStatistics statistics = new SearchStatistics();
        try {
            dfs(statistics,limit);
        } catch (StopSearchException e) {}
        return statistics;
    }

    public SearchStatistics start() {
        return start(statistics -> false);
    }


    private void dfs(SearchStatistics statistics, SearchLimit limit) {
        if (limit.stopSearch(statistics)) throw new StopSearchException();
        Alternative [] alternatives = branching.getAlternatives();
        if (alternatives.length == 0) {
            statistics.nSolutions++;
            notifySolutionFound();
        }
        else {
            for (Alternative alt : alternatives) {
                context.push();
                try {
                    statistics.nNodes++;
                    alt.execute();
                    dfs(statistics,limit);
                } catch (Status e) {
                    statistics.nFailures++;
                }
                context.pop();
            }
        }
    }
}

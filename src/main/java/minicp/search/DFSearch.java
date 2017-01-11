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

import minicp.cp.core.Solver;
import minicp.cp.core.Engine;
import minicp.cp.core.Status;

public class DFSearch {

    private Choice branching;
    private Solver       cps;
    private Engine       eng;

    public DFSearch(Solver cp, Choice branching) {
        this.cps = cp;
        this.eng = cps.getEngine();
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


    private void dfs(SearchStatistics statistics,SearchLimit limit) {
        if (limit.stopSearch(statistics)) throw new StopSearchException();
        Alternative [] alternatives = branching.getAlternatives();
        if (alternatives.length == 0) {
            statistics.nSolutions++;
            cps.notifySolutionFound();
        }
        else {
            for (Alternative alt : alternatives) {
                eng.push();
                try {
                    statistics.nNodes++;
                    alt.execute();
                    dfs(statistics,limit);
                } catch (Status e) {
                    statistics.nFailures++;
                }
                eng.pop();
            }
        }
    }
}

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

package minicp.cp.branchings;


import minicp.cp.core.IntVar;
import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.util.NotImplementedException;

import java.util.function.IntUnaryOperator;

public class BinaryFirstFail extends Branching {

    private IntVar[] x;
    private IntUnaryOperator heuristic;

    /**
     * Creates a binary first fail heuristic branching:
     * left branch: x[i] = v, right branch x[i] != v
     * The variable selected x[i] is the unbound one with min domain size
     * @param x
     * @param valueHeuristic: i -> v where i is the index of the variable in x, and
     *                        v is the value in the domain of x[i].
     */
    public BinaryFirstFail(IntVar [] x, IntUnaryOperator valueHeuristic) {
        this.x = x;
        this.heuristic = valueHeuristic;
    }

    /**
     * Creates a binary first fail heuristic branching:
     * left branch: x[i] = x[i].min, right branch x[i] != x[i].min
     * The variable selected x[i] is the unbound one with min domain size
     * @param x
     */
    public BinaryFirstFail(IntVar [] x) {
        this(x,i -> x[i].getMin());
    }

    public Alternative[] getAlternatives() {
        throw new NotImplementedException();
    }
}

/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2018. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.engine.constraints;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.BitSet;

import static minicp.cp.Factory.minus;

public class ShortTableCT extends Constraint {
    private IntVar[] x; //variables
    private int[][] table; //the table
    //supports[i][v] is the set of tuples supported by x[i]=v
    private BitSet[][] supports;

    /**
     * Table constraint. Assignment of x_0=v_0, x_1=v_1,... only valid if there exists a
     * row (v_0|*,v_1|*, ...) in the table.
     *
     * @param x     variables to constraint. x.length must be > 0.
     * @param table array of valid solutions (second dimension must be of same size as the array x)
     * @param star the symbol representing "any" value in the table
     */
    public ShortTableCT(IntVar[] x, int[][] table, int star) {
        super(x[0].getSolver());
        this.x = x;
        this.x = new IntVar[x.length];
        this.table = table;

        // Allocate supports
        supports = new BitSet[x.length][];
        for (int i = 0; i < x.length; i++) {
            this.x[i] = minus(x[i],x[i].getMin()); // map the variables domain to start at 0
            supports[i] = new BitSet[x[i].getMax() - x[i].getMin() + 1];
            for (int j = 0; j < supports[i].length; j++)
                supports[i][j] = new BitSet();
        }

        // Set values in supportedByVarVal, which contains all the tuples supported by each var-val pair
        // TODO: compute the supports (be careful, take into account the star value)
        throw new NotImplementedException("ShortTableCT");
    }

    @Override
    public void post() throws InconsistencyException {
        for (IntVar var : x)
            var.propagateOnDomainChange(this);
        propagate();
    }

    @Override
    public void propagate() throws InconsistencyException {
        // TODO: implement the filtering
        throw new NotImplementedException("ShortTableCT");
    }
}

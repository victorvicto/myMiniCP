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
 * Copyright (c)  2017. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.engine.constraints;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.util.BitSet;

public class TableCT extends Constraint {
    private IntVar[] x; //variables
    private int[] xOffset; //offset of the variable. Init to x[i].getMin()
    private int[][] table; //the table
    private BitSet[][] supportedByVarVal;   //supportedByVarVal[i][j] is the list of supported tuples by domain value j+xOffset[i] of variable x[i].

    // Advanced implementation variables, use these only when the first implementation is working
    /*
    private ReversibleInt[] lastSize; // lastSize[i] == last size of variable x[i]
    private ReversibleBitSet currentlySupported; // list of currently supported tuples
    */

    /**
     * Table constraint. Assignment of x_0=v_0, x_1=v_1,... only valid if there exists a
     * row (v_0, v_1, ...) in the table.
     *
     * @param x     variables to constraint. x.length must be > 0.
     * @param table array of valid solutions (second dimension must be of same size as the array x)
     */
    public TableCT(IntVar[] x, int[][] table) {
        super(x[0].getSolver());
        this.x = x;
        this.table = table;

        // Allocate supportedByVarVal
        supportedByVarVal = new BitSet[x.length][];
        xOffset = new int[x.length];
        for (int i = 0; i < x.length; i++) {
            xOffset[i] = x[i].getMin();
            supportedByVarVal[i] = new BitSet[x[i].getMax() - x[i].getMin() + 1];
            for (int j = 0; j < supportedByVarVal[i].length; j++)
                supportedByVarVal[i][j] = new BitSet();
        }

        // Set values in supportedByVarVal, which contains all the tuples supported by each var-val pair
        for (int i = 0; i < table.length; i++) //i is the index of the tuple (in table)
            for (int j = 0; j < x.length; j++) //j is the index of the current variable (in x)
                if (table[i][j] - xOffset[j] >= 0 && table[i][j] - xOffset[j] < supportedByVarVal[j].length)
                    supportedByVarVal[j][table[i][j] - xOffset[j]].set(i);

        // Advanced implementation. Uncomment this once the first part of the implementation is done and working
        /*
        // Init currentlySupported. Before the first propagation, we can safely assume they are all valid
        BitSet all1 = new BitSet(table.length);
        all1.flip(0, table.length);
        currentlySupported = new ReversibleBitSet(cp.getTrail(), table.length, all1);

        // Add some watchers on the size of the variables domains. With this, we will know when a variable has been
        // modified since the last propagation in this tree branch.
        lastSize = new ReversibleInt[x.length];
        for(int i = 0; i < x.length; i++)
            lastSize[i] = new ReversibleInt(cp.getTrail(), -1);
        */
    }

    @Override
    public void post() throws InconsistencyException {
        for (IntVar var : x)
            var.propagateOnDomainChange(this);
        propagate();
    }

    @Override
    public void propagate() throws InconsistencyException {
        // For each var, compute the tuples supported by the var
        // Intersection of the tuples supported by each var is the list of supported tuples
        // Then check if each var/val support a tuples. If not, remove the val.
        // TODO
        throw new NotImplementedException("TableCT");
    }
}

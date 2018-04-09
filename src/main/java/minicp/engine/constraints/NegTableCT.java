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

import java.util.ArrayList;
import java.util.BitSet;

import static minicp.cp.Factory.minus;

public class NegTableCT extends Constraint {
    private IntVar[] x; //variables
    private int[][] table; //the table
    // conficts[i][v] is the set of tuples such that x[i]=v
    private BitSet[][] conflicts;

    /**
     * Negative Table constraint.
     * Assignment of x_0=v_0, x_1=v_1,... is forbidden
     * for every row (v_0, v_1, ...) in the table.
     *
     * @param x     variables to constraint. x.length must be > 0.
     * @param table array of forbidden solutions (second dimension must be of same size as the array x)
     */
    public NegTableCT(IntVar[] x, int[][] table) {
        super(x[0].getSolver());
        this.x = new IntVar[x.length];


        // remove duplicate (the negative ct algo does not support it)
        ArrayList<int[]> tableList = new ArrayList<>();
        boolean [] duplicate = new boolean[table.length];
        for (int i = 0; i < table.length; i++) {
            if (!duplicate[i]) {
                tableList.add(table[i]);
                for (int j = i + 1; j < table.length; j++) {
                    if (i != j & !duplicate[j]) {
                        boolean same = true;
                        for (int k = 0; k < x.length; k++) {
                            same &= table[i][k] == table[j][k];
                        }
                        if (same) {
                            duplicate[j] = true;
                        }
                    }
                }
            }
        }
        this.table = tableList.toArray(new int[0][]);

        // Allocate conflicts
        conflicts = new BitSet[x.length][];
        for (int i = 0; i < x.length; i++) {
            // map the variables domain to start at index 0
            this.x[i] = minus(x[i],x[i].getMin());
            conflicts[i] = new BitSet[x[i].getMax() - x[i].getMin() + 1];
            for (int j = 0; j < conflicts[i].length; j++)
                conflicts[i][j] = new BitSet();
        }

        for (int i = 0; i < this.table.length; i++) { //i is the index of the tuple (in table)
            for (int j = 0; j < x.length; j++) { //j is the index of the current variable (in x)
                if (x[j].contains(this.table[i][j])) {
                    conflicts[j][this.table[i][j] - x[j].getMin()].set(i);
                }
            }
        }
    }

    @Override
    public void post() throws InconsistencyException {
        for (int i = 0; i < x.length; i++) {
            for (int v = x[i].getMin(); v <= x[i].getMax(); v++) {
                if (x[i].contains(v)) {
                    // The condition for removing the value v from x[i] is to check if
                    //         there is no intersection between supportedTuples and the support[i][v]
                    if (conflicts[i][v].nextClearBit(0) >= table.length)
                        x[i].remove(v);
                }
            }
        }
    }

    @Override
    public void propagate() throws InconsistencyException {
        // Bit-set of tuple indices all set to 0
        BitSet supportedTuples = new BitSet(table.length);
        //Set all bits to 1
        supportedTuples.flip(0,table.length);

        // compute supportedTuples as
        //       supportedTuples = (supports[0][x[0].getMin()] | ... | supports[0][x[0].getMax()] ) & ... &
        //                         (supports[x.length][x[0].getMin()] | ... | supports[x.length][x[0].getMax()] )
        // "|" is the bitwise "or" method on BitSet
        // "&" is bitwise "and" method on BitSet

        BitSet curSupport = new BitSet(table.length);

        for (int i = 0; i < x.length; i++) {
            curSupport.set(0,table.length, false);
            for (int j = x[i].getMin(); j <= x[i].getMax() ; j++) {
                if(x[i].contains(j))
                    curSupport.or(conflicts[i][j]);
            }
            supportedTuples.or(curSupport);
        }

        for (int i = 0; i < x.length; i++) {
            for (int v = x[i].getMin(); v <= x[i].getMax(); v++) {
                if (x[i].contains(v)) {
                    // The condition for removing the value v from x[i] is to check if
                    //         there is no intersection between supportedTuples and the support[i][v]
                    if (conflicts[i][v].nextClearBit(0) >= table.length)
                        x[i].remove(v);
                }
            }
        }
    }
}

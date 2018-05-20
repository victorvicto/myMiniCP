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

import minicp.engine.core.BoolVar;
import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;

import static minicp.cp.Factory.makeBoolVar;
import static minicp.cp.Factory.makeIntVarArray;
import static minicp.cp.Factory.plus;
import static minicp.cp.Factory.minus;

import java.util.Arrays;

public class Disjunctive extends Constraint {

    private final IntVar[] start;
    private final int[] duration;
    private final IntVar[] end;
    private final boolean postMirror;


    public Disjunctive(IntVar[] start, int[] duration) throws InconsistencyException {
        this(start, duration, true);
    }

    public Disjunctive(IntVar[] start, int[] duration, boolean postMirror) throws InconsistencyException {
        super(start[0].getSolver());
        this.start = start;
        this.duration = duration;
        this.end = makeIntVarArray(cp,start.length, i -> plus(start[i],duration[i]));
        this.postMirror = postMirror;
    }

    @Override
    public void post() throws InconsistencyException {
        BoolVar[][] bij = new BoolVar[start.length][start.length];
        BoolVar[][] bji = new BoolVar[start.length][start.length];

        for (int i = 0; i < start.length; i++) {
            for (int j = i+1; j < start.length; j++) {
                bij[i][j] = makeBoolVar(cp);
                bji[i][j] = makeBoolVar(cp);
            }
        }

        for (int i = 0; i < start.length; i++) {
            for (int j = i+1; j < start.length; j++) {
                cp.post(new IsLessOrEqualVar(bij[i][j], end[i], start[j]));
                cp.post(new IsLessOrEqualVar(bji[i][j], end[j], start[i]));
                cp.post(new NotEqual(bij[i][j],bji[i][j]));
            }
        }


        if (postMirror) {
            IntVar[] startMirror = makeIntVarArray(cp, start.length, i -> minus(end[i]));
            cp.post(new Disjunctive(startMirror, duration, false), false);
        }

        // index for theta tree
        int makeThisSolverGreatAgain = 3;
        if(!postMirror){
            makeThisSolverGreatAgain = -makeThisSolverGreatAgain;
        }
        ArrayIndexComparator comparatorT = new ArrayIndexComparator(start, duration,makeThisSolverGreatAgain);
        Integer[] indT = comparatorT.createIndexArray();
        Arrays.sort(indT, comparatorT);

        //overload checker
        ArrayIndexComparator comparator = new ArrayIndexComparator(start, duration,0);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        ThetaTree thetaTree = new ThetaTree(start.length);

        for (int i = 0; i < start.length; i++) {
            thetaTree.insert(indT[i],start[indexes[i]].getMin()+duration[indexes[i]],duration[indexes[i]]);
            if(thetaTree.getECT()>start[indexes[i]].getMax()+duration[indexes[i]]) {
                throw new InconsistencyException();
            }
        }


        //detect precedence
        ArrayIndexComparator comparator1 = new ArrayIndexComparator(start, duration,1);
        Integer[] indexes1 = comparator1.createIndexArray();
        Arrays.sort(indexes1, comparator1);

        ArrayIndexComparator comparator2 = new ArrayIndexComparator(start, duration,2);
        Integer[] indexes2 = comparator2.createIndexArray();
        Arrays.sort(indexes2, comparator2);

        int j = 0; // to iterate on indexes1
        boolean notFinished = true;

        ThetaTree thetaTree2 = new ThetaTree(start.length);
        int[] estPrime = new int[start.length];


        for (int i = 0; i<start.length; i++) {
            while((start[indexes2[i]].getMin()+duration[indexes2[i]]>start[indexes1[j]].getMax()) && notFinished) {
                thetaTree2.insert(j,start[indexes1[j]].getMin()+duration[indexes1[j]],duration[indexes1[j]]);
                j++;
                if (j==start.length) {
                    notFinished = false;
                    j--;
                }
            }
            thetaTree2.remove(i);
            estPrime[indexes2[i]] = Math.max(start[indexes2[i]].getMin(), thetaTree2.getECT());
        }
        for (int i = 0; i<start.length; i++) {
            start[indexes2[i]].removeBelow(estPrime[indexes2[i]]);
        }


        // TODO 6: add the Not-Last algorithm

        // Not last
        int[] lctPrime = new int[start.length];


        ArrayIndexComparator comparator3 = new ArrayIndexComparator(start, duration,1);
        Integer[] indexes3 = comparator3.createIndexArray();
        Arrays.sort(indexes3, comparator3);

        ArrayIndexComparator comparator4 = new ArrayIndexComparator(start, duration,0);
        Integer[] indexes4 = comparator4.createIndexArray();
        Arrays.sort(indexes4, comparator4);

        for (int i = 0;i<start.length;i++) {
            lctPrime[indexes4[i]] = start[indexes4[i]].getMax() + duration[indexes4[i]];
        }

        int k = 0; // iterates on indexes3
        j = -1;

        ThetaTree thetaTree3 = new ThetaTree(start.length);
        boolean fin = false;
        // i iterates on indexes 4
        for (int i = 0;i<start.length;i++) {
            while (start[indexes4[i]].getMax() + duration[indexes4[i]] > start[indexes3[k]].getMax() && !fin) {
                thetaTree3.insert(k,start[indexes3[k]].getMin()+duration[indexes3[k]],duration[indexes3[k]]);
                j = k;
                k++;
                if (k == start.length) {
                    fin = true;
                    k--;
                }
            }
            thetaTree3.remove(i);
            if (thetaTree3.getECT() > start[indexes4[i]].getMax()){
                lctPrime[indexes4[i]] = Math.min(start[indexes4[i]].getMax()+duration[indexes4[i]],start[indexes3[k-1]].getMax());
            }
        }
        for (int i = 0; i<start.length; i++) {
            start[indexes4[i]].removeAbove(lctPrime[indexes4[i]]-duration[indexes4[i]]);
        }



        // TODO 7 (optional, for a bonus): implement the Lambda-Theta tree and implement the Edge-Finding
    }

}

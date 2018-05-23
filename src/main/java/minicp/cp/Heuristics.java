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

package minicp.cp;


import minicp.engine.constraints.LessOrEqual;
import minicp.engine.core.BoolVar;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.Alternative;
import minicp.search.Choice;
import minicp.search.ChoiceCombinator;
import minicp.util.OrderNode;
import minicp.util.OrderQueue;

import java.util.ArrayList;
import java.util.List;

import static minicp.cp.Factory.*;
import static minicp.search.Selector.*;

public class Heuristics {

    public static Choice firstFail(IntVar... x) {
        Solver cp = x[0].getSolver();
        return selectMin(x,
                xi -> xi.getSize() > 1,
                xi -> xi.getSize(),
                xi -> {
                    int v = xi.getMin();
                    return branch(
                            () -> {
                                equal(xi,v);
                            },
                            () -> {
                                notEqual(xi,v);
                            }
                    );
                }
        );
    }

    public static Choice firstFailTimeLim(IntVar[] x, long StartTime) {
        Solver cp = x[0].getSolver();
        return selectMin(x,
                xi -> xi.getSize() > 1,
                xi -> xi.getMin(),
                xi -> {
                    long cur = System.currentTimeMillis();
                    if (cur-119998<StartTime) {
                        int v = xi.getMin();
                        return branch(
                                () -> {
                                    equal(xi, v);
                                },
                                () -> {
                                    notEqual(xi, v);
                                }
                        );
                    } else {
                        return branch(() -> {equal(xi,-1);});
                    }
                }
        );
    }

    public static Choice LNSTimeLim(OrderNode[] n, OrderQueue oq, long StartTime) {
        Solver cp = n[0].getVar().getSolver();
        return selectMin(n,
                xi -> xi.getVar().getSize() > 1,
                xi -> xi.getVar().getSize(),
                xi -> {
                    long cur = System.currentTimeMillis();
                    if (cur-115000<StartTime) {
                        List<Alternative> a = new ArrayList<>();
                        a.add(() -> {
                            xi.addNext(oq.getFirst());
                            oq.setFirst(xi.getIndex());
                        });

                        int next = oq.getFirst();
                        while (next!=-1) {
                            int finalNext = next;
                            a.add(() -> {
                                n[finalNext].addNext(xi.getIndex());
                            });
                            next = n[next].getNext();
                        }
                        return branch(a.toArray(new Alternative[a.size()]));
                    } else {
                        return branch(() -> {
                            equal(xi.getVar(),-1);
                        });
                    }
                }
        );
    }

    public static Choice noSymetry(IntVar[] x, BoolVar[][] inSlab) {
        Solver cp = x[0].getSolver();
        return selectMin(x,
                xi -> xi.getSize() > 1,
                xi -> xi.getSize(),
                xi -> {
                    List<Alternative> a = new ArrayList<>();
                    int v = xi.getMin();
                    for (int j=v; j<inSlab.length; j++) {
                        int tot = 0;
                        for (int i=0; i<inSlab[j].length; i++){
                            if (inSlab[i][j].isTrue())
                                tot++;
                        }
                        if (tot!=0) {
                            v++;
                        } else {
                            break;
                        }
                    }
                    for (int i=xi.getMin(); i<=v;i++) {
                        int j = i;
                        a.add(() -> {
                            equal(xi, j);
                        });
                    }
                    return branch(a.toArray(new Alternative[a.size()]));
                }
        );
    }

    /**
     *
     * @param choices
     * @return A choice that is only returns an empty alternative when all the choices return empty, otherwise it
     *         return the alternatives generated by the first non empty one.
     */
    public static Choice and(Choice ... choices) {
        return new ChoiceCombinator(choices);
    }


}

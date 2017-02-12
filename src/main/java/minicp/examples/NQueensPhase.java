package minicp.examples;

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;

import java.util.Arrays;

import static minicp.cp.Factory.*;
import static minicp.cp.Factory.notEqual;
import static minicp.search.Selector.branch;
import static minicp.search.Selector.selectMin;
import minicp.util.Box;

/**
 * Created by ldm on 2/12/17.
 */
public class NQueensPhase {
    public static void main(String[] args) throws InconsistencyException {

        int n = 8;
        Solver cp = makeSolver();

        IntVar[] q = makeIntVarArray(cp, n, n);
        IntVar[] ql = makeIntVarArray(cp,n,i -> plus(q[i],i));
        IntVar[] qr = makeIntVarArray(cp,n,i -> plus(q[i],-i));
        IntVar[]  q1 = makeIntVarArray(cp,n/2,i -> q[i]);
        IntVar[]  q2 = makeIntVarArray(cp,n/2,i -> q[i+4]);

        cp.post(allDifferent(q));
        cp.post(allDifferent(ql));
        cp.post(allDifferent(qr));
        Box<Integer> nbSol = new Box<>(0);
        SearchStatistics stats = makeDfs(cp,
                selectMin(q1,
                        qi -> qi.getSize() > 1,
                        qi -> qi.getSize(),
                        qi -> {
                            int v = qi.getMin();
                            return branch(() -> equal(qi,v),
                                          () -> notEqual(qi,v)
                                    );
                        }
                )
        ).onSolution(() ->
                makeDfs(cp,selectMin(q2,
                        qi -> qi.getSize() > 1,
                        qi -> qi.getSize(),
                        qi -> {
                            int v = qi.getMin();
                            return branch(() -> equal(qi,v),
                                          () -> notEqual(qi,v)
                                    );
                        })
                ).onSolution(() -> {
                    nbSol.set(nbSol.get() + 1);
                    System.out.println("solution:"+ Arrays.toString(q));
                }).start()
        ).start();

        System.out.format("#Solutions: %s\n", nbSol);
    }
}


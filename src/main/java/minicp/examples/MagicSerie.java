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

/**
 * Created by ldm on 2/12/17.
 */
public class MagicSerie {
    public static void main(String[] args) throws InconsistencyException {

        int n = 8;
        Solver cp = makeSolver();

        IntVar[] s = makeIntVarArray(cp, n, n);

        for (int i = 0; i < n; i++) {
            final int fi = i;
            cp.post(sum(all(0,n-1,j -> booleqc(s[j],fi)),s[i]));
        }
        cp.post(sum(all(0,n-1,i -> mul(s[i],i)),n));
        cp.post(sum(all(0,n-1,i -> mul(s[i],i-1)),0));

        SearchStatistics stats = makeDfs(cp,
                selectMin(s,
                        si -> si.getSize() > 1,
                        si -> si.getSize(),
                        si -> {
                            int v = si.getMin();
                            return branch(() -> equal(si,v),
                                          () -> notEqual(si,v));
                        }
                )
        ).onSolution(() ->
                System.out.println("solution:"+ Arrays.toString(s))
        ).start();

        System.out.format("#Solutions: %s\n", stats.nSolutions);
        System.out.format("Statistics: %s\n", stats);

    }}

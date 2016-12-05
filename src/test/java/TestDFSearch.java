
import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleInt;
import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.search.DFSearch;
import minicp.search.DFSearchNode;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestDFSearch {

    @Test
    public void testDFS() {
        DFSearchNode node = new DFSearchNode();
        ReversibleInt i = new ReversibleInt(node,0);
        boolean [] values = new boolean[4];

        Branching myBranching = new Branching() {
            @Override
            public Alternative[] getAlternatives() {
                if (i.getValue() >= values.length)
                    return SOLUTION;
                else return branch (
                        ()-> {
                            // left branch
                            values[i.getValue()] = false;
                            i.increment();
                        },
                        ()-> {
                            // right branch
                            values[i.getValue()] = true;
                            i.increment();
                        }
                );
            }
        };

        DFSearch dfs = new DFSearch(node,myBranching);

        int [] nSols = new int[1];

        dfs.onSolution(() -> {
            System.out.println(Arrays.toString(values));
            nSols[0] += 1;
        });


        dfs.start();

        assert(nSols[0] == 16);



    }



}

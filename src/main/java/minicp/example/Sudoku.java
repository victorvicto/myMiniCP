package minicp.example;

import minicp.constraints.AllDifferentBinary;
import minicp.constraints.DifferentVal;
import minicp.constraints.EqualVal;
import minicp.core.IntVar;
import minicp.core.Model;
import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.search.DFSearch;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class Sudoku {

    public static void main(String[] args) {

        int[][] puzzle =
                       {{4,2,3,5,6,7,8,0,1},
                        {5,6,1,0,8,4,2,3,7},
                        {0,8,7,2,3,1,4,5,6},
                        {7,4,8,6,5,0,3,1,2},
                        {3,1,5,7,4,2,6,8,0},
                        {6,0,2,8,1,3,7,4,5},
                        {8,5,0,4,2,6,1,7,3},
                        {1,7,6,3,0,8,5,2,4},
                        {2,3,4,1,7,5,0,6,8}};


        Random rand = new Random(0);

        Model cp = new Model();

        IntVar [][] x = new IntVar[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                x[i][j] = new IntVar(cp,9);
                if (rand.nextInt() < 95) {
                    cp.add(new EqualVal(x[i][j],puzzle[i][j]));
                }
            }
        }

        // allDifferent on a line
        for (IntVar [] line: x) {
            cp.add(new AllDifferentBinary(line));
        }


        // allDifferent on a column
        for (int j = 0; j < x.length; j++) {
            IntVar [] column = new IntVar[9];
            for (int i = 0; i < x.length; i++)
                column[i] = x[i][j];
            cp.add(new AllDifferentBinary(column));
        }

        // allDifferent on a 3x3 block


        IntVar [] xFlat = new IntVar[x.length * x.length];
        for (int i = 0; i < x.length; i++) {
            System.arraycopy(x[i],0,xFlat,i * x.length,x.length);
        }


        Branching myBranching = new Branching() {
            @Override
            public Alternative[] getAlternatives() {
                Optional<IntVar> unBoundVarOpt = Arrays.stream(xFlat).filter(var -> !var.isBound()).findFirst();
                if (!unBoundVarOpt.isPresent()) {
                    return SOLUTION;
                } else {
                    IntVar unBoundVar = unBoundVarOpt.get();
                    int v = unBoundVar.getMin();
                    return branch(
                            () -> { // left branch
                                return cp.add(new EqualVal(unBoundVar, v));
                            },
                            () -> { // right branch
                                return cp.add(new DifferentVal(unBoundVar, v));
                            });
                }
            }
        };


        DFSearch dfs = new DFSearch(cp,myBranching);

        // count the number of solution
        int [] nSols = new int[1];
        dfs.onSolution(() -> {
            nSols[0] += 1;
        });

        // start the search
        dfs.start();

        System.out.println("#Solutions:"+nSols[0]);


    }

}

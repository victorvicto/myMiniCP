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

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;
import org.junit.Assume;
import org.junit.Test;

import java.util.Arrays;

import static minicp.cp.Factory.*;
import static minicp.cp.Factory.notEqual;
import static minicp.cp.Heuristics.firstFail;
import static minicp.search.Selector.branch;
import static minicp.search.Selector.selectMin;
import static org.junit.Assert.*;


public class CircuitTest {


    int [] circuit1ok = new int []{1,2,3,4,5,0};
    int [] circuit2ok = new int []{1,2,3,4,5,0};

    int [] circuit1ko = new int []{1,2,3,4,5,2};
    int [] circuit2ko = new int []{1,2,0,4,5,3};

    public static boolean checkHamiltonian(int [] circuit) {
        int [] count = new int[circuit.length];
        for (int v: circuit) {
            count[v]++;
            if (count[v] > 1) return false;
        }
        boolean [] visited = new boolean[circuit.length];
        int c = circuit[0];
        for (int i = 0; i < circuit.length; i++) {
            visited[c] = true;
            c = circuit[c];
        }
        for (int i = 0; i < circuit.length; i++) {
            if (!visited[i]) return false;
        }
        return true;
    }

    public static IntVar[] instanciate(Solver cp, int [] circuit) {
        IntVar[] x = new IntVar[circuit.length];
        for (int i = 0; i < circuit.length; i++) {
            x[i] = makeIntVar(cp,circuit[i],circuit[i]);
        }
        return x;
    }

    @Test
    public void testCircuitOk() {
        try {
            try {
                Solver cp = new Solver();
                cp.post(new Circuit(instanciate(cp,circuit1ok)));
                cp.post(new Circuit(instanciate(cp,circuit2ok)));
            } catch (InconsistencyException e) {
                fail("should not fail");
            }
        } catch (NotImplementedException e) {
            Assume.assumeNoException(e);
        }
    }


    @Test
    public void testCircuitKo() {
        try {
            try {
                Solver cp = new Solver();
                cp.post(new Circuit(instanciate(cp,circuit1ko)));
                fail("should fail");
            } catch (InconsistencyException e) {}
            try {
                Solver cp = new Solver();
                cp.post(new Circuit(instanciate(cp,circuit2ko)));
                fail("should fail");
            } catch (InconsistencyException e) {}
        } catch (NotImplementedException e) {
            Assume.assumeNoException(e);
        }
    }


    @Test
    public void testAllSolutions() {
        try {
            try {
                Solver cp = new Solver();
                IntVar [] x = makeIntVarArray(cp,5,5);
                cp.post(new Circuit(x));
                SearchStatistics stats = makeDfs(cp,firstFail(x)).onSolution(() -> {
                            int [] sol = new int[x.length];
                            for (int i = 0; i < x.length; i++) {
                                sol[i] = x[i].getMin();
                            }
                            System.out.println(Arrays.toString(sol));
                            assertTrue("Solution is not an hamiltonian Circuit",checkHamiltonian(sol));
                        }
                ).start();
            } catch (InconsistencyException e) { fail("should not fail");}
        } catch (NotImplementedException e) {
            Assume.assumeNoException(e);
        }
    }

    @Test
    public void testCircuitDomainFilter() {
        try {
            try {
                Solver cp = new Solver();

                IntVar[] x = new IntVar[10];
                for (int i = 0; i < 10; i++)
                    x[i] = makeIntVar(cp,0,9);

                cp.post(new Circuit(x));

                // No self-loop
                for(int i = 0; i < x.length; i++)
                    assertFalse(x[i].contains(i));

                x[0].assign(1);
                cp.fixPoint();
                for(int i = 1; i < x.length; i++)
                    assertFalse(x[i].contains(1));

            } catch (InconsistencyException e) { fail("should not fail");}
        } catch (NotImplementedException e) {
            Assume.assumeNoException(e);
        }
    }

    @Test
    public void testImmediateCircuit() {
        try {
            try {
                Solver cp = new Solver();

                IntVar[] x = new IntVar[1];
                x[0] = makeIntVar(cp, 0, 0);

                cp.post(new Circuit(x));
            } catch (InconsistencyException e) { fail("should not fail");}
        } catch (NotImplementedException e) {
            Assume.assumeNoException(e);
        }
    }

    @Test
    public void testCircuitDomainInit() {
        try {
            try {
                Solver cp = new Solver();

                IntVar[] x = new IntVar[10];
                for (int i = 0; i < 10; i++)
                    x[i] = makeIntVar(cp,-100,100);

                cp.post(new Circuit(x));

                assertEquals(0, x[2].getMin());
                assertEquals(9, x[2].getMax());
            } catch (InconsistencyException e) { fail("should not fail");}
        } catch (NotImplementedException e) {
            Assume.assumeNoException(e);
        }
    }
}

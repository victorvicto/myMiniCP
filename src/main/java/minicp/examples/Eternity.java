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

package minicp.examples;

import minicp.engine.constraints.Element2D;
import minicp.engine.constraints.TableCT;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import minicp.util.InputReader;

import java.util.Arrays;

import static minicp.cp.Factory.*;
import static minicp.cp.Heuristics.and;
import static minicp.cp.Heuristics.firstFail;

/**
 * https://en.wikipedia.org/wiki/Eternity_II_puzzle
 * The Eternity II puzzle is an edge-matching puzzle which involves placing nxm square puzzle pieces
 * into a n by m grid, constrained by the requirement to match adjacent edges.
 */
public class Eternity {

    public static IntVar[] flatten(IntVar [][] x) {
        return Arrays.stream(x).flatMap(Arrays::stream).toArray(IntVar[]::new);
    }

    public static void main(String[] args) throws InconsistencyException {

        // Read the data

        InputReader reader = new InputReader("data/eternity7x7.txt");

        int n = reader.getInt();
        int m = reader.getInt();

        int [][] pieces = new int[n*m][4];
        int max_ = 0;

        for (int i = 0; i < n*m; i++) {
            for (int j = 0; j < 4; j++) {
                pieces[i][j] = reader.getInt();
                if (pieces[i][j] > max_)
                    max_ = pieces[i][j];
            }
        }
        final int max = max_;

        // ------------------------

        // Table with all pieces and for each their 4 possible rotations


        // TODO: create the table where each line correspond to one possible rotation of a piece
        // For instance if the line piece[6] = [2,3,5,1]
        // the four lines created in the table are
        // [6,2,3,5,1] // rotation of 0°
        // [6,3,5,1,2] // rotation of 90°
        // [6,5,1,2,3] // rotation of 180°
        // [6,1,2,3,5] // rotation of 270°

        int [][] table = new int[4*n*m][5];

        for (int i = 0; i < n*m; i++) {
            int index = i*4;
            table[index][0] = i;
            table[index+1][0] = i;
            table[index+2][0] = i;
            table[index+3][0] = i;
            for (int j = 0; j < 4; j++) {
                table[index][j+1] = pieces[i][j];
                table[index+1][(j+1)%4+1] = pieces[i][j];
                table[index+2][(j+2)%4+1] = pieces[i][j];
                table[index+3][(j+3)%4+1] = pieces[i][j];
            }
        }

        /*for(int i=0; i<n*m*4; i++){
            String p = "";
            for(int j=0; j<5; j++){
                p += Integer.toString(table[i][j]);
                p += " ";
            }
            System.out.println(p);
        }*/

        Solver cp = makeSolver();

        // Create the variables making sure that common edges share the same instance variable)

        //   |         |
        // - +---------+- -
        //   |    u    |
        //   | l  i  r |
        //   |    d    |
        // - +---------+- -
        //   |         |


        IntVar[][] id = new IntVar[n][m]; // id variables
        IntVar[][] u = new IntVar[n][m];  // up side variables
        IntVar[][] r = new IntVar[n][m];  // right side variables
        IntVar[][] d = new IntVar[n][m];  // down side variables
        IntVar[][] l = new IntVar[n][m];  // left side variable


        for (int i = 0; i < n; i++) {
            u[i] = makeIntVarArray(cp,m,j -> makeIntVar(cp,0,max));
            id[i] = makeIntVarArray(cp,m,n*m);
        }
        for (int k = 0; k < n; k++) {
            final int i = k;
            if (i < n-1) d[i] = u[i+1];
            else d[i] = makeIntVarArray(cp,m,j -> makeIntVar(cp,0,max));
        }
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                l[i][j] = makeIntVar(cp,0,max);
            }
        }
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                if (j < m-1) r[i][j] = l[i][j+1];
                else r[i][j] = makeIntVar(cp,0,max);
            }
        }

        // TODO: State the constraints of the problem

        // Constraint1: all the pieces placed are different t

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                for (int ii = i+1; ii < n; ii++) {
                    cp.post(notEqual(id[i][j],id[ii][j]));
                }
                for (int jj = j+1; jj < m; jj++) {
                    for (int ii = 0; ii < n; ii++) {
                        if(i!=ii || j!=jj)
                            cp.post(notEqual(id[i][j],id[ii][jj]));
                    }
                }
            }
        }

        // Constraint2: all the pieces placed are valid ones i.e. one of the given mxn pieces possibly rotated

        /*int [][] tableu = new int[n*m][4];
        int [][] tabler = new int[n*m][4];
        int [][] tabled = new int[n*m][4];
        int [][] tablel = new int[n*m][4];

        for(int i=0; i<n*m; i++) {
            for(int j=0; j<4; j++) {
                tableu[i][j] = table[i * 4 + j][1];
                tabler[i][j] = table[i * 4 + j][2];
                tabled[i][j] = table[i * 4 + j][3];
                tablel[i][j] = table[i * 4 + j][4];
            }
        }

        IntVar[][] y = new IntVar[n][m];

        for(int i=0; i<n; i++){
            for (int j = 0; j < m; j++) {
                IntVar y1 = makeIntVar(cp,4);
                y[i][j] = y1;
                cp.post(new Element2D(tableu,id[i][j],y1,u[i][j]));
                cp.post(new Element2D(tabler,id[i][j],y1,r[i][j]));
                cp.post(new Element2D(tabled,id[i][j],y1,d[i][j]));
                cp.post(new Element2D(tablel,id[i][j],y1,l[i][j]));
            }
        }*/

        for(int i=0; i<n; i++){
            for (int j = 0; j < m; j++) {
                IntVar[] yy = new IntVar[5];
                yy[0] = id[i][j];
                yy[1] = u[i][j];
                yy[2] = r[i][j];
                yy[3] = d[i][j];
                yy[4] = l[i][j];
                cp.post(new TableCT(yy,table));
            }
        }


        // Constraint3: place "0" one all external side of the border (gray color)

        for(int i=0; i<n; i++){
            l[i][0].assign(0);
            r[i][m-1].assign(0);
        }

        for(int j=0; j<m; j++){
            u[0][j].assign(0);
            d[n-1][j].assign(0);
        }
        //l[0][0].assign(0);
        //u[0][0].assign(0);

        // The search using the and combinator

        SearchStatistics stats = makeDfs(cp,
                and(firstFail(flatten(id)),
                        firstFail(flatten(u)),
                        firstFail(flatten(r)),
                        firstFail(flatten(d)),
                        firstFail(flatten(l))
                        /* TODO: continue, are you branching on all the variables ? */
                )
        ).onSolution(() -> {
            // Pretty Print
            for (int i = 0; i < n; i++) {
                String line = "   ";
                for (int j = 0; j < m; j++) {
                    line += u[i][j].getMin() + "   ";
                }
                System.out.println(line);
                line = " ";
                for (int j = 0; j < m; j++) {
                    line += l[i][j].getMin() + "   ";
                }
                line += r[i][m - 1].getMin();
                System.out.println(line);
            }
            String line = "   ";
            for (int j = 0; j < m; j++) {
                line += d[n - 1][j].getMin() + "   ";
            }
            System.out.println(line);

        }).start(statistics -> statistics.nSolutions == 1);

        System.out.format("#Solutions: %s\n", stats.nSolutions);
        System.out.format("Statistics: %s\n", stats);

    }


}
package minicp.cp;

import minicp.cp.constraints.DifferentVar;
import minicp.cp.core.Constraint;
import minicp.cp.core.Solver;
import minicp.cp.core.IntVar;
/**
 * Created by ldm on 1/10/17.
 */
public class Factory {
    // Factory
    static public IntVar[] makeIntVarArray(Solver cp,int n,int sz) {
        IntVar[] rv = new IntVar[n];
        for(int i=0;i<n;i++)
            rv[i] = new IntVar(cp,sz);
        return rv;
    }
    static public Constraint makeDifferentVar(IntVar x,IntVar y,int c) { return new DifferentVar(x,y,c);}
    static public Constraint makeDifferentVar(IntVar x,IntVar y)       { return new DifferentVar(x,y,0);}
}

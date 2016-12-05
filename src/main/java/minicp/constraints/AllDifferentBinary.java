package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;
import minicp.core.Store;
import minicp.search.Inconsistency;

public class AllDifferentBinary extends Constraint {

    private IntVar [] x;

    public AllDifferentBinary(IntVar [] x) {
        this.x = x;
    }

    @Override
    public void setUp() throws Inconsistency {
        Store cp = x[0].getStore();
        for (int i = 0; i < x.length; i++) {
            for (int j = i+1; j < x.length; j++) {
                cp.add(new DifferentVar(x[i],x[j]),false);
            }
        }
    }

}

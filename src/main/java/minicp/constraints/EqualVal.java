package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;
import minicp.search.Inconsistency;

public class EqualVal extends Constraint {

    private IntVar x;
    private int v;

    public EqualVal(IntVar x, int v) {
        this.x = x;
        this.v = v;
    }

    @Override
    public void setUp() throws Inconsistency{
        x.assign(v);
    }


}

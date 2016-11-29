package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;

public class EqualVal extends Constraint {

    private IntVar x;
    private int v;

    public EqualVal(IntVar x, int v) {
        this.x = x;
        this.v = v;
    }

    @Override
    public boolean setUp() {
        boolean ok = x.assign(v);
        return ok;
    }


}

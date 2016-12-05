package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;
import minicp.search.Inconsistency;

public class DifferentVal extends Constraint {

    private IntVar x;
    private int y;

    public DifferentVal(IntVar x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setUp() throws Inconsistency {
        x.remove(y);
    }

}

package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;

public class DifferentVal extends Constraint {

    private IntVar x;
    private int y;

    public DifferentVal(IntVar x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean setUp() {
        return x.remove(y);
    }

}

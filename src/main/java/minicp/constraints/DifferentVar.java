package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;

public class DifferentVar extends Constraint {

    private IntVar x,y;

    public DifferentVar(IntVar x, IntVar y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean setUp() {
        x.propagateOnBind(this);
        y.propagateOnBind(this);
        if (x.isBound() || y.isBound()) {
            return propagate();
        }
        else return true;
    }

    @Override
    public boolean propagate() {
        System.out.println("propagate?");
        if (x.isBound()) {
            return y.remove(x.getMin());
        } else {
            return x.remove(y.getMin());
        }
    }
}

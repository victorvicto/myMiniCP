package minicp.constraints;

import minicp.core.Constraint;
import minicp.core.IntVar;
import minicp.search.Inconsistency;

public class DifferentVar extends Constraint {

    private IntVar x,y;

    public DifferentVar(IntVar x, IntVar y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setUp() throws Inconsistency {
        x.propagateOnBind(this);
        y.propagateOnBind(this);
        if (x.isBound() || y.isBound()) {
            propagate();
        }
    }

    @Override
    public void propagate() throws Inconsistency {
        if (x.isBound()) {
            y.remove(x.getMin());
        } else {
            x.remove(y.getMin());
        }
    }
}

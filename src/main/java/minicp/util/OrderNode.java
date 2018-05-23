package minicp.util;

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.reversible.ReversibleInt;

public class OrderNode {
    private ReversibleInt nextIndex;
    private OrderNode[] nextList;
    private IntVar var;
    private int index;
    private Solver cp;

    public OrderNode (IntVar v, int i) {
        this.var = v;
        cp = v.getSolver();
        nextIndex = null;
        nextList = null;
        index = i;
    }

    public void instantiateNextList (OrderNode[] l) {
        nextList = l;
    }

    public void addNext(int newNode) throws InconsistencyException {
        if (newNode==-1)
            return;

        IntVar var2 = nextList[newNode].getVar();
        var2.removeBelow(var.getMin());
        var.removeAbove(var2.getMax());
        cp.fixPoint();

        if (nextIndex!=null)
            nextList[newNode].setNext(nextIndex.getValue());
        else
            nextIndex = new ReversibleInt(cp.getTrail(),-1);
        this.nextIndex.setValue(newNode);
    }

    public void setNext(int newNode) {
        nextIndex.setValue(newNode);
    }

    public IntVar getVar () {
        return var;
    }

    public int getIndex () {
        return index;
    }

    public int getNext () {
        if (nextIndex==null)
            return -1;
        return nextIndex.getValue();
    }
}

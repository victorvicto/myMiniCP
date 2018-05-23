package minicp.util;

import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.reversible.ReversibleInt;
import minicp.search.Alternative;

import java.util.ArrayList;
import java.util.List;

public class OrderQueue {
    private ReversibleInt first;
    private Solver cp;

    public OrderQueue (Solver cp) {
        first = null;
        this.cp = cp;
    }

    public void setFirst(int i) {
        if (first==null)
            first = new ReversibleInt(cp.getTrail(),-1);
        first.setValue(i);
    }

    public int getFirst() {
        if (first==null)
            return -1;
        return first.getValue();
    }

    /*public List<Alternative> listOptions (OrderNode n) {
        List<Alternative> a = new ArrayList<>();

        if (first==null) {
            first.setValue(n.getIndex());
            a.add(()->{});
            return a;
        }

        ReversibleInt next = first;


        return a;
    }*/
}

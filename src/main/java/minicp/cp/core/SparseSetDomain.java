package minicp.cp.core;

import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleSparseSet;
import minicp.util.InconsistencyException;

/**
 * Created by ldm on 1/12/17.
 */
public class SparseSetDomain implements IntDomain {
    private ReversibleSparseSet domain;
    public SparseSetDomain(ReversibleContext state,int n) { domain = new ReversibleSparseSet(state,n);}
    public int getMin()  { return domain.getMin();}
    public int getMax()  { return domain.getMax();}
    public int getSize() { return domain.getSize();}
    public boolean contains(int v) { return domain.contains(v);}
    public boolean isBound() { return domain.getSize() == 1;}
    public String toString() { return domain.toString();

    void remove(int v,Notifier x) throws InconsistencyException {

    }
    void assign(int v,Notifier x) throws InconsistencyException {

    }
    int removeBelow(int value,Notifier x) throws InconsistencyException {

    }
    int removeAbove(int value,Notifier x) throws InconsistencyException {

    }
}

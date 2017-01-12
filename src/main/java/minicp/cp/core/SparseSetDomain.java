package minicp.cp.core;

import minicp.reversible.State;
import minicp.reversible.ReversibleSparseSet;
import minicp.util.InconsistencyException;

/**
 * Created by ldm on 1/12/17.
 */
public class SparseSetDomain implements IntDomain {
    private ReversibleSparseSet domain;
    public SparseSetDomain(State state, int n) { domain = new ReversibleSparseSet(state,n);}
    public int getMin()  { return domain.getMin();}
    public int getMax()  { return domain.getMax();}
    public int getSize() { return domain.getSize();}
    public boolean contains(int v) { return domain.contains(v);}
    public boolean isBound() { return domain.getSize() == 1;}
    public String toString() { return domain.toString();}

    public void remove(int v,Notifier x) throws InconsistencyException {
        domain.remove(v);
        x.domainEvt(domain.getSize());
    }
    public void removeAllBut(int v,Notifier x) throws InconsistencyException {
        domain.removeAllBut(v);
        x.bindEvt();
    }
    public void removeBelow(int value,Notifier x) throws InconsistencyException {
        domain.removeBelow(value);
        x.updateMinEvt(domain.getSize());
    }
    public void removeAbove(int value,Notifier x) throws InconsistencyException {
        domain.removeAbove(value);
        x.updateMaxEvt(domain.getSize());
    }
}

package minicp.core;


import minicp.reversible.ReversibleSparseSet;
import minicp.reversible.ReversibleStack;

public class IntVar {

    Store store;
    ReversibleSparseSet domain;
    ReversibleStack<Constraint> onDomainChange;
    ReversibleStack<Constraint> onBind;

    public IntVar(Store store, int n) {
        this.store = store;
        domain = new ReversibleSparseSet(store,n);
        onDomainChange = new ReversibleStack<Constraint>(store);
        onBind = new ReversibleStack<Constraint>(store);
    }

    public void propagateOnDomainChange(Constraint c) {
        onDomainChange.push(c);
    }

    public void propagateOnBind(Constraint c) {
        onBind.push(c);
    }

    private void enQueueAll(ReversibleStack<Constraint> constraints) {
        for (int i = 0; i < constraints.size(); i++) {
            store.enqueue(constraints.get(i));
        }
    }

    public Store getStore() { return store; }

    public int getMin() { return domain.getMin(); };

    public int getMax() { return domain.getMax(); }

    public int getSize() { return domain.getMax(); }

    public boolean contains(int v) { return domain.contains(v); }

    public boolean remove(int v) {
        if (domain.contains(v)) {
            domain.remove(v);
            enQueueAll(onDomainChange);
            if (domain.getSize() == 1) {
                enQueueAll(onBind);
            }
        }
        return domain.isEmpty();
    }

    public boolean assign(int v) {
        if (domain.contains(v)) {
            if (domain.getSize() != 1) {
                domain.removeAllBut(v);
                enQueueAll(onDomainChange);
                enQueueAll(onBind);
            }
            return true;
        }
        else {
            domain.removeAll();
            return false;
        }
    }

    public boolean isBound() {
        return domain.getSize() == 1;
    }

    @Override
    public String toString() {
        return domain.toString();
    }
}

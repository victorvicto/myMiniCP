package minicp.cp.core;

import minicp.util.InconsistencyException;

/**
 * Created by ldm on 1/12/17.
 */
public interface IntDomain {
    int getMin();
    int getMax();
    int getSize();
    boolean contains(int v);
    boolean isBound();
    String toString();
    void remove(int v,Notifier x) throws InconsistencyException;
    void assign(int v,Notifier x) throws InconsistencyException;
    int removeBelow(int value,Notifier x) throws InconsistencyException;
    int removeAbove(int value,Notifier x) throws InconsistencyException;
}

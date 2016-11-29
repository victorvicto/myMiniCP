package minicp.search;


import minicp.reversible.ReversibleBool;
import minicp.reversible.ReversibleContext;

public class DFSearchNode extends ReversibleContext {

    protected  ReversibleBool failed = new ReversibleBool(this,false);

    public boolean isFailed() { return failed.getValue(); }
}

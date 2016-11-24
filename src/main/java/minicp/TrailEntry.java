package minicp;


public class TrailEntry {

    int v;
    ReversibleInt rInt;

    public TrailEntry(ReversibleInt rInt, int v) {
        this.rInt = rInt;
        this.v = v;
    }

    public void restore() {
        rInt.restore(v);
    }
}

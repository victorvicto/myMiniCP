package minicp.reversible;


public class ReversibleBool implements TrailEntry {

    private boolean v;
    private ReversibleContext context;
    private long lastMagic;

    public ReversibleBool(ReversibleContext context, boolean initial) {
        this.context = context;
        v = initial;
        lastMagic = context.magic;
    }

    private void trail() {
        long contextMagic = context.magic;
        if (lastMagic != contextMagic) {
            lastMagic = contextMagic;
            context.pushOnTrail(this);
        }
    }

    public void setValue(boolean v) {
        if (v != this.v) {
            trail();
            this.v = v;
        }
    }

    public boolean getValue() { return this.v; }

    public void restore() {
        this.v = !v;
    }
}

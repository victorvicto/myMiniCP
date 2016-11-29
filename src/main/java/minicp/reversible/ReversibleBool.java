package minicp.reversible;


public class ReversibleBool implements TrailEntry {

    final TrailEntry restoreTrue = new TrailEntry() {
        @Override
        public void restore() {
            v = true;
        }
    };

    final TrailEntry restoreFalse = new TrailEntry() {
        @Override
        public void restore() {
            v = false;
        }
    };

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
            if (v) context.pushOnTrail(restoreTrue);
            else context.pushOnTrail(restoreFalse);
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
        v = !v;
    }
}

package minicp.reversible;


public class ReversibleInt {

    class TrailEntryInt implements TrailEntry {

        private int v;

        public TrailEntryInt(int v) {
            this.v = v;
        }

        public void restore() {
            ReversibleInt.this.v = v;
        }
    }

    private ReversibleContext context;
    private int v;
    private Long lastMagic = -1L;

    public ReversibleInt(ReversibleContext context, int initial) {
        this.context = context;
        v = initial;
        lastMagic = context.magic;
    }

    private void trail() {
        long contextMagic = context.magic;
        if (lastMagic != contextMagic) {
            lastMagic = contextMagic;
            context.pushOnTrail(new TrailEntryInt(v));
        }
    }

    public void setValue(int v) {
        if (v != this.v) {
            trail();
            this.v = v;
        }
    }

    public void increment() { setValue(getValue()+1); }
    public void decrement() { setValue(getValue()-1); }

    public int getValue() { return this.v; }

}

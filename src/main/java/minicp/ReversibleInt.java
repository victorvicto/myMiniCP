package minicp;


public class ReversibleInt {

    private ReversibleContext context;
    private int v;
    private Long lastMagic = -1L;

    public ReversibleInt(ReversibleContext context, int initial) {
        this.context = context;
        v = initial;
    }

    private void trail() {
        long contextMagic = context.magic;
        if (lastMagic != contextMagic) {
            lastMagic = contextMagic;
            context.pushOnTrail(this,v);
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

    public void restore(int v) {
        this.v = v;
    }
}

package minicp.search;


public abstract class Branching {

    public static final Alternative[] SOLUTION = new Alternative[0];

    public Alternative[] branch(Alternative... alternatives) {
        return alternatives;
    }

    public abstract Alternative[] getAlternatives();
}

import minicp.ReversibleContext;
import minicp.ReversibleInt;
import minicp.ReversibleSparseSet;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class TestReversibleSparseSet {

    @Test
    public void testReversibleSparseSet() {
        ReversibleContext rc = new ReversibleContext();
        ReversibleSparseSet set = new ReversibleSparseSet(rc,10);

        System.out.println(set);

        rc.push();

        set.remove(1);
        set.remove(0);

        assertTrue(set.getMin() == 2);


    }
}

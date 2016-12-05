import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleSparseSet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestReversibleSparseSet {

    @Test
    public void testReversibleSparseSet() {

        ReversibleContext rc = new ReversibleContext();
        ReversibleSparseSet set = new ReversibleSparseSet(rc,10);

        rc.push();

        set.remove(1);
        set.remove(0);

        assertTrue(set.getMin() == 2);

        set.remove(8);
        set.remove(9);

        assertTrue(set.getMax() == 7);

        rc.pop();
        rc.push();

        assertEquals(10, set.getSize());

        for (int i = 0; i < 10; i++) {
            assertTrue(set.contains(i));
        }
        assertFalse(set.contains(10));

        assertTrue(set.getMin() == 0);
        assertTrue(set.getMax() == 9);

        set.removeAllBut(2);

        for (int i = 0; i < 10; i++) {
            if (i != 2) assertFalse(set.contains(i));
        }
        assertTrue(set.contains(2));

        rc.pop();
        rc.push();

        assertEquals(10, set.getSize());


    }
}

import minicp.core.IntVar;
import minicp.core.Model;
import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleInt;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestIntVar {

    @Test
    public void testIntVar() {
        Model cp  = new Model();

        IntVar x = new IntVar(cp,10);
        IntVar y = new IntVar(cp,10);

        cp.push();

        assertFalse(x.isBound());
        assertTrue(x.remove(5));
        assertEquals(9,x.getSize());
        assertTrue(x.assign(7));
        assertEquals(1,x.getSize());
        assertTrue(x.isBound());
        assertFalse(x.assign(8));

        cp.pop();
        cp.push();

        assertFalse(x.isBound());
        assertEquals(10,x.getSize());

        for (int i = 0; i < 10; i++) {
            assertTrue(x.contains(i));
        }
        assertFalse(x.contains(-1));


    }


}

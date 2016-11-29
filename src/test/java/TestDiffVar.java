import minicp.constraints.DifferentVar;
import minicp.constraints.EqualVal;
import minicp.core.IntVar;
import minicp.core.Model;
import org.junit.Test;

import static org.junit.Assert.*;


public class TestDiffVar {

    @Test
    public void testIntVar() {
        Model cp  = new Model();

        IntVar x = new IntVar(cp,10);
        IntVar y = new IntVar(cp,10);

        boolean ok = true;

        ok = cp.add(new DifferentVar(x,y));

        assertTrue(ok);

        ok = cp.add(new EqualVal(x,6));

        assertTrue(ok);

        assertFalse(y.contains(6));

    }


}

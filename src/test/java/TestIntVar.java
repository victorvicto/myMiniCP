import minicp.core.IntVar;
import minicp.core.Model;
import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleInt;
import minicp.search.Inconsistency;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;


public class TestIntVar {


    @Test
    public void testIntVar() {
        Model cp  = new Model();

        IntVar x = new IntVar(cp,10);
        IntVar y = new IntVar(cp,10);

        cp.push();


        try {


        assertFalse(x.isBound());
        x.remove(5);
        assertEquals(9,x.getSize());
        x.assign(7);
        assertEquals(1,x.getSize());
        assertTrue(x.isBound());

        } catch(Inconsistency e) { fail("should not fail here");}

        try {
            x.assign(8);
            fail( "shoudl have failed" );
        } catch (Inconsistency expectedException) {}



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

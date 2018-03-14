package minicp.util;
import org.junit.Assume;
import org.junit.Assert;

public class NotImplementedExceptionAssume {
    public static boolean failOnNPE = System.getenv().getOrDefault("MINICP_TEST_FAIL_ON_NPE", "0").equals("1");

    public static void fail(NotImplementedException e) {
        if(failOnNPE)
            Assert.fail("NotImplementedException");
        else
            Assume.assumeNoException(e);
    }
}

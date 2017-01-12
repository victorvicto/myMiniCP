package minicp.search;

/**
 * Created by ldm on 1/9/17.
 */
@FunctionalInterface
public interface Choice {
    Alternative[] getAlternatives();
}

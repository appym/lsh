import java.util.Random;

/**
 * Created by Apratim Mishra on 5/26/15.
 */
public class MyRandom extends Random {
    public MyRandom() {}
    public MyRandom(int seed) { super(seed); }

    public int nextNonNegative() {
        return next(31);
    }
}

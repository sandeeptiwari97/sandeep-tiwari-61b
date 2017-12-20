import lab14lib.Generator;

/**
 * Created by sandeeptiwari on 4/28/17.
 */
public class SawToothGenerator implements lab14lib.Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    public double next() {
        state += 1;
        return (state % period) * 2.0 / period - 1;
    }
}

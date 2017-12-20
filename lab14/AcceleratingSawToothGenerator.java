import lab14lib.Generator;

/**
 * Created by sandeeptiwari on 4/28/17.
 */
public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private double factor;
    private double cycles;

    public AcceleratingSawToothGenerator(int period, double next) {
        this.state = 0;
        this.period = period;
        this.factor = next;
        this.cycles = period;
    }

    public double next() {
        state += 1;
        if (state == cycles) {
            period = (int) (factor * period);
            cycles += period;
            cycles = (int) cycles;
        }
        return (-2 * ((cycles - state) % period) / period - 1) + 2;
    }
}

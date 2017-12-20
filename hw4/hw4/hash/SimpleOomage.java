package hw4.hash;
import java.awt.Color;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;


public class SimpleOomage implements Oomage {
    protected int red;
    protected int green;
    protected int blue;

    private static final double WIDTH = 0.01;
<<<<<<< HEAD
    private static final boolean USE_PERFECT_HASH = true;
=======
    private static final boolean USE_PERFECT_HASH = false;
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b

    @Override
    public boolean equals(Object o) {
        // TODO: Write this method.
<<<<<<< HEAD
        if (this == o) {
            return true;
        }
        else if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SimpleOomage p = (SimpleOomage) o;
//        return false;
        return (this.red == p.red) && (this.blue == p.blue) && (this.green == p.green);
=======
        return false;
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b
    }

    /* Uncomment this method after you've written
       equals and failed the testHashCodeAndEqualsConsistency
<<<<<<< HEAD
       test. */
=======
       test.
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b
    @Override
    public int hashCode() {
        if (!USE_PERFECT_HASH) {
            return red + green + blue;
        } else {
            // TODO: Write a perfect hash function for Simple Oomages.
<<<<<<< HEAD
//            return (red * 256 * 256) + (green * 256) + (blue);
            return (red * 2267 + green) * 2269 + (blue);
        }
    }
=======
            return 0;
        }
    }*/
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b

    public SimpleOomage(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException();
        }
        if ((r % 5 != 0) || (g % 5 != 0) || (b % 5 != 0)) {
            throw new IllegalArgumentException("red/green/blue values must all be multiples of 5!");
        }
        red = r;
        green = g;
        blue = b;
    }

    @Override
    public void draw(double x, double y, double scalingFactor) {
        StdDraw.setPenColor(new Color(red, green, blue));
        StdDraw.filledSquare(x, y, WIDTH * scalingFactor);
    }

    public static SimpleOomage randomSimpleOomage() {
        int red = StdRandom.uniform(0, 51) * 5;
        int green = StdRandom.uniform(0, 51) * 5;
        int blue = StdRandom.uniform(0, 51) * 5;
        return new SimpleOomage(red, green, blue);
    }

    public static void main(String[] args) {
        System.out.println("Drawing 4 random simple Oomages.");
        randomSimpleOomage().draw(0.25, 0.25, 1);
        randomSimpleOomage().draw(0.75, 0.75, 1);
        randomSimpleOomage().draw(0.25, 0.75, 1);
        randomSimpleOomage().draw(0.75, 0.25, 1);
    }

    public String toString() {
        return "R: " + red + ", G: " + green + ", B: " + blue;
    }
} 

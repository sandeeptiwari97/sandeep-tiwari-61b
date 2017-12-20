import edu.princeton.cs.algs4.Picture;
import java.util.ArrayList;
//import org.apache.commons.lang3.ArrayUtils;
import java.awt.Color;

/**
 * Created by sandeeptiwari on 4/23/17.
 */
public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private int[][] start;
    private final int MAXIMUM_ENERGY = 195075;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }


    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of the current picture
    public int width() {
        return picture.width();
    }

    // height of the current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        Color upper = picture.get(x, (y - 1 + this.height()) % this.height());
        Color lower = picture.get(x, (y + 1 + this.height()) % this.height());
        Color left = picture.get((x - 1 + this.width()) % this.width(), y);
        Color right = picture.get((x + 1 + this.width()) % this.width(), y);
        return getGradient(upper, lower) + getGradient(left, right);
    }

    private double getGradient(Color one, Color two) {
        int delRed = one.getRed() - two.getRed();
        int delBlue = one.getBlue() - two.getBlue();
        int delGreen = one.getGreen() - two.getGreen();
        return delRed * delRed + delBlue * delBlue + delGreen * delGreen;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture transposed = new Picture(this.height(), this.width());
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                transposed.set(i, j, picture.get(j, i));
            }
        }
        SeamCarver s = new SeamCarver(transposed);
        return s.findVerticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        ArrayList<Integer> seam = new ArrayList<>();
        double[][] energies = SCUtility.toEnergyMatrix(this);
        for (int r = 1; r < this.height(); r++) {
            for (int c = 0; c < this.width(); c++) {
                energies[c][r] = findVert(c, r, energies);
            }
        }

        double[] first = new double[this.width()];
        for (int c = 0; c < this.width(); c++) {
            first[c] = energies[c][this.height() - 1];
        }

        int bottomRowIndex = getMinIndex(first);
        seam.add(bottomRowIndex);
        connectLeast(bottomRowIndex, this.height() - 1, energies, seam);
        int[] correctSeam = new int[seam.size()];
        for (int i = 0; i < seam.size(); i++) {
            correctSeam[i] = seam.get(i);
        }
        return correctSeam;
    }

    private void finder(int row1, int col1, int row2, int col2) {
        if (energy[row2][col2] > energy[row1][col1] + energy(row2, col2)) {
            energy[row2][col2] = energy[row1][col1] + energy(row2, col2);
            start[row2][col2] = row1;
        }
    }

    private void connectLeast(int x, int y, double[][] e, ArrayList<Integer> seam) {
        if (y == 0) {
            return;
        } else {
            int top = x - 1;
            if (top < 0) {
                top = 0;
            }
            int newX = top;
            double min = Double.MAX_VALUE;
            for (int i = top; i <= x + 1 && i < this.width(); i++) {
                if (e[i][y - 1] < min) {
                    min = e[i][y - 1];
                    newX = i;
                }
            }
            seam.add(0, newX);
            connectLeast(newX, y - 1, e, seam);
        }
    }

    private int getMinIndex(double[] e) {
        int minIndex = 0;
        double minEnergy = Double.MAX_VALUE;
        for (int x = 0; x < e.length; x++) {
            if (e[x] < minEnergy) {
                minIndex = x;
                minEnergy = e[x];
            }
        }
        return minIndex;
    }

    private double findVert(int x, int y, double[][] energies) {
        int top = x - 1;
        if (top < 0) {
            top = 0;
        }
        double min = Double.MAX_VALUE;
        for (int i = top; i <= x + 1 && i < this.width(); i++) {
            if (energies[i][y - 1] < min) {
                min = energies[i][y - 1];
            }
        }
        return energies[x][y] + min;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        Picture original = picture;
        Picture transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.picture = transpose;
        transpose = null;
        original = null;

        // call removeVerticalSeam
        removeVerticalSeam(seam);

        // Transpose back.
        original = picture;
        transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.picture = transpose;
        transpose = null;
        original = null;
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }

        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }

        Picture original = this.picture;
        Picture seamed = new Picture(original.width() - 1, original.height());

        for (int h = 0; h < seamed.height(); h++) {
            for (int w = 0; w < seam[h]; w++) {
                seamed.set(w, h, original.get(w, h));
            }
            for (int w = seam[h]; w < seamed.width(); w++) {
                seamed.set(w, h, original.get(w + 1, h));
            }
        }
        this.picture = seamed;
    }
}

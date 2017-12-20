public class OffByN implements CharacterComparator {
    private int difference;
    public OffByN(int N) {
        this.difference = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(x - y) == this.difference);
    }
}

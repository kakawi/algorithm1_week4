import java.util.Arrays;
import java.util.function.Consumer;

public class Board {

    private final int n;
    private int hamming;
    private int manhattan;
    private int[][] blocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = Arrays.copyOf(blocks, blocks.length);
        n = blocks.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int correctNumber = (i + 1) * n + (j + 1);
                final int currentNumber = blocks[i][j];
                if (currentNumber != correctNumber) {
                    hamming++;
                    manhattan += calculateManhattan(currentNumber, i + 1, j + 1); // FIXME
                }
            }
        }

    }

    private int calculateManhattan(final int currentNumber, final int row, final int column) {
        int rowForCurrentNumber = currentNumber / n;
        int columnForCurrentNumber = currentNumber % n;
        return Math.abs(row - rowForCurrentNumber) + Math.abs(column - columnForCurrentNumber);
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) {
            return false;
        }
        final Board that = (Board) y;
        if (this.manhattan != that.manhattan) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(blocks[i][j]);
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

//    public static void main(String[] args) // unit tests (not graded)
}

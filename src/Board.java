import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    private final int n;
    private final int[][] blocks;

    private int hamming;
    private int manhattan;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = deepCopy(blocks);
        n = blocks.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int correctNumber = i * n + (j + 1);
                final int currentNumber = blocks[i][j];
                if (currentNumber != 0 && currentNumber != correctNumber) {
                    hamming++;
                    manhattan += calculateManhattan(currentNumber, i, j + 1);
                }
            }
        }
    }

    private int calculateManhattan(final int currentNumber, final int row, final int column) {
        int rowForCurrentNumber = currentNumber / n;
        int columnForCurrentNumber = currentNumber % n;
        if (columnForCurrentNumber == 0) {
            columnForCurrentNumber = n;
            rowForCurrentNumber--;
        }
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
        final int[][] twinBlocks = deepCopy(blocks);
        if (twinBlocks[0][0] != 0 && twinBlocks[1][0] != 0) {
            int swap = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[1][0];
            twinBlocks[1][0] = swap;
        } else if (twinBlocks[0][0] != 0 && twinBlocks[0][1] != 0) {
            int swap = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[0][1];
            twinBlocks[0][1] = swap;
        } else {
            int swap = twinBlocks[1][1];
            twinBlocks[1][1] = twinBlocks[1][0];
            twinBlocks[1][0] = swap;
        }
        return new Board(twinBlocks);
    }

    private static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
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
        final Stack<Board> neighbors = new Stack<>();
        // find 0 block
        // try to add 4 neighbors
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    if (i > 0) {
                        final int[][] copied = deepCopy(blocks);
                        copied[i][j] = copied[i - 1][j];
                        copied[i - 1][j] = 0;
                        neighbors.push(new Board(copied));
                    }
                    if (i < n - 1) {
                        final int[][] copied = deepCopy(blocks);
                        copied[i][j] = copied[i + 1][j];
                        copied[i + 1][j] = 0;
                        neighbors.push(new Board(copied));
                    }
                    if (j > 0) {
                        final int[][] copied = deepCopy(blocks);
                        copied[i][j] = copied[i][j - 1];
                        copied[i][j - 1] = 0;
                        neighbors.push(new Board(copied));
                    }
                    if (j < n - 1) {
                        final int[][] copied = deepCopy(blocks);
                        copied[i][j] = copied[i][j + 1];
                        copied[i][j + 1] = 0;
                        neighbors.push(new Board(copied));
                    }
                    break;
                }
            }
        }
        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}

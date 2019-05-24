import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.Comparator;

public class Solver {
    private class BoardWrapper {
        private final Board board;
        private final int priority;

        public BoardWrapper(final Board board, final int moves) {
            this.board = board;
            this.priority = board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public int getPriority() {
            return priority;
        }
    }

    private int moves = 0;
    private Queue<Board> solution = new Queue<>();
    private final Board initial;
    private Boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
        isSolvable();
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        if (solvable == null) {
            final MinPQ<BoardWrapper> originMinPQ = new MinPQ<>(
                    Comparator.comparingInt(BoardWrapper::getPriority)
            );
            final MinPQ<BoardWrapper> twinMinPQ = new MinPQ<>(
                    Comparator.comparingInt(BoardWrapper::getPriority)
            );
            originMinPQ.insert(new BoardWrapper(initial, moves));
            twinMinPQ.insert(new BoardWrapper(initial.twin(), moves));
            Board originPredecessor = null;
            Board twinPredecessor = null;
            while (true) {
                final Board originMinBoard = originMinPQ.delMin().getBoard();
                final Board twinMinBoard = twinMinPQ.delMin().getBoard();
                solution.enqueue(originMinBoard);
                if (originMinBoard.isGoal()) {
                    solvable = true;
                    break;
                }
                if (twinMinBoard.isGoal()) {
                    solution = null;
                    moves = -1;
                    solvable = false;
                    break;
                }
                moves++;
                fillByNeighbors(originMinPQ, originMinBoard, originPredecessor);
                fillByNeighbors(twinMinPQ, twinMinBoard, twinPredecessor);
                originPredecessor = originMinBoard;
                twinPredecessor = twinMinBoard;
            }
        }
        return solvable;
    }

    private void fillByNeighbors(final MinPQ<BoardWrapper> minPQ, final Board minBoard, final Board predecessor) {
        final Iterable<Board> neighbors = minBoard.neighbors();
        for (final Board neighbor : neighbors) {
            if (neighbor.equals(predecessor)) {
                continue;
            }
            minPQ.insert(new BoardWrapper(neighbor, moves));
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

}

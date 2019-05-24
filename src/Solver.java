import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {
    private final Board initial;
    private final boolean solvable;

    private int moves = 0;
    private Stack<Board> solution = new Stack<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
        solvable = haveFoundSolution();
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    private boolean haveFoundSolution() {
        final MinPQ<BoardWrapper> originMinPQ = new MinPQ<>(
                Comparator.comparingInt(BoardWrapper::getPriority)
        );

        final MinPQ<BoardWrapper> twinMinPQ = new MinPQ<>(
                Comparator.comparingInt(BoardWrapper::getPriority)
        );
        originMinPQ.insert(new BoardWrapper(initial, null));
        twinMinPQ.insert(new BoardWrapper(initial.twin(), null));
        while (true) {
            BoardWrapper originMinBoardWrapper = originMinPQ.delMin();
            final Board originMinBoard = originMinBoardWrapper.getBoard();
            final BoardWrapper twinMinBoardWrapper = twinMinPQ.delMin();
            final Board twinMinBoard = twinMinBoardWrapper.getBoard();
            if (originMinBoard.isGoal()) {
                moves = originMinBoardWrapper.getMoves();
                while (originMinBoardWrapper.getPreviousBoardWrapper() != null) {
                    solution.push(originMinBoardWrapper.getBoard());
                    originMinBoardWrapper = originMinBoardWrapper.getPreviousBoardWrapper();
                }
                solution.push(originMinBoardWrapper.getBoard());
                return true;
            }
            if (twinMinBoard.isGoal()) {
                solution = null;
                moves = -1;
                return false;
            }
            fillByNeighbors(originMinPQ, originMinBoardWrapper);
            fillByNeighbors(twinMinPQ, twinMinBoardWrapper);
        }
    }

    private void fillByNeighbors(final MinPQ<BoardWrapper> minPQ, final BoardWrapper minBoardWrapper) {
        final Iterable<Board> neighbors = minBoardWrapper.getBoard().neighbors();
        for (final Board neighbor : neighbors) {
            final BoardWrapper previousBoardWrapper = minBoardWrapper.getPreviousBoardWrapper();
            if (previousBoardWrapper != null && neighbor.equals(previousBoardWrapper.getBoard())) {
                continue;
            }
            minPQ.insert(new BoardWrapper(neighbor, minBoardWrapper));
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

    private class BoardWrapper {
        private final Board board;
        private final int priority;
        private final BoardWrapper previousBoardWrapper;
        private final int moves;

        public BoardWrapper(final Board board, final BoardWrapper previousBoardWrapper) {
            if (previousBoardWrapper == null) {
                moves = 0;
            } else {
                moves = previousBoardWrapper.getMoves() + 1;
            }
            this.board = board;
            this.previousBoardWrapper = previousBoardWrapper;
            this.priority = board.manhattan() + moves;

        }

        public Board getBoard() {
            return board;
        }

        public int getPriority() {
            return priority;
        }

        public BoardWrapper getPreviousBoardWrapper() {
            return previousBoardWrapper;
        }

        public int getMoves() {
            return moves;
        }
    }
}

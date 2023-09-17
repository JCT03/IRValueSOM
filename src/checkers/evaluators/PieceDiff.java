package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class PieceDiff implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        return Math.abs(value.numPiecesOf(value.getCurrentPlayer())- value.numPiecesOf(value.getCurrentPlayer().opponent()));
    }
}

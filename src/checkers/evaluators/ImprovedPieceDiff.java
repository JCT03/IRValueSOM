package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class ImprovedPieceDiff implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        int result = value.numPiecesOf(value.getCurrentPlayer())- value.numPiecesOf(value.getCurrentPlayer().opponent());
        result = result + value.numKingsOf(value.getCurrentPlayer())-value.numKingsOf(value.getCurrentPlayer().opponent());
        return result;
    }
}
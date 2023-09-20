package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class ImprovedPieceDiff implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        int pieceWeight= 100;
        int kingWeight=200;
        int result = pieceWeight * (value.numPiecesOf(value.getCurrentPlayer())- value.numPiecesOf(value.getCurrentPlayer().opponent()));
        result += (kingWeight-pieceWeight) * (value.numKingsOf(value.getCurrentPlayer())-value.numKingsOf(value.getCurrentPlayer().opponent()));
        return result;
    }
}
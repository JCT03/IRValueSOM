package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.security.DrbgParameters;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {
//    private ToIntFunction<Checkerboard> eval;
//    private int maxDepth;
    private int numNodes;
    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return Optional.of(NegaMaxFunc(board, getDepthLimit()));
    }

    public NegaMax(ToIntFunction<Checkerboard> e){
        super(e);
    }

    public Duple<Integer, Move> NegaMaxFunc(Checkerboard board, int depthLimit){
        if(board.gameOver()){
            if(board.playerWins(board.getCurrentPlayer())){
                return new Duple<Integer, Move>(Integer.MAX_VALUE, null);
            } else if(board.playerWins(board.getCurrentPlayer().opponent())){
                return new Duple<Integer, Move>(-Integer.MAX_VALUE, null);
            }
        } else if(depthLimit <= 0) {
                return new Duple<Integer, Move>(getEvaluator().applyAsInt(board), null);
        }
        int best_score = -Integer.MAX_VALUE;
        Move best_move = null;
        for (Move currentMove: board.getCurrentPlayerMoves()) {
            numNodes +=1;
            Checkerboard newBoard = board.duplicate();
            newBoard.move(currentMove);
            int value = 0;
            Move move;
            Duple<Integer, Move> nega = NegaMaxFunc(newBoard, depthLimit - 1);
            value = -nega.getFirst();
            move = nega.getSecond();
            if(value > best_score){
                best_move = currentMove;
                best_score = value;
            }
        }
        return new Duple<Integer, Move>(best_score, best_move);
    }
}

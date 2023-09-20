package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class AlphaBetaTable extends CheckersSearcher {
    private int numNodes;

    private HashMap<Checkerboard,int[]> gameRecords;
    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer,  Move>> selectMove(Checkerboard board) {
        gameRecords = new HashMap<Checkerboard,int[]>();
        return Optional.of(AlphaBetaFunc(board, getDepthLimit(), -Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public AlphaBetaTable(ToIntFunction<Checkerboard> e){
        super(e);
    }

    public Duple<Integer, Move> AlphaBetaFunc(Checkerboard board, int depthLimit, int alpha, int beta){
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
            numNodes += 1;
            Checkerboard newBoard = board.duplicate();
            newBoard.move(currentMove);
            int value = 0;
            if (gameRecords.get(newBoard) != null && gameRecords.get(newBoard)[0] >= (getDepthLimit()-depthLimit)) {
                value =  gameRecords.get(newBoard)[1];
            } else {
                if (!newBoard.turnIsRepeating()) {
                    Duple<Integer, Move> nega = AlphaBetaFunc(newBoard, depthLimit - 1, -beta, -alpha);
                    value = -nega.getFirst();
                } else {
                    Duple<Integer, Move> nega = AlphaBetaFunc(newBoard, depthLimit - 1, alpha, beta);
                    value = nega.getFirst();
                }
                int[] depthScore = {(getDepthLimit()-depthLimit),value};
                gameRecords.put(newBoard, depthScore);
            }
            if (best_move == null) {
                best_move = currentMove;
                best_score = value;
                alpha = value;
            }
            if (value > best_score) {
                best_move = currentMove;
                best_score = value;
                alpha = value;
            }
            if (alpha >= beta) {
                best_move = currentMove;
                best_score = value;
                return new Duple<Integer, Move>(best_score, best_move);
            }
        }
    return new Duple<Integer, Move>(best_score, best_move);
    }
}

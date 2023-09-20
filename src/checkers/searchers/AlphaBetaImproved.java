package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import core.Duple;

import java.security.DrbgParameters;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class AlphaBetaImproved extends CheckersSearcher {
    private int numNodes;

    private int gameDepth;

    private HashMap<Checkerboard,ArrayList<Object>> gameRecords;
    private HashMap<Checkerboard,ArrayList<Object>> newGameRecords;
    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        gameDepth = board.getNumMovesMade();
        gameRecords.clear();
        gameRecords.putAll(newGameRecords);
        newGameRecords.clear();
        return Optional.of(AlphaBetaFunc(board, getDepthLimit(), -Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public AlphaBetaImproved(ToIntFunction<Checkerboard> e){
        super(e);
        gameRecords = new HashMap<Checkerboard,ArrayList<Object>>();
        newGameRecords = new HashMap<Checkerboard,ArrayList<Object>>();
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
            numNodes +=1;
            Checkerboard newBoard = board.duplicate();
            newBoard.move(currentMove);
            if(gameRecords.get(newBoard) != null)
            {
                ArrayList<Object> objects = gameRecords.get(newBoard);
                System.out.println("Board Found");
                newGameRecords.put(newBoard, gameRecords.get(newBoard));
                if(objects.get(2) == newBoard.getCurrentPlayer()) {
                    return new Duple<Integer, Move>((Integer) objects.get(0), currentMove);
                }
                else{
                    return new Duple<Integer, Move>(-(Integer) objects.get(0), currentMove);
                }
            }
            int value = 0;
            Move move;
            if (!newBoard.turnIsRepeating()) {
                Duple<Integer, Move> nega = AlphaBetaFunc(newBoard, depthLimit - 1, -beta, -alpha);
                value = -nega.getFirst();
            } else{
                Duple<Integer, Move> nega = AlphaBetaFunc(newBoard, depthLimit - 1, alpha, beta);
                value = nega.getFirst();
            }
            if(best_move == null){
                best_move = currentMove;
                best_score = value;
                alpha = value;
            }
            if(value > best_score){
                best_move = currentMove;
                best_score = value;
                alpha = value;
            }
            if(alpha >= beta){
                best_move = currentMove;
                best_score = value;
                Integer Conscore = (Integer) best_score;
                Integer depth = (Integer) gameDepth + getDepthLimit() - depthLimit;
                ArrayList<Object> info = new ArrayList<>();
                info.add(0, Conscore);
                info.add(1, depth);
                info.add(2, newBoard.getCurrentPlayer());
                newGameRecords.put(newBoard, info);
                return new Duple<Integer, Move>(best_score, best_move);
            }
            Integer Conscore = (Integer) value;
            Integer depth = (Integer) gameDepth + getDepthLimit() - depthLimit;
            ArrayList<Object> info = new ArrayList<>();
            info.add(0, Conscore);
            info.add(1, depth);
            info.add(2, newBoard.getCurrentPlayer());
            newGameRecords.put(newBoard, info);        }
        return new Duple<Integer, Move>(best_score, best_move);
    }
}

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

    private HashMap<Duple<Checkerboard, Integer>,Duple<Integer, PlayerColor>> gameRecords;
    private HashMap<Duple<Checkerboard, Integer>,Duple<Integer, PlayerColor>> newGameRecords;
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
        gameRecords = new HashMap<Duple<Checkerboard, Integer>,Duple<Integer, PlayerColor>>();
        newGameRecords = new HashMap<Duple<Checkerboard, Integer>,Duple<Integer, PlayerColor>>();
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
            for (Duple<Checkerboard, Integer> potBoard: gameRecords.keySet()) {
                if(newBoard.equals(potBoard.getFirst())){
                    System.out.println("Board Found");
                    newGameRecords.put(potBoard, gameRecords.get(potBoard));
                    Duple<Integer, PlayerColor> score = gameRecords.get(potBoard);
                    if(score.getSecond() == newBoard.getCurrentPlayer()) {
                        System.out.println(score.getFirst());
                        return new Duple<Integer, Move>(score.getFirst(), currentMove);
                    }
                    else{
                        return new Duple<Integer, Move>(-score.getFirst(), currentMove);
                    }
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
                newGameRecords.put(new Duple<Checkerboard, Integer>(newBoard, gameDepth + getDepthLimit() - depthLimit), new Duple<Integer, PlayerColor>(best_score, board.getCurrentPlayer()));
                return new Duple<Integer, Move>(best_score, best_move);
            }
            newGameRecords.put(new Duple<Checkerboard, Integer>(newBoard, gameDepth + getDepthLimit() - depthLimit), new Duple<Integer, PlayerColor>(value, board.getCurrentPlayer()));
        }
        return new Duple<Integer, Move>(best_score, best_move);
    }
}

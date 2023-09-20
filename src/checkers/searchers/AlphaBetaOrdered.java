package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;
import search.SearchNode;

import java.security.DrbgParameters;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.PriorityQueue;

public class AlphaBetaOrdered extends CheckersSearcher {
    //    private ToIntFunction<Checkerboard> eval;
//    private int maxDepth;
    private int numNodes;
    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return Optional.of(AlphaBetaFunc(board, getDepthLimit(), -Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public AlphaBetaOrdered(ToIntFunction<Checkerboard> e){
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
        Comparator<Move> moveComparator = new Comparator<Move>() { //https://www.callicoder.com/java-priority-queue/
        @Override
        public int compare(Move a, Move b) {
            Checkerboard boardA = board.duplicate();
            boardA.move(a);
            Checkerboard boardB = board.duplicate();
            boardB.move(b);
            return (getEvaluator().applyAsInt(boardB)-getEvaluator().applyAsInt(boardA));
        }
        };
        PriorityQueue<Move> sortedMoves = new PriorityQueue<>(moveComparator);
        for (Move m:board.getCurrentPlayerMoves()) {
            sortedMoves.add(m);
        }
        while (!sortedMoves.isEmpty()) {
            Move currentMove= sortedMoves.poll();
            numNodes +=1;
            Checkerboard newBoard = board.duplicate();
            newBoard.move(currentMove);
            int value = 0;
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
                return new Duple<Integer, Move>(best_score, best_move);
            }
        }
        return new Duple<Integer, Move>(best_score, best_move);
    }
}

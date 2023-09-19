package checkers.searchers;
import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class Random extends CheckersSearcher {
    private int numNodes = 0;

    public Random(ToIntFunction<Checkerboard> e) {
        super(e);
    }
    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        Optional<Duple<Integer, Move>> move = Optional.empty();
        numNodes+=1;
        int numMoves = board.getCurrentPlayerMoves().size();
        int random = (int) (Math.random() * (numMoves));
        List<Move> list = new ArrayList<Move>(board.getCurrentPlayerMoves());
        Move randMove = list.get(random);
        return Optional.of(new Duple<Integer,Move>(0, randMove));
    }
}

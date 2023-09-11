package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

public class ManhattanToTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer value) {
        Set<Pos> treasure = value.getAllTreasureFromMaze();
        int closest = 0;
        if(!treasure.isEmpty()) {
            for (Pos pos : treasure) {
                if(!value.getAllTreasureFound().contains(pos)) {
                    int dist = pos.getManhattanDist(value.getLocation());
                    if (closest == 0) {
                        closest = dist;
                    } else {
                        if (closest > dist) {
                            closest = dist;
                        }
                    }
                }
            }
        }
        return closest;
    }
}

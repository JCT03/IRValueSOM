package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

public class ManhattanLeftRightGoal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer value) {
        Set<Pos> treasure = value.getAllTreasureFromMaze();
        int closest = 0;
        int closestDist = 0;
        if(!treasure.isEmpty()) {
            for (Pos pos : treasure) {
                if(!value.getAllTreasureFound().contains(pos)) {
                    int dist = pos.getX();
                    if (closestDist == 0) {
                        closestDist = dist;
                    } else {
                        if (closest > dist) {
                            closestDist = dist;
                            closest = pos.getManhattanDist(value.getLocation());
                        }
                    }
                } else {
                    closest = value.getLocation().getManhattanDist(value.getGoal().getLocation());
                }
            }
        }else{
            closest = value.getLocation().getManhattanDist(value.getGoal().getLocation());
        }
        return closest;
    }
}

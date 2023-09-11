package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

public class SumToGoals implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer value) {
        int total = 0;
        Set<Pos> treasure = value.getAllTreasureFromMaze();
        if(!treasure.isEmpty()) {
            for (Pos pos : treasure) {
                total += pos.getManhattanDist(value.getLocation());
            }
        }
        total+= value.getLocation().getManhattanDist(value.getGoal().getLocation());;
        return total;
    }
}

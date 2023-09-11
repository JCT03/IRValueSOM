package maze.heuristics;

import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;

public class ManhattanToGoal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer value) {
        return value.getLocation().getManhattanDist(value.getGoal().getLocation());
    }
}

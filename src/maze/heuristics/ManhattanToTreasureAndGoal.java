package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

public class ManhattanToTreasureAndGoal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer value) {
        Set<Pos> treasure = value.getAllTreasureFromMaze();
        int closest = 0;
        if(!treasure.isEmpty()) {
            for (Pos pos : treasure) {
                int dist = pos.getManhattanDist(value.getLocation());
                if(closest == 0){
                    closest = dist;
                } else{
                    if(closest > dist){
                        closest = dist;
                    }
                }
            }
        } else{
            closest = value.getLocation().getManhattanDist(value.getGoal().getLocation());
        }
        return closest;
    }
}

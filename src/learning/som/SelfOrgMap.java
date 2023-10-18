package learning.som;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;

public class SelfOrgMap<V> {
    private V[][] map;
    private ToDoubleBiFunction<V, V> distance;
    private WeightedAverager<V> averager;

    public SelfOrgMap(int side, Supplier<V> makeDefault, ToDoubleBiFunction<V, V> distance, WeightedAverager<V> averager) {
        this.distance = distance;
        this.averager = averager;
        map = (V[][])new Object[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                map[i][j] = makeDefault.get();
            }
        }
    }

    // TODO: Return a SOMPoint corresponding to the map square which has the
    //  smallest distance compared to example.
    // NOTE: The unit tests assume that the map is traversed in row-major order,
    //  that is, the y-coordinate is updated in the outer loop, and the x-coordinate
    //  is updated in the inner loop.
    public SOMPoint bestFor(V example) {
		double closestDist = Integer.MAX_VALUE;
        SOMPoint closestPoint = null;
        for (int y = 0; y<map[0].length; y++) {
            for (int x = 0; x<map.length; x++) {
                System.out.println("x: " + x + "  y: " + y);
                System.out.println("map:\n" + map[x][y]);
                System.out.println("example\n" + example);
                System.out.println("distance " + distance.applyAsDouble(map[x][y], example));
                if ((distance.applyAsDouble(map[x][y], example) < closestDist)){
                    closestDist = distance.applyAsDouble(map[x][y], example);
                    closestPoint = new SOMPoint(x,y);
                }
            }
        }
        return closestPoint;
    }

    // TODO: Train this SOM with example.
    //  1. Find the best matching node.
    //  2. Update the best matching node with the average of itself and example,
    //     using a learning rate of 0.9.
    //  3. Update each neighbor of the best matching node that is in the map,
    //     using a learning rate of 0.4.
    public void train(V example) {
        SOMPoint bestPoint = bestFor(example);
        map[bestPoint.x()][bestPoint.y()] = averager.weightedAverage(example, map[bestPoint.x()][bestPoint.y()], .9);
        for(SOMPoint p:bestPoint.neighbors()) {
            if (p.x() < map.length && p.x() >= 0 && p.y() < map[0].length && p.y() >= 0) {
                map[p.x()][p.y()] = averager.weightedAverage(example, map[p.x()][p.y()], .4);
            }
        }
    }

    public V getNode(int x, int y) {
        return map[x][y];
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }

    public int numMapNodes() {
        return getMapHeight() * getMapWidth();
    }

    public boolean inMap(SOMPoint point) {
        return point.x() >= 0 && point.x() < getMapWidth() && point.y() >= 0 && point.y() < getMapHeight();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SelfOrgMap that) {
            if (this.getMapHeight() == that.getMapHeight() && this.getMapWidth() == that.getMapWidth()) {
                for (int x = 0; x < getMapWidth(); x++) {
                    for (int y = 0; y < getMapHeight(); y++) {
                        if (!map[x][y].equals(that.map[x][y])) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                result.append(String.format("(%d, %d):\n", x, y));
                result.append(getNode(x, y));
            }
        }
        return result.toString();
    }
}
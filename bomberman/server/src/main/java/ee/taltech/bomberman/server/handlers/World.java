package ee.taltech.bomberman.server.handlers;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static final World INSTANCE = new World();
    private List<Integer> destructibleWalls = new ArrayList<>();

    public void setDestructibleWalls(List<Integer> walls) {
        destructibleWalls = walls;
    }

    public List<Integer> getDestructibleWalls() {
        return destructibleWalls;
    }

    public void removeWalls() {
        destructibleWalls.clear();
    }
}

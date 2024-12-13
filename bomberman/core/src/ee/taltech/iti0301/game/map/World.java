package ee.taltech.iti0301.game.map;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.game.walls.DestructibleWall;
import ee.taltech.iti0301.game.walls.IndestructibleWall;
import ee.taltech.iti0301.network.events.CurrentState;
import ee.taltech.iti0301.network.events.DestructibleWalls;
import ee.taltech.iti0301.network.events.RequestMap;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static final World INSTANCE = new World();
    private GameState gameState = GameState.INSTANCE;
    private List<Integer> destructibleWalls = new ArrayList<>();

    /**
     * creates and sets the map to game state
     */
    public void setMap() {
        gameState.setDestructibleWalls(createDestructibleWallSet());
        DestructibleWalls destructibleWalls1 = new DestructibleWalls();
        destructibleWalls1.destructibleWalls = getDestructibleWalls();
        BombermanGdxGame.client.sendUDP(destructibleWalls1);
        gameState.setIndestructibleWalls(createInDestructibleWallSet());
    }

    public void setServerMap(List<Integer> destructibleWalls) {
        gameState.setDestructibleWalls(createAlternativeDestructibleWallSet(destructibleWalls));
        gameState.setIndestructibleWalls(createInDestructibleWallSet());
    }

    private List<DestructibleWall> createAlternativeDestructibleWallSet(List<Integer> walls) {
        int i = 0;
        List<DestructibleWall> resultSet = new ArrayList<>();
        TiledMap map = new TmxMapLoader().load("maps\\testMap.tmx");
        MapLayers mapLayers = map.getLayers();
        for (MapObject object : mapLayers.get("BreakableWall").getObjects()) {
            Rectangle rectangle = createRectangleWithMapObject(object);
            DestructibleWall wall = new DestructibleWall(rectangle, walls.get(i));

            resultSet.add(wall);
            i++;
        }
        return resultSet;
    }

    /**
     * gets the destructible wall positions from the .tmx file and creates new destructible wall objects
     * @return list of destructible walls
     */
    private List<DestructibleWall> createDestructibleWallSet(){
        List<DestructibleWall> resultSet = new ArrayList<>();
        TiledMap map = new TmxMapLoader().load("maps\\testMap.tmx");
        MapLayers mapLayers = map.getLayers();
        for (MapObject object : mapLayers.get("BreakableWall").getObjects()) {
            Rectangle rectangle = createRectangleWithMapObject(object);
            DestructibleWall wall = new DestructibleWall(rectangle);

            resultSet.add(wall);
        }
        return resultSet;
    }
    /**
     * gets the indestructible wall positions from the .tmx file and creates new indestructible wall objects
     * @return list of indestructible walls
     */
    private List<IndestructibleWall> createInDestructibleWallSet(){
        List<IndestructibleWall> resultSet = new ArrayList<>();

        TiledMap map = new TmxMapLoader().load("maps\\testMap.tmx");
        MapLayers mapLayers = map.getLayers();

        for (MapObject object : mapLayers.get("UnbreakableWall").getObjects()) {
            Rectangle rectangle = createRectangleWithMapObject(object);
            IndestructibleWall wall = new IndestructibleWall(rectangle);
            resultSet.add(wall);
        }
        return resultSet;
    }

    /**
     * creates new rectangle object using map object
     * @param object to get the position and size from
     * @return rectangle with stats gotten from map object
     */
    private Rectangle createRectangleWithMapObject(MapObject object) {
        float posX = (float) object.getProperties().get("x");
        float posY = (float) object.getProperties().get("y");

        float width = (float) object.getProperties().get("width");
        float height = (float) object.getProperties().get("height");
        return new Rectangle(posX, posY, width, height);
    }

    /**
     *
     * @return all the destructible walls
     */
    private List<Integer> getDestructibleWalls() {
        return destructibleWalls;
    }

    public void addDestructibleWalls(Integer num) {
        destructibleWalls.add(num);
    }

    public void requestNewMap() {
        destructibleWalls.clear();
        RequestMap requestMap = new RequestMap();
        BombermanGdxGame.client.sendUDP(requestMap);
        CurrentState currentState = new CurrentState();
        currentState.gameStatus = false;
        BombermanGdxGame.client.sendUDP(currentState);
    }

}

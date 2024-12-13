package ee.taltech.iti0301.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import ee.taltech.iti0301.network.events.AddBomb;
import ee.taltech.iti0301.network.events.AddEnemy;
import ee.taltech.iti0301.network.events.AddPlayer;
import ee.taltech.iti0301.network.events.CurrentState;
import ee.taltech.iti0301.network.events.DestructibleWalls;
import ee.taltech.iti0301.network.events.EnemyMoveUpdate;
import ee.taltech.iti0301.network.events.GameStart;
import ee.taltech.iti0301.network.events.JoinRequest;
import ee.taltech.iti0301.network.events.JoinResponse;
import ee.taltech.iti0301.network.events.LobbyResponse;
import ee.taltech.iti0301.network.events.LobbyUserUpdate;
import ee.taltech.iti0301.network.events.PlayerUpdate;
import ee.taltech.iti0301.network.events.RemoveCollidedPlayers;
import ee.taltech.iti0301.network.events.RemoveEnemies;
import ee.taltech.iti0301.network.events.RemovePlayer;
import ee.taltech.iti0301.network.events.RemovePowerUps;
import ee.taltech.iti0301.network.events.RequestMap;

import java.util.*;

public class Network {

    public static final int TCP = 8085;
    public static final int UDP = 8086;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(AddPlayer.class);
        kryo.register(JoinRequest.class);
        kryo.register(JoinResponse.class);
        kryo.register(EnemyMoveUpdate.class);
        kryo.register(PlayerUpdate.class);
        kryo.register(RemovePlayer.class);
        kryo.register(String.class);
        kryo.register(AddBomb.class);
        kryo.register(RemoveCollidedPlayers.class);
        kryo.register(Set.class);
        kryo.register(HashSet.class);
        kryo.register(AddEnemy.class);
        kryo.register(EnemyMoveUpdate.class);
        kryo.register(RemoveEnemies.class);
        kryo.register(LobbyResponse.class);
        kryo.register(LobbyUserUpdate.class);
        kryo.register(Boolean.class);
        kryo.register(Map.class);
        kryo.register(HashMap.class);
        kryo.register(GameStart.class);
        kryo.register(CurrentState.class);
        kryo.register(Boolean.class);
        kryo.register(ArrayList.class);
        kryo.register(DestructibleWalls.class);
        kryo.register(List.class);
        kryo.register(RemovePowerUps.class);
        kryo.register(RequestMap.class);
    }
}

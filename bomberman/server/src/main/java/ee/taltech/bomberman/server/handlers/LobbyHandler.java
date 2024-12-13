package ee.taltech.bomberman.server.handlers;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.bomberman.server.GameServer;
import ee.taltech.bomberman.server.character.User;
import ee.taltech.iti0301.network.events.GameStart;
import ee.taltech.iti0301.network.events.CurrentState;
import ee.taltech.iti0301.network.events.LobbyResponse;
import ee.taltech.iti0301.network.events.LobbyUserUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyHandler {
    public static final LobbyHandler INSTANCE = new LobbyHandler();
    private List<User> userList = new ArrayList<>();
    private Map<String, Boolean> responseMap = new HashMap<>();
    private boolean play = false;

    public void addUser(String text, Connection connection) {
        User user = new User(text, false, connection);
        userList.add(user);
        LobbyResponse lobbyResponse = new LobbyResponse();
        responseMap.put(user.getName(), user.getStatus());
        lobbyResponse.users = responseMap;
        GameServer.server.sendToAllUDP(lobbyResponse);
    }

    public User getUserByConnection(Connection connection) {
        for (User user : userList) {
            if (user.getConnection() == connection) {
                return user;
            }
        }
        return null;
    }

    public void removeUser(User user) {
        userList.remove(user);
        responseMap.remove(user.getName());
        LobbyUserUpdate lobbyUserUpdate = new LobbyUserUpdate();
        lobbyUserUpdate.userName = user.getName();
        GameServer.server.sendToAllUDP(lobbyUserUpdate);
    }

    public void startGame(int connectionId) {
        GameStart gameStart = new GameStart();
        GameServer.server.sendToAllExceptUDP(connectionId, gameStart);
    }

    public void setStatus(boolean status) {
        play = status;
        CurrentState currentState = new CurrentState();
        currentState.gameStatus = play;
        GameServer.server.sendToAllUDP(currentState);
    }

    public boolean getStatus() {
        return play;
    }

    public void deleteUsers() {
        responseMap.clear();
        userList.clear();
    }
}

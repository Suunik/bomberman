package ee.taltech.iti0301.network.handlers;

import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.character.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LobbyHandler {
    public static final LobbyHandler INSTANCE = new LobbyHandler();
    private List<User> users = new ArrayList<>();
    private boolean gameServerStatus;
    private int gameCount = 0;
    public void addUser(Map<String, Boolean> userMap) {
        if (users.size() < 1) {
            for (Map.Entry<String, Boolean> entry : userMap.entrySet()) {
                users.add(new User(entry.getKey(), entry.getValue()));
            }
        } else {
            List<String> currentUsers = new ArrayList<>();
            List<String> updatedUsers = new ArrayList<>(userMap.keySet());
            for (User user : users) {
                currentUsers.add(user.getName());
            }
            updatedUsers.removeAll(currentUsers);
            String newUser = updatedUsers.get(0);
            users.add(new User(newUser, userMap.get(newUser)));
        }
    }

    public void deleteUsers(String name) {
        users.removeIf(user -> Objects.equals(user.getName(), name));
    }

    public List<User> getUsers() {
        return users;
    }

    public void startGame() {
        BombermanGdxGame.getInstance().changeState();
    }

    public void setCurrentState(boolean status) {
        gameServerStatus = status;
    }

    public boolean getGameServerStatus() {
        return gameServerStatus;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void incrementGameCount() {
        gameCount += 1;
    }

}

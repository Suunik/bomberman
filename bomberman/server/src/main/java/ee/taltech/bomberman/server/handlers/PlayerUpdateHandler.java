package ee.taltech.bomberman.server.handlers;

import ee.taltech.bomberman.server.GameServer;
import ee.taltech.bomberman.server.character.Player;
import ee.taltech.iti0301.network.events.PlayerUpdate;

public class PlayerUpdateHandler implements Runnable {
    public static final PlayerUpdateHandler INSTANCE = new PlayerUpdateHandler();
    private boolean running;
    public synchronized void start() {
        running = true;
        final Thread thread = new Thread(this);
        thread.start();
    }

    public void tick() {
        for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
            final Player player = PlayerHandler.INSTANCE.getPlayers().get(i);
            final PlayerUpdate playerUpdate = new PlayerUpdate();
            playerUpdate.username = player.getUsername();
            playerUpdate.x = player.getX();
            playerUpdate.y = player.getY();
            GameServer.server.sendToAllUDP(playerUpdate);
        }
    }

    @Override
    public void run() {
        long time = System.nanoTime();
        double ticks = 60;
        double ns = 1000000000 / ticks;
        double delta = 0;
        while (this.running) {
            try {
                Thread.sleep((long) (60F / ticks));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long now = System.nanoTime();
            delta += (now - time) / ns;
            time = now;
            while (delta > 0) {
                tick();
                delta--;
            }
        }
    }
}

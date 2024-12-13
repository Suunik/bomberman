package ee.taltech.bomberman.server.handlers;

import ee.taltech.bomberman.server.GameServer;
import ee.taltech.bomberman.server.character.Enemy;
import ee.taltech.iti0301.network.events.EnemyMoveUpdate;

public class EnemyUpdateHandler implements Runnable {
    public static final EnemyUpdateHandler INSTANCE = new EnemyUpdateHandler();
    private boolean running;
    public synchronized void start() {
        running = true;
        final Thread thread = new Thread(this);
        thread.start();
    }

    public void tick() {
        for (int i = 0; i < EnemyHandler.INSTANCE.getEnemies().size(); i++) {
            final Enemy enemy = EnemyHandler.INSTANCE.getEnemies().get(i);
            final EnemyMoveUpdate enemyMoveUpdate = new EnemyMoveUpdate();
            enemyMoveUpdate.up = enemy.isW();
            enemyMoveUpdate.left = enemy.isA();
            enemyMoveUpdate.down = enemy.isS();
            enemyMoveUpdate.right = enemy.isD();
            GameServer.server.sendToAllExceptUDP(enemy.getConnectionID(), enemyMoveUpdate);
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

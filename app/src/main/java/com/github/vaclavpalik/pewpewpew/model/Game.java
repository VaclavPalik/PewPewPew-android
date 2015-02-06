package com.github.vaclavpalik.pewpewpew.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceView;

import com.github.vaclavpalik.pewpewpew.MainActivity;
import com.github.vaclavpalik.pewpewpew.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class Game {

    private Set<Enemy> enemies = new HashSet<>();
    private int level = 0;
    private Random random = new Random();

    private Game() {
        Thread spawnTask = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final int time = spawnEnemy();
                        redraw();
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        spawnTask.start();
    }

    public static Game getInstance() {
        return SingletonHolder.instance;
    }

    public void handleHit(int x, int y) {
        boolean redraw = false;
        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
            Enemy enemy = it.next();
            if (enemy.inRange(x, y) && enemy.hit(Player.getInstance().getDamage())) {
                it.remove();
                redraw = true;
            }
        }
        if (redraw) {
            redraw();
        }
    }

    private void redraw() {
        if (MainActivity.getInstance().getGameFragment() == MainActivity.getInstance().getActiveFragment()) {
            SurfaceView view = (SurfaceView) MainActivity.getInstance().getGameFragment().getView().findViewById(R.id.surfaceView);
            Canvas canvas = view.getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);
            for (Enemy enemy : enemies) {
                canvas.drawBitmap(enemy.getBitmap(), enemy.getX(), enemy.getY(), null);
            }
            view.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public int getLevel() {
        return level;
    }

    private static class SingletonHolder {
        private static Game instance = new Game();
    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }

    private int spawnEnemy() {
        switch (getLevel()) {
            case 0:
                Location location = getRandomLocation(EnemyTemplate.FIGHTER.getWidth(), EnemyTemplate.FIGHTER.getHeight());
                enemies.add(new Enemy(location.getX(), location.getY(), EnemyTemplate.FIGHTER));
                return 500;
            default:
                return 100;
        }
    }

    private Location getRandomLocation(int width, int height) {
        return new Location(random.nextInt(MainActivity.getInstance().getGameFragment().getWidth() - width), random.nextInt(MainActivity.getInstance().getGameFragment().getHeight() - height));
    }
}

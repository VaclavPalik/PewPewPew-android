package com.github.vaclavpalik.pewpewpew.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceView;

import com.android.internal.util.Predicate;
import com.github.vaclavpalik.pewpewpew.MainActivity;
import com.github.vaclavpalik.pewpewpew.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game {
    private static boolean isStarted=false;
    private Set<Enemy> enemies = Collections.synchronizedSet(new HashSet<Enemy>());
    private int level = 0;
    private Map<Integer, Predicate<Integer>> levelConditions = new HashMap<>();
    private Random random = new Random();
    private volatile long lastHit = 0L;
    private Lock enemyLock = new ReentrantLock();
    private volatile boolean spawnHalted=false;
    private Runnable spawnTask= new Runnable() {
        @Override
        public void run() {
            while (!spawnHalted) {
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
    };

    private Game() {
        new Thread(spawnTask).start();
        levelConditions.put(0, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return Player.getInstance().getScore() >= 30;
            }
        });
        levelConditions.put(1, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return Player.getInstance().getScore() >= 100;
            }
        });
        levelConditions.put(2, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return Player.getInstance().getScore() >= 200;
            }
        });
        levelConditions.put(3, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return Player.getInstance().getScore() >= 500;
            }
        });
        levelConditions.put(4, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return Player.getInstance().getScore() >= 1000;
            }
        });
        levelConditions.put(5, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return Player.getInstance().getScore() >= 2000;
            }
        });
        levelConditions.put(6, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return false;
            }
        });
    }

    public static Game getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Handles the enemy hitting.
     * checks which enemies are hit and deals damage to them
     * @param x the x coord
     * @param y the y coord
     */
    public synchronized void handleHit(int x, int y) {
        long time = System.currentTimeMillis();
        if (time - lastHit < 100) //cooldown
            return;
        lastHit = time;
        boolean redraw = false;
        enemyLock.lock();
        try {
            for (Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
                Enemy enemy = it.next();
                if (enemy.inRange(x, y) && enemy.hit(Player.getInstance().getDamage())) {
                    it.remove();
                    redraw = true;
                }
            }
        } finally {
            enemyLock.unlock();
        }
        if (redraw) {
            redraw();
        }
    }

    /**
     * Redraws the game area where the enemies are spawned
     */
    private void redraw() {
        MainActivity.getInstance().getFragmentLock().lock();
        try {
            if (MainActivity.getInstance().getGameFragment() == MainActivity.getInstance().getActiveFragment()) {
                SurfaceView view = (SurfaceView) MainActivity.getInstance().getGameFragment().getView().findViewById(R.id.surfaceView);
                Canvas canvas = view.getHolder().lockCanvas();
                enemyLock.lock();
                try {
                    canvas.drawColor(Color.WHITE);
                    for (Enemy enemy : enemies) {
                        canvas.drawBitmap(enemy.getBitmap(), enemy.getX(), enemy.getY(), null);
                    }
                } finally {
                    view.getHolder().unlockCanvasAndPost(canvas);
                    enemyLock.unlock();
                }
            }
        }
        finally {
            MainActivity.getInstance().getFragmentLock().unlock();
        }
    }

    /**
     *
     * @return the current game's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * check if the conditions for the next level are met, if so, increases the game's level
     */
    public void checkNextLevel() {
        if (levelConditions.get(level).apply(level))
            level++;
    }

    /**
     * halts the enemy spawn
     */
    public void haltSpawn() {
        spawnHalted=true;
    }

    /**
     * resumes the enemy spawn
     */
    public void resumeSpawn(){
        spawnHalted=false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spawnTask.run();
            }
        }).start();
    }



    private static class SingletonHolder {
        private static Game instance = new Game();
        static {
            isStarted=true;
        }
    }

    /**
     *
     * @return true if the game has already started
     */
    public static boolean isStarted(){
        return isStarted;
    }

    /**
     * Spawns an enemy approtipriate to the current game's level
     * @return the time in millis determining cooldown 'till next enemy spawn
     */
    private int spawnEnemy() {
        switch (getLevel()) {
            case 0:
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 1500;
            case 1:
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 600;
            case 2:
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.MOOK : EnemyTemplate.FIGHTER);
                return 600;
            case 3:
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.MOOK : EnemyTemplate.FIGHTER);
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 600;
            case 4:
                spawnNewEnemyRandomly(EnemyTemplate.MOOK);
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 600;
            case 5:
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.BOMBER : EnemyTemplate.MOOK);
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.MOOK : EnemyTemplate.FIGHTER);
                return 600;
            case 6:
                spawnNewEnemyRandomly(new IEnemyTemplate[]{EnemyTemplate.BOMBER, EnemyTemplate.ARMORED, EnemyTemplate.MOOK}[random.nextInt(3)]);
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 300;
            default:
                return 100;
        }
    }

    /**
     * Spawns an enemy at random place
     * @param template the template describing the type of enemy spawned
     */
    private void spawnNewEnemyRandomly(IEnemyTemplate template) {
        Location location = getRandomLocation(template.getWidth(), template.getHeight());
        enemyLock.lock();
        try {
            enemies.add(new Enemy(location.getX(), location.getY(), template));
        } finally {
            enemyLock.unlock();
        }
    }

    /**
     * finds a suitable random location for a rectangle in the game's area
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @return a random suitable location
     */
    private Location getRandomLocation(int width, int height) {
        return new Location(random.nextInt(MainActivity.getInstance().getGameFragment().getWidth() - width), random.nextInt(MainActivity.getInstance().getGameFragment().getHeight() - height));
    }
}

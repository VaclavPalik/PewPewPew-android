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

    public int getLevel() {
        return level;
    }

    public void checkNextLevel() {
        if (levelConditions.get(level).apply(level))
            level++;
    }

    public void haltSpawn() {
        spawnHalted=true;
    }
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

    public static boolean isStarted(){
        return isStarted;
    }

    private int spawnEnemy() {
        switch (getLevel()) {
            case 0:
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 500;
            case 1:
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 200;
            case 2:
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.MOOK : EnemyTemplate.FIGHTER);
                return 200;
            case 3:
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.MOOK : EnemyTemplate.FIGHTER);
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 200;
            case 4:
                spawnNewEnemyRandomly(EnemyTemplate.MOOK);
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 200;
            case 5:
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.BOMBER : EnemyTemplate.MOOK);
                spawnNewEnemyRandomly(random.nextInt(3) == 0 ? EnemyTemplate.MOOK : EnemyTemplate.FIGHTER);
                return 200;
            case 6:
                spawnNewEnemyRandomly(new IEnemyTemplate[]{EnemyTemplate.BOMBER, EnemyTemplate.ARMORED, EnemyTemplate.MOOK}[random.nextInt(3)]);
                spawnNewEnemyRandomly(EnemyTemplate.FIGHTER);
                return 100;
            default:
                return 100;
        }
    }

    private void spawnNewEnemyRandomly(IEnemyTemplate template) {
        Location location = getRandomLocation(template.getWidth(), template.getHeight());
        enemyLock.lock();
        try {
            enemies.add(new Enemy(location.getX(), location.getY(), template));
        } finally {
            enemyLock.unlock();
        }
    }

    private Location getRandomLocation(int width, int height) {
        return new Location(random.nextInt(MainActivity.getInstance().getGameFragment().getWidth() - width), random.nextInt(MainActivity.getInstance().getGameFragment().getHeight() - height));
    }
}

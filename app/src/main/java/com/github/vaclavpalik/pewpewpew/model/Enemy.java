package com.github.vaclavpalik.pewpewpew.model;

import android.graphics.Bitmap;

public class Enemy {
    private Bitmap bitmap;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private int hp;
    private final int armor;
    private final int value;

    public Enemy(int x, int y, IEnemyTemplate template) {

        this.x = x;
        this.y = y;
        bitmap = template.getBitmap();
        height = template.getHeight();
        width = template.getWidth();
        hp = template.getHP();
        armor = template.getArmor();
        value = template.getValue();
    }

    /**
     *
     * @return the x coord of the top-left corner
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y coord of the top-left corner
     */
    public float getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * checks if this enemy is hit by a player's attack
     * @param x the x coord of player's attack
     * @param y the y coord of player's attack
     * @return true if this enemy is hit
     */
    public boolean inRange(int x, int y) {
        if (x < getX())
            if (y < getY())
                return Math.pow(getX() - x, 2) + Math.pow(getY() - y, 2) <= Player.getInstance().getRange();
            else if (y > getY() + getHeight())
                return Math.pow(getX() - x, 2) + Math.pow(y - getY() - getHeight(), 2) <= Player.getInstance().getRange();
            else
                return getX() - x <= Player.getInstance().getRange();
        else if (x > getX() + getWidth())
            if (y < getY())
                return Math.pow(x -getX()-getWidth(), 2) + Math.pow(getY() - y, 2) <= Player.getInstance().getRange();
            else if (y > getY() + getHeight())
                return Math.pow(x -getX()-getWidth(), 2) + Math.pow(y - getY() - getHeight(), 2) <= Player.getInstance().getRange();
            else
                return x -getX()-getWidth() <= Player.getInstance().getRange();
        else if (y < getY())
            return getY() - y <= Player.getInstance().getRange();
        else if (y > getY() + getHeight())
            return y - getY() - getHeight() <= Player.getInstance().getRange();
        else
            return true;

    }

    /**
     * Deals given damage to this enemy
     * @param damage
     * @return if this enemy was destroyed by this hit
     */
    public boolean hit(int damage) {
        hp-=Math.max(1, damage-armor);
        if(hp<=0) {
            onDestroy();
            return true;
        }
        return false;
    }

    private void onDestroy() {
        Player.getInstance().setMoney(Player.getInstance().getMoney()+ value +Player.getInstance().getIncome());
        Player.getInstance().addScore(value);
    }

    /**
     *
     * @return width of this enemy's rectangle
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return height of this enemy's rectangle
     */
    public int getHeight() {
        return height;
    }
}

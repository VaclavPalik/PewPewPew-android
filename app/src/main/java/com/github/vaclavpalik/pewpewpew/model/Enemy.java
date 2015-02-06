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
    private final int money;

    public Enemy(int x, int y, IEnemyTemplate template) {

        this.x = x;
        this.y = y;
        bitmap = template.getBitmap();
        height = template.getHeight();
        width = template.getWidth();
        hp = template.getHP();
        armor = template.getArmor();
        money = template.getMoney();
    }

    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

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

    public boolean hit(int damage) {
        hp-=Math.max(1, damage-armor);
        if(hp<=0) {
            onDestroy();
            return true;
        }
        return false;
    }

    private void onDestroy() {
        Player.getInstance().setMoney(Player.getInstance().getMoney()+money+Player.getInstance().getIncome());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

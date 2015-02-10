package com.github.vaclavpalik.pewpewpew.model;


import android.graphics.Bitmap;

public interface IEnemyTemplate {
    /**
     *
     * @return the max HP of this type of enemy
     */
    public int getHP();

    /**
     *
     * @return the value subtracted from each hit's damage
     */
    public int getArmor();

    /**
     *
     * @return the money given to the player after kill
     */
    public int getValue();

    /**
     *
     * @return the width of the rectangle of this enemy
     */
    public int getWidth();

    /**
     *
     * @return the height of the rectangle of this enemy
     */
    public int getHeight();

    /**
     *
     * @return the bitmap representing the enemy's graphics
     */
    public Bitmap getBitmap();
}

package com.github.vaclavpalik.pewpewpew.model;


import android.graphics.Bitmap;

public interface IEnemyTemplate {
    public int getHP();
    public int getArmor();
    public int getMoney();
    public int getWidth();
    public int getHeight();
    public Bitmap getBitmap();
}

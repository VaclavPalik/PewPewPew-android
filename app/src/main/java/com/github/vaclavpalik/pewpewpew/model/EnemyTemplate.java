package com.github.vaclavpalik.pewpewpew.model;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.github.vaclavpalik.pewpewpew.MainActivity;
import com.github.vaclavpalik.pewpewpew.R;

public enum EnemyTemplate implements IEnemyTemplate {
    FIGHTER(1,0,1,R.drawable.fighter);

    private final int hp;
    private final int armor;
    private final int money;
    private final Bitmap bitmap;

    private EnemyTemplate(int hp,int armor,int money,int image){

        this.armor = armor;
        this.hp = hp;
        this.money = money;
        bitmap = BitmapFactory.decodeResource(MainActivity.getInstance().getResources(),image);
        if(bitmap==null){
            throw  new NullPointerException();
        }
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}

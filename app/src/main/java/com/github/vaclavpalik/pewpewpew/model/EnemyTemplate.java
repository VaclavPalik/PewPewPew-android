package com.github.vaclavpalik.pewpewpew.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.vaclavpalik.pewpewpew.MainActivity;
import com.github.vaclavpalik.pewpewpew.R;

public enum EnemyTemplate implements IEnemyTemplate {
    FIGHTER(1,0,1,R.drawable.fighter),
    MOOK(2,0, 2, R.drawable.mook),
    BOMBER(5,1,5, R.drawable.bomber),
    ARMORED(5, 5, 15, R.drawable.armored);

    private final int hp;
    private final int armor;
    private final int value;
    private final Bitmap bitmap;

    private EnemyTemplate(int hp,int armor,int value,int image){

        this.armor = armor;
        this.hp = hp;
        this.value = value;
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
    public int getValue() {
        return value;
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

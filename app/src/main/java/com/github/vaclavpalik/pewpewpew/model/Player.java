package com.github.vaclavpalik.pewpewpew.model;

import android.widget.TextView;

import com.github.vaclavpalik.pewpewpew.MainActivity;
import com.github.vaclavpalik.pewpewpew.R;
import com.github.vaclavpalik.pewpewpew.model.Upgrades.Upgrade;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int money = 0;
    private int score = 0;
    private List<Upgrade> upgrades = new ArrayList<>();
    private Upgrade damage= new Upgrade("damage", "Damage", "+1 to damage", 100, 2, 10);
    private Upgrade income= new Upgrade("income", "Income", "+1 to money from all enemies", 100, 2, 10);
    private Upgrade range = new Upgrade("range", "Range" , "+1 to splash area", 100, 2, 10);

    private Player(){
        registerUpgrade(damage);
        registerUpgrade(income);
        registerUpgrade(range);
        setMoney(500000); //debug
    }

    public static Player getInstance() {
        return SingletonHolder.instance;
    }

    public int getDamage(){
        return damage.getLevel();
    }

    public int getMoney() {
        return money;
    }

    public int getIncome() {return income.getLevel()-1;}

    public void setMoney(int money) {
        this.money = money;
        ((TextView) MainActivity.getInstance().getMenuFragment().getView().findViewById(R.id.moneyCounter)).setText("Money: " + money);
        //MainActivity.getInstance().getUpgradeFragment().notifyChanged();
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public int getRange() {
        return range.getLevel()-1;
    }

    public int getScore() {
        return score;
    }

    private static class SingletonHolder {
        private static Player instance = new Player();
    }
    private void registerUpgrade(Upgrade upgrade){
        Upgrades.addItem(upgrade);
        upgrades.add(upgrade);
    }
    
    public void addScore(int score){
        this.score+=score;
        Game.getInstance().checkNextLevel();
    }
}

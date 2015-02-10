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
        setMoney(0); //debug
    }

    public static Player getInstance() {
        return SingletonHolder.instance;
    }

    /**
     *
     * @return the player's hit damage
     */
    public int getDamage(){
        return damage.getLevel();
    }

    /**
     *
     * @return the player's current money amount
     */
    public int getMoney() {
        return money;
    }

    /**
     *
     * @return the player's bonus to the money received for each kill
     */
    public int getIncome() {return income.getLevel()-1;}

    /**
     * Sets the player's money
     * @param money the new amount of money
     */
    public void setMoney(int money) {
        this.money = money;
        TextView textView = (TextView) MainActivity.getInstance().getMenuFragment().getView().findViewById(R.id.moneyCounter);
        textView.clearComposingText();
        textView.setText("Money: " + money);
        //MainActivity.getInstance().getUpgradeFragment().notifyChanged();
    }

    /**
     *
     * @return all player's upgrades
     */
    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    /**
     *
     * @return the player's splash area of hits
     */
    public int getRange() {
        return range.getLevel()-1;
    }

    /**
     *
     * @return the player's score indicating the total value of enemies destroyed
     */
    public int getScore() {
        return score;
    }

    private static class SingletonHolder {
        private static Player instance = new Player();
    }

    /**
     * Makes the upgrade buyable
     * @param upgrade
     */
    private void registerUpgrade(Upgrade upgrade){
        Upgrades.addItem(upgrade);
        upgrades.add(upgrade);
    }

    /**
     * Adds a specified amount to the player's score
     * @param score
     */
    public void addScore(int score){
        this.score+=score;
        Game.getInstance().checkNextLevel();
    }
}

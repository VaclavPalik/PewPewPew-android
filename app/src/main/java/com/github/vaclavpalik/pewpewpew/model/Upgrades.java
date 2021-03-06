package com.github.vaclavpalik.pewpewpew.model;

import com.github.vaclavpalik.pewpewpew.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Upgrades {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Upgrade> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Upgrade> ITEM_MAP = new HashMap<>();

    protected static void addItem(Upgrade item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Upgrade {
        private final int baseCost;
        private final int costMultiplier;
        private final int maxLevel;
        public String id;
        public String name;
        private String desc;
        private int level = 1;

        public Upgrade(String id, String name, String desc, int baseCost, int costMultiplier, int maxLevel) {
            this.baseCost = baseCost;
            this.costMultiplier = costMultiplier;
            this.id = id;
            this.name = name;
            this.desc = desc;
            this.maxLevel = maxLevel;
        }

        /**
         *
         * @return if the upgrade is on its max level
         */
        public boolean isMaxed() {
            return level >= maxLevel;
        }

        /**
         *
         * @return the level of this upgrade
         */
        public int getLevel() {
            return level;
        }

        /**
         *
         * @return the current cost of one level of this upgrade
         */
        public int getCost() {
            return (int) (baseCost * Math.pow(level, costMultiplier));
        }

        @Override
        public String toString() {
            return name + "\n" + desc + "\n Level: " + (isMaxed()?"MAXED":(level + " Cost: "+getCost()));
        }

        /**
         * Tries to buy a level of this upgrade. If this operation is possible, performs it.
         * @return true if a level of this upgrade was bought
         */
        public boolean tryBuy() {
            if (level >= maxLevel)
                return false;
            if (getCost() > Player.getInstance().getMoney())
                return false;
            Player.getInstance().setMoney(Player.getInstance().getMoney() - getCost());
            level++;
            MainActivity.getInstance().getUpgradeFragment().notifyChanged();
            return true;
        }
    }
}

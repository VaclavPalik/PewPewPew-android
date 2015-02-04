package com.github.vaclavpalik.pewpewpew.model;

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

    private static void addItem(Upgrade item) {
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
        private int level=0;

        public Upgrade(String id, String name, String desc, int baseCost, int costMultiplier, int maxLevel) {
            this.baseCost=baseCost;
            this.costMultiplier=costMultiplier;
            this.id = id;
            this.name = name;
            this.desc=desc;
            this.maxLevel=maxLevel;
            Upgrades.addItem(this);
            Player.getInstance().getUpgrades().add(this);
        }

        public int getLevel(){
            return level;
        }

        public int getCost(){
            return (int) (baseCost*Math.pow(level,costMultiplier));
        }

        @Override
        public String toString() {
            return name + " " + desc;
        }
    }
}

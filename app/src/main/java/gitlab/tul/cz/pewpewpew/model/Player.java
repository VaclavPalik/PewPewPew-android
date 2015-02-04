package gitlab.tul.cz.pewpewpew.model;

import java.util.ArrayList;
import java.util.List;
import gitlab.tul.cz.pewpewpew.model.Upgrades.Upgrade;

public class Player {

    private int money = 0;
    private List<Upgrade> upgrades = new ArrayList<>();
    private Upgrade damage= new Upgrade("damage", "Damage", "+1 to damage", 100, 2, 10);

    public static Player getInstance() {
        return SingletonHolder.instance;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    private static class SingletonHolder {
        private static Player instance = new Player();
    }
}

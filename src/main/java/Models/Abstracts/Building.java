package Models.Abstracts;

import Util.Models.Upgrade;

public abstract class Building {
    protected int buildingid;
    protected int btid;
    protected String name;

    protected int rid;
    protected String resname;

    protected int hp;
    protected int maxHP;

    protected int level;
    protected Upgrade upgrade;

    public Building(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                    int buildingLevel, Upgrade upgradeInfo){
        buildingid = buildingID;
        btid = buildingTypeID;
        name = buildingName;
        rid = resourceID;
        resname = resourceName;
        hp = maxHealth;
        maxHP = maxHealth;
        level = buildingLevel;
        upgrade = upgradeInfo;
    }

    public int level(){
        return level;
    }

    public int hp(){
        return hp;
    }
    public int maxHP(){
        return maxHP;
    }

    // returns true if destroyed after damage taken, false if still not broken
    public boolean takeDamage(int dmg){
        hp -= dmg;
        return hp <= 0;
    }

    public void rebuild(){
        hp = maxHP;
    }

    public void upgrade(){

    }
}

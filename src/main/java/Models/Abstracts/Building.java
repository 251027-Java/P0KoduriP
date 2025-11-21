package Models.Abstracts;

import Application.Game;
import Util.Models.Upgrade;

public abstract class Building {
    protected int buildingid;
    protected int buildid;
    protected int btid;
    protected String name;

    protected int rid;
    protected String resname;

    protected int hp;
    protected int maxHP;

    protected int level;
    protected Upgrade upgrade;

    public boolean upgrading;

    public Building(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                    int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int buildID){
        buildingid = buildingID;
        btid = buildingTypeID;
        name = buildingName;
        rid = resourceID;
        resname = resourceName;
        hp = maxHealth;
        maxHP = maxHealth;
        level = buildingLevel;
        upgrade = upgradeInfo;
        upgrading = currentlyUpgrading;
        buildid = buildID;
    }

    public int GetLevel(){
        return level;
    }

    public int GetHP(){
        return hp;
    }
    public int GetMaxHP(){
        return maxHP;
    }

    // returns true if destroyed after damage taken, false if still not broken
    public boolean TakeDamage(int dmg){
        hp -= dmg;
        return hp <= 0;
    }

    public void Rebuild(){
        hp = maxHP;
    }

    public void Upgrade(){

    }
    public void SetUpgradingInfo(Upgrade newUpgradeInfo){
        upgrade = newUpgradeInfo;
    }
    public Upgrade GetUpgradingInfo(){
        return upgrade;
    }

    public void SetUpgrading(boolean currentlyUpgrading){
        upgrading = currentlyUpgrading;
    }
    public boolean GetUpgrading(){
        return upgrading;
    }
    public long GetBuildingUpgradeTimeRemainingSeconds(int profID){
        return Game.getInstance().GetDataService().GetBuildingUpgradeTimeRemainingSeconds(profID, buildid);
    }

    public int GetBuildID(){
        return buildid;
    }
    public int GetBuildingID(){
        return buildingid;
    }
    public int GetBuildingTypeID(){
        return btid;
    }
}

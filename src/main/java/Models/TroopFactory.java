package Models;

import java.util.ArrayList;
import java.util.List;

public class TroopFactory {
    private int troopid;
    private String name;
    private int cost;
    private int space;

    private int speed;
    private int range;
    private int hitspeed;
    private boolean ground;
    private boolean atkair;

    private int dmg;
    private int hp;

    private int raxUnlockLevel;

    private int level;

    private int upCost;
    private int upDays;
    private int upHours;
    private int upMinutes;
    private int upSeconds;
    private boolean maxLevel;

    public TroopFactory(String tName, int tID, int tCost, int tSpace, int tSpeed, int tRange, int tHitSpeed, boolean groundTroop,
                 boolean canAttackAir, int damage, int health, int barracksUnlockLevel, int currentLevel, int upgradeCost,
                 int upgradeDays, int upgradeHours, int upgradeMinutes, int upgradeSeconds, boolean isMaxLevel){
        troopid = tID;
        name = tName;
        cost = tCost;
        space = tSpace;

        speed = tSpeed;
        range = tRange;
        hitspeed = tHitSpeed;
        ground = groundTroop;
        atkair = canAttackAir;

        dmg = damage;
        hp = health;

        raxUnlockLevel = barracksUnlockLevel;

        level = currentLevel;

        upCost = upgradeCost;
        upDays = upgradeDays;
        upHours = upgradeHours;
        upMinutes = upgradeMinutes;
        upSeconds = upgradeSeconds;
        maxLevel = isMaxLevel;
    }

    public void UpgradeTroop(int newLevel, int newDmg, int newHP, int newUpCost, int newUpDays, int newUpHours,
                             int newUpMinutes, int newUpSeconds, boolean isNowMaxLevel){
        level = newLevel;
        dmg = newDmg;
        hp = newHP;
        upCost = newUpCost;
        upDays = newUpDays;
        upHours = newUpHours;
        upMinutes = newUpMinutes;
        upSeconds = newUpSeconds;
        maxLevel = isNowMaxLevel;
    }

    // returns whether this troop is available for the current highest barracks level
    public boolean troopIsAvailable(int highestBarracksLevel){
        return highestBarracksLevel >= raxUnlockLevel;
    }

    public Troop CreateTroop(){
        return new Troop(name, troopid, cost, space, speed, range, hitspeed, ground, atkair, dmg, hp);
    }
    public List<Troop> CreateTroops(int numTroops){
        List<Troop> troops = new ArrayList<>(numTroops);
        for (int i = 0; i < numTroops; i++) troops.add(CreateTroop());
        return troops;
    }
}

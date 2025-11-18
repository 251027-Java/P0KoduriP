package Models;

import Util.Models.Upgrade;

import java.util.ArrayList;
import java.util.List;

public class TroopFactory {
    private int troopid;
    private String name;
    private int cost;
    private int space;

    private int speed;
    private int range;
    private float hitspeed;
    private boolean ground;
    private boolean atkair;

    private int dmg;
    private int hp;

    private final int raxUnlockLevel;

    private int level;

    private Upgrade upgrade;
    private boolean upgrading;

    public TroopFactory(String tName, int tID, int tCost, int tSpace, int tSpeed, int tRange, float tHitSpeed, boolean groundTroop,
                 boolean canAttackAir, int barracksUnlockLevel, boolean currentlyUpgrading){
        troopid = tID;
        name = tName;
        cost = tCost;
        space = tSpace;

        speed = tSpeed;
        range = tRange;
        hitspeed = tHitSpeed;
        ground = groundTroop;
        atkair = canAttackAir;

        raxUnlockLevel = barracksUnlockLevel;

        upgrading = currentlyUpgrading;
    }

    public void SetTroopInfo(int newLevel, int newDmg, int newHP, Upgrade newUpgradeInfo){
        level = newLevel;
        dmg = newDmg;
        hp = newHP;
        upgrade = newUpgradeInfo;
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

    public String GetTroopName(){
        return name;
    }
}

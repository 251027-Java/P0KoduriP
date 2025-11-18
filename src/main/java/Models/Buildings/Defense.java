package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class Defense extends Building {
    private boolean atkair;
    private float hitspeed;
    private int range;
    private int dmg;

    public Defense(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                   int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, boolean canAttackAir, float bHitSpeed, int bRange, int damage){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        atkair = canAttackAir;
        hitspeed = bHitSpeed;
        range = bRange;
        dmg = damage;
    }

    public int Attack(){
        return dmg;
    }
    public float HitSpeed(){
        return hitspeed;
    }

    @Override
    public void Upgrade(){
        super.Upgrade();
    }
}

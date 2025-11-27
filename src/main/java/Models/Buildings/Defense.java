package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class Defense extends Building {
    private boolean atkair;
    private float hitspeed;
    private int range;
    private int dmg;
    private boolean splash;
    private float splashrad;

    public Defense(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                   int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int buildID, boolean canAttackAir,
                   float bHitSpeed, int bRange, boolean splashDamage, float splashRadius, int damage){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading, buildID);
        atkair = canAttackAir;
        hitspeed = bHitSpeed;
        range = bRange;
        splash = splashDamage;
        splashrad = splashRadius;
        dmg = damage;
    }

    public int Attack(){
        return upgrading ? 0 : dmg;
    }
    public float HitSpeed(){
        return upgrading ? 0 : hitspeed;
    }

    @Override
    public void FinishUpgrade(){
        super.FinishUpgrade();
    }
}

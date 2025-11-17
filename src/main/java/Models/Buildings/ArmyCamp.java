package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class ArmyCamp extends Building {
    int maxspace;
    public ArmyCamp(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                   int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int maxSpace){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        maxspace = maxSpace;
    }

    @Override
    public void upgrade(){
        super.upgrade();
    }

    public int GetSpace(){
        return maxspace;
    }
}

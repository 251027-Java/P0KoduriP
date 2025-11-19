package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class ArmyCamp extends Building {
    int maxspace;
    public ArmyCamp(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                   int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int buildID, int maxSpace){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading, buildID);
        maxspace = maxSpace;
    }

    @Override
    public void Upgrade() {
        super.Upgrade();
    }

    public int GetMaxSpace(){
        return maxspace;
    }
}

package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class Barracks extends Building {
    public Barracks(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                           int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int buildID){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading, buildID);
    }

    @Override
    public void FinishUpgrade(){
        super.FinishUpgrade();
    }
}

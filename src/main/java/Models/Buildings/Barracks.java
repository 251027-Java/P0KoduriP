package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class Barracks extends Building {
    public Barracks(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                           int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
    }

    @Override
    public void upgrade(){
        super.upgrade();
    }
}

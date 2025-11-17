package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class ResourceStorage extends Building {
    int maxCap;
    int resStoreID;

    public ResourceStorage(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                             int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int maxCapacity, int resourceStoredID){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        maxCap = maxCapacity;
        resStoreID = resourceStoredID;
    }

    @Override
    public void upgrade(){
        super.upgrade();
    }

    public int GetResourceGeneratedID(){
        return resStoreID;
    }

    public int GetMaxCapacity(){
        return maxCap;
    }
}

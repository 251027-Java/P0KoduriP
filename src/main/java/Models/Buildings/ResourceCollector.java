package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class ResourceCollector extends Building {
    int genRate;
    int resGenID;

    public ResourceCollector(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
               int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int generatedPerHour, int resourceGeneratedID){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        genRate = generatedPerHour;
        resGenID = resourceGeneratedID;
    }

    @Override
    public void upgrade(){
        super.upgrade();
    }

    public int GetResourceGeneratedID(){
        return resGenID;
    }

    public int GetResourceGenerationRate(){
        return genRate;
    }
    public int CollectResources(float hours) {
        return (int) (hours * genRate);
    }
}

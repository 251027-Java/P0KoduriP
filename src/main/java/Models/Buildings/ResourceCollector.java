package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class ResourceCollector extends Building {
    int genRate;

    public ResourceCollector(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
               int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int generatedPerHour){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        genRate = generatedPerHour;
    }

    @Override
    public void upgrade(){
        super.upgrade();
    }

    public int GetResourceGenerationRate(){
        return genRate;
    }
    public int CollectResources(float hours) {
        return (int) (hours * genRate);
    }
}

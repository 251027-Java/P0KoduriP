package Models.Buildings;

import Models.Abstracts.Building;
import Models.Singletons.ResourceManager;
import Util.Models.Upgrade;

public class ResourceCollector extends Building {
    int genRate;
    int resGenID;
    int amount;

    public ResourceCollector(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
               int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int generatedPerHour, int resourceGeneratedID, int resourceAmount){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        genRate = generatedPerHour;
        resGenID = resourceGeneratedID;
        amount = resourceAmount;
    }

    @Override
    public void Upgrade(){
        super.Upgrade();
    }

    public int GetResourceGeneratedID(){
        return resGenID;
    }

    public int GetResourceGenerationRate(){
        return genRate;
    }
    public int CollectResources(float hours) {
        int newAmount = amount + (int) (hours * genRate);
        int maxCollectorCapacity = ResourceManager.MaxCollectionHours * genRate;

        if (newAmount > maxCollectorCapacity) newAmount = maxCollectorCapacity;
        amount = 0;

        return newAmount;
    }
    public void RestoreResources(int restoreAmount){
        amount += restoreAmount;
    }
}

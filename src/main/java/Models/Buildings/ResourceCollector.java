package Models.Buildings;

import Application.Game;
import Models.Abstracts.Building;
import Models.Singletons.Profile;
import Models.Singletons.ResourceManager;
import Service.DataService;
import Util.Models.Upgrade;

public class ResourceCollector extends Building {
    int genRate;
    int resGenID;
    int amount;

    public ResourceCollector(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
               int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int buildID, int generatedPerHour, int resourceGeneratedID, int resourceAmount){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading, buildID);
        genRate = generatedPerHour;
        resGenID = resourceGeneratedID;
        amount = resourceAmount;
    }

    @Override
    public void FinishUpgrade(){
        super.FinishUpgrade();
    }

    public int GetResourceGeneratedID(){
        return resGenID;
    }

    public int GetResourceGenerationRate(){
        return genRate;
    }
    public int GetMaxCapacity(){
        return ResourceManager.MaxCollectionHours * genRate;
    }
    public int CollectResources(float hours) {
        DataService serv = Game.getInstance().GetDataService();

        int newAmount = amount + (int) (hours * genRate);
        int maxCollectorCapacity = GetMaxCapacity();

        newAmount = Math.min(maxCollectorCapacity, newAmount);
        amount = 0;
        serv.SetResourceAmount(Profile.GetID(), GetBuildID(), amount);

        return newAmount;
    }
    public void RestoreResources(int restoreAmount){
        amount = Math.min(amount + restoreAmount, GetMaxCapacity());

        DataService serv = Game.getInstance().GetDataService();
        serv.SetResourceAmount(Profile.GetID(), GetBuildID(), amount);
    }
    public void UpdateResourceAmount(float hours){
        RestoreResources(CollectResources(hours));
    }
}

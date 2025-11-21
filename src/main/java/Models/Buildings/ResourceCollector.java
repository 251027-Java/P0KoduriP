package Models.Buildings;

import Application.Game;
import GamePackage.HomeGameScreen;
import Models.Abstracts.Building;
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
    public void Upgrade(){
        super.Upgrade();
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

        if (newAmount > maxCollectorCapacity) newAmount = maxCollectorCapacity;
        amount = 0;
        serv.SetResourceAmount(HomeGameScreen.GetProfID(), GetBuildID(), amount);

        return newAmount;
    }
    public void RestoreResources(int restoreAmount){
        amount += restoreAmount;

        DataService serv = Game.getInstance().GetDataService();
        serv.SetResourceAmount(HomeGameScreen.GetProfID(), GetBuildID(), amount);
    }
}

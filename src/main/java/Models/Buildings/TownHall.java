package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class TownHall extends Building {
    int nextcost;

    public TownHall(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                           int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int buildID, int nextCost){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading, buildID);
        nextcost = nextCost;
    }

    @Override
    public void Upgrade(){
        super.Upgrade();
    }

    public int GetNextCost(){
        return nextcost;
    }
}

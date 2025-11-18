package Models.Buildings;

import Models.Abstracts.Building;
import Util.Models.Upgrade;

public class Lab extends Building {
    int maxtrooplevel;
    public Lab(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth,
                    int buildingLevel, Upgrade upgradeInfo, boolean currentlyUpgrading, int maxTroopLevel){
        super(buildingID, buildingTypeID, buildingName, resourceID, resourceName, maxHealth, buildingLevel, upgradeInfo, currentlyUpgrading);
        maxtrooplevel = maxTroopLevel;
    }

    @Override
    public void Upgrade(){
        super.Upgrade();
    }

    public int GetMaxTroopLevel(){
        return maxtrooplevel;
    }

    public void UpgradeTroop(){

    }
}

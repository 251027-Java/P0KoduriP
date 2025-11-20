package Repository;

import Models.Buildings.*;
import Models.TroopFactory;
import Util.Models.Upgrade;

import java.util.List;
import java.util.Map;

public interface IRequirementRepository extends IRepository {
    public Map<Integer, String> GetResourceInfo();
    public int GetValuePerGem(String value);
    public Map<Integer, Integer> GetGemShopOptions();
    public List<TroopFactory> GetTroopFactories();
    public int GetTroopDamage(int troopID, int troopLevel);
    public int GetTroopHP(int troopID, int troopLevel);
    public Upgrade GetTroopUpgradeInfo(int troopID, int troopLevel);
    public Map<Integer, Integer> GetBuildingTypes();
    public Map<Integer, String> GetBuildingTypeNames();
    public Map<Integer, Integer> GetBuildingPurchaseResources();
    public Map<Integer, String> GetBuildingNames();
    public Map<Integer, Integer> GetBuildingResourcesHeld();
    public Upgrade GetBuildingUpgradeInfo(int buildingID, int buildingLevel);
    public int GetMaxNumberBuildings(int buildingTypeID, int townHallLevel);
    public int GetMaxBuildingLevel(int buildingTypeID, int townHallLevel);

    public TownHall GetTownHall(int buildID, int level, int buildingID, boolean currentlyUpgrading);
    public ArmyCamp GetArmyCamp(int buildID, int level, int buildingID, boolean currentlyUpgrading);
    public Barracks GetBarracks(int buildID, int level, int buildingID, boolean currentlyUpgrading);
    public Lab GetLab(int buildID, int level, int buildingID, boolean currentlyUpgrading);
    public ResourceCollector GetCollector(int buildID, int level, int buildingID, boolean currentlyUpgrading, int amount);
    public ResourceStorage GetStorage(int buildID, int level, int buildingID, boolean currentlyUpgrading);
    public Defense GetDefense(int buildID, int level, int buildingID, boolean currentlyUpgrading);
}

package Service;

import Models.Buildings.*;
import Models.TroopFactory;
import Repository.IRequirementRepository;
import Util.Models.Upgrade;

import java.util.List;
import java.util.Map;

public class RequirementService implements IService{
    private IRequirementRepository repo;

    public RequirementService(IRequirementRepository repository){
        repo = repository;
    }

    public Map<Integer, String> GetResourceInfo(){
        return repo.GetResourceInfo();
    }
    public int GetValuePerGem(String value){
        return repo.GetValuePerGem(value);
    }
    public Map<Integer, Integer> GetGemShopOptions(){
        return repo.GetGemShopOptions();
    }
    public List<TroopFactory> GetTroopFactories(){
        return repo.GetTroopFactories();
    }
    public int GetTroopDamage(int troopID, int troopLevel){
        return repo.GetTroopDamage(troopID, troopLevel);
    }
    public int GetTroopHP(int troopID, int troopLevel){
        return repo.GetTroopHP(troopID, troopLevel);
    }
    public Upgrade GetTroopUpgradeInfo(int troopID, int troopLevel){
        return repo.GetTroopUpgradeInfo(troopID, troopLevel);
    }
    public Map<Integer, Integer> GetBuildingTypes(){
        return repo.GetBuildingTypes();
    }
    public Map<Integer, String> GetBuildingTypeNames(){
        return repo.GetBuildingTypeNames();
    }
    public Map<Integer, Integer> GetBuildingPurchaseResources(){
        return repo.GetBuildingPurchaseResources();
    }
    public Map<Integer, String> GetBuildingNames(){
        return repo.GetBuildingNames();
    }
    public Map<Integer, Integer> GetBuildingResourcesHeld() {
        return repo.GetBuildingResourcesHeld();
    }
    public Upgrade GetBuildingUpgradeInfo(int buildingID, int buildingLevel){
        return repo.GetBuildingUpgradeInfo(buildingID, buildingLevel);
    }
    public int GetMaxNumberBuildings(int buildingTypeID, int townHallLevel){
        return repo.GetMaxNumberBuildings(buildingTypeID, townHallLevel);
    }
    public int GetMaxBuildingLevel(int buildingTypeID, int townHallLevel){
        return repo.GetMaxBuildingLevel(buildingTypeID, townHallLevel);
    }

    public TownHall GetTownHall(int buildID, int level, int buildingID, boolean currentlyUpgrading){
        return repo.GetTownHall(buildID, level, buildingID, currentlyUpgrading);
    }
    public ArmyCamp GetArmyCamp(int buildID, int level, int buildingID, boolean currentlyUpgrading){
        return repo.GetArmyCamp(buildID, level, buildingID, currentlyUpgrading);
    }
    public Barracks GetBarracks(int buildID, int level, int buildingID, boolean currentlyUpgrading){
        return repo.GetBarracks(buildID, level, buildingID, currentlyUpgrading);
    }
    public Lab GetLab(int buildID, int level, int buildingID, boolean currentlyUpgrading){
        return repo.GetLab(buildID, level, buildingID, currentlyUpgrading);
    }
    public ResourceCollector GetCollector(int buildID, int level, int buildingID, boolean currentlyUpgrading, int amount){
        return repo.GetCollector(buildID, level, buildingID, currentlyUpgrading, amount);
    }
    public ResourceStorage GetStorage(int buildID, int level, int buildingID, boolean currentlyUpgrading){
        return repo.GetStorage(buildID, level, buildingID, currentlyUpgrading);
    }
    public Defense GetDefense(int buildID, int level, int buildingID, boolean currentlyUpgrading){
        return repo.GetDefense(buildID, level, buildingID, currentlyUpgrading);
    }

    @Override
    public void DropRepo() {
        repo.DropRepo();
    }
}

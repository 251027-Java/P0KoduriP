package Service;

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

    @Override
    public void DropRepo() {
        repo.DropRepo();
    }
}

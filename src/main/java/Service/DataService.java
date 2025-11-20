package Service;

import Models.Abstracts.Building;
import Models.PaymentAccount;
import Repository.IDataRepository;
import Repository.IRequirementRepository;

import java.util.List;
import java.util.Map;

public class DataService implements IService{
    private IDataRepository dRepo;
    private IRequirementRepository rRepo;

    public DataService(IDataRepository dataRepository, IRequirementRepository reqRepository){
        dRepo = dataRepository;
        rRepo = reqRepository;
    }

    public String GetCreationDate(int profID){
        return dRepo.GetCreationDate(profID);
    }
    public float GetHoursSinceLastAttacked(int profID) {
        return dRepo.GetHoursSinceLastAttacked(profID);
    }
    public float GetHoursSinceLastCollectedResources(int profID) {
        return dRepo.GetHoursSinceLastCollectedResources(profID);
    }
    public String GetPlayerName(int profID){
        return dRepo.GetPlayerName(profID);
    }
    public int GetGems(int profID){
        return dRepo.GetGems(profID);
    }
    public int GetTrophies(int profID){
        return dRepo.GetTrophies(profID);
    }
    public boolean GetTroopIsUpgrading(int profID){
        return dRepo.GetTroopIsUpgrading(profID);
    }
    public boolean GetBuildingIsUpgrading(int profID, int buildID){
        return dRepo.GetBuildingIsUpgrading(profID, buildID);
    }
    public int GetUpgradingTroopID(int profID){
        return dRepo.GetUpgradingTroopID(profID);
    }
    public long GetTroopUpgradeTimeRemainingSeconds(int profID){
        return dRepo.GetTroopUpgradeTimeRemainingSeconds(profID);
    }
    public long GetBuildingUpgradeTimeRemainingSeconds(int profID, int buildID){
        return dRepo.GetBuildingUpgradeTimeRemainingSeconds(profID, buildID);
    }
    public List<PaymentAccount> GetPaymentAccounts(int profID){
        return dRepo.GetPaymentAccounts(profID);
    }
    public Map<Integer, Integer> GetUserTroopLevels(int profID){
        return dRepo.GetUserTroopLevels(profID);
    }
    public Map<Integer, Integer> GetPlayerBuildingLineup(int profID){
        return dRepo.GetPlayerBuildingLineup(profID);
    }
    public List<Building> GetPlayerBuildings(int profID){
        return dRepo.GetPlayerBuildings(profID);
    }
    public int GetResourceAmount(int profID, int buildID){
        return dRepo.GetResourceAmount(profID, buildID);
    }

    @Override
    public void DropRepo() {
        dRepo.DropRepo();
    }
}

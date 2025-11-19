package Service;

import Repository.IDataRepository;
import Repository.IRequirementRepository;

public class DataService implements IService{
    private IDataRepository dRepo;
    private IRequirementRepository rRepo;

    public DataService(IDataRepository dataRepository, IRequirementRepository reqRepository){
        dRepo = dataRepository;
        rRepo = reqRepository;
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
    public int GetUpgradingTroopID(int profID){
        return dRepo.GetUpgradingTroopID(profID);
    }
    public long GetTroopUpgradeTimeRemainingSeconds(int profID){
        return dRepo.GetTroopUpgradeTimeRemainingSeconds(profID);
    }

    @Override
    public void DropRepo() {
        dRepo.DropRepo();
    }
}

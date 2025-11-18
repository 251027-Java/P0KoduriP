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

    @Override
    public void DropRepo() {
        dRepo.DropRepo();
    }
}

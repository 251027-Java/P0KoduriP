package Service;

import Models.TroopFactory;
import Repository.IRequirementRepository;

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

    @Override
    public void DropRepo() {
        repo.DropRepo();
    }
}

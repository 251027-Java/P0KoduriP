package Service;

import Repository.IRequirementRepository;

public class RequirementService implements IService{
    private IRequirementRepository repo;

    public RequirementService(IRequirementRepository repository){
        repo = repository;
    }

    @Override
    public void DropRepo() {
        repo.DropRepo();
    }
}

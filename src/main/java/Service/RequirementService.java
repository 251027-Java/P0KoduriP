package Service;

import Repository.IRequirementRepository;

public class RequirementService {
    private IRequirementRepository repo;

    public RequirementService(IRequirementRepository repository){
        repo = repository;
    }
}

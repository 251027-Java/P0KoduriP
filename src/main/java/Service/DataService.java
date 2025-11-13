package Service;

import Repository.IDataRepository;

public class DataService implements IService{
    private IDataRepository repo;

    public DataService(IDataRepository repository){
        repo = repository;
    }

    @Override
    public void DropRepo() {
        repo.DropRepo();
    }
}

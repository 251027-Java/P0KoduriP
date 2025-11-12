package Service;

import Repository.IDataRepository;

public class DataService {
    private IDataRepository repo;

    public DataService(IDataRepository repository){
        repo = repository;
    }
}

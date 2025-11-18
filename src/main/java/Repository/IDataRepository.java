package Repository;

public interface IDataRepository extends IRepository {
    public float GetHoursSinceLastAttacked(int profID);
    public float GetHoursSinceLastCollectedResources(int profID);
}

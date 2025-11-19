package Repository;

public interface IDataRepository extends IRepository {
    public float GetHoursSinceLastAttacked(int profID);
    public float GetHoursSinceLastCollectedResources(int profID);
    public String GetPlayerName(int profID);
    public int GetGems(int profID);
    public int GetTrophies(int profID);
    public boolean GetTroopIsUpgrading(int profID);
    public int GetUpgradingTroopID(int profID);
    public long GetTroopUpgradeTimeRemainingSeconds(int profID);
}

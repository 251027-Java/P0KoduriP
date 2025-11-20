package Repository;

import Models.Abstracts.Building;
import Models.PaymentAccount;

import java.util.List;
import java.util.Map;

public interface IDataRepository extends IRepository {
    public String GetCreationDate(int profID);
    public float GetHoursSinceLastAttacked(int profID);
    public float GetHoursSinceLastCollectedResources(int profID);
    public String GetPlayerName(int profID);
    public int GetGems(int profID);
    public int GetTrophies(int profID);
    public boolean GetTroopIsUpgrading(int profID);
    public boolean GetBuildingIsUpgrading(int profID, int buildID);
    public int GetUpgradingTroopID(int profID);
    public long GetTroopUpgradeTimeRemainingSeconds(int profID);
    public long GetBuildingUpgradeTimeRemainingSeconds(int profID, int buildID);
    public List<PaymentAccount> GetPaymentAccounts(int profID);
    public Map<Integer, Integer> GetUserTroopLevels(int profID); // TroopID -> TroopLevel
    public Map<Integer, Integer> GetPlayerBuildingLineup(int profID);
    public List<Building> GetPlayerBuildings(int profID);
    public int GetResourceAmount(int profID, int buildID);
    public Map<Integer, Integer> GetPlayerArmy(int profID);
}

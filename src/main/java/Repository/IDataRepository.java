package Repository;

import Models.Abstracts.Building;
import Models.PaymentAccount;

import java.util.List;
import java.util.Map;

public interface IDataRepository extends IRepository {
    public int GetNumberOfProfiles();
    public List<String> DisplayGameAccounts();
    public List<Integer> GetGameAccountIDs();
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
    public Map<Integer, Integer> GetPlayerBuildingLineup(int profID); // buildID -> pos
    public List<Building> GetPlayerBuildings(int profID);
    public int GetResourceAmount(int profID, int buildID);
    public Map<Integer, Integer> GetPlayerArmy(int profID);

    public boolean GetPaymentAccountExists(long cardNo);
    public boolean GetPaymentAccountExists(long cardNo, int expMo, int expYr, int pin);
    public void AddNewCard(long cardNo, int expMo, int expYr, int pin);
    public void DeleteCard(long cardNo, int expMo, int expYr, int pin);
    public List<String> DisplayGameAccountsToAddToCard(long cardNo);
    public List<String> DisplayGameAccountsToRemoveFromCard(long cardNo);
    public List<Integer> GetGameAccountIDsToAddToCard(long cardNo);
    public List<Integer> GetGameAccountsIDsToRemoveFromCard(long cardNo);
    public boolean GetAccountsAreConnected(long cardNo, int profID);
    public void AddGameAccountToCard(long cardNo, int profID);
    public void RemoveGameAccountFromCard(long cardNo, int profID);
    public void MakeDeposit(int deposit, long cardNo, int expMo, int expYr, int pin);
    public void MakeWithdrawal(int withdrawal, long cardNo, int expMo, int expYr, int pin);
    public int GetBalance(long cardNo, int expMo, int expYr, int pin);

    public boolean CreateProfile(int profID, String name); //true if successful creation, false if not
    public void UpdateCollectedResourcesTime(int profID);
    public void SetResourceAmount(int profID, int buildID, int amount);
}

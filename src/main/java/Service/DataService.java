package Service;

import Models.Abstracts.Building;
import Models.PaymentAccount;
import Repository.IDataRepository;
import Repository.IRequirementRepository;

import java.util.List;
import java.util.Map;

public class DataService implements IService{
    private IDataRepository dRepo;

    public DataService(IDataRepository dataRepository){
        dRepo = dataRepository;
    }

    public int GetNumberOfProfiles(){
        return dRepo.GetNumberOfProfiles();
    }
    public List<String> DisplayGameAccounts(){
        return dRepo.DisplayGameAccounts();
    }
    public List<Integer> GetGameAccountIDs(){
        return dRepo.GetGameAccountIDs();
    }
    public String GetCreationDate(int profID){
        return dRepo.GetCreationDate(profID);
    }
    public float GetHoursSinceLastAttacked(int profID) {
        return dRepo.GetHoursSinceLastAttacked(profID);
    }
    public float GetHoursSinceLastCollectedResources(int profID) {
        return dRepo.GetHoursSinceLastCollectedResources(profID);
    }
    public String GetPlayerName(int profID){
        return dRepo.GetPlayerName(profID);
    }
    public int GetGems(int profID){
        return dRepo.GetGems(profID);
    }
    public int GetTrophies(int profID){
        return dRepo.GetTrophies(profID);
    }
    public boolean GetTroopIsUpgrading(int profID){
        return dRepo.GetTroopIsUpgrading(profID);
    }
    public boolean GetBuildingIsUpgrading(int profID, int buildID){
        return dRepo.GetBuildingIsUpgrading(profID, buildID);
    }
    public int GetUpgradingTroopID(int profID){
        return dRepo.GetUpgradingTroopID(profID);
    }
    public long GetTroopUpgradeTimeRemainingSeconds(int profID){
        return dRepo.GetTroopUpgradeTimeRemainingSeconds(profID);
    }
    public long GetBuildingUpgradeTimeRemainingSeconds(int profID, int buildID){
        return dRepo.GetBuildingUpgradeTimeRemainingSeconds(profID, buildID);
    }
    public List<PaymentAccount> GetPaymentAccounts(int profID){
        return dRepo.GetPaymentAccounts(profID);
    }
    public Map<Integer, Integer> GetUserTroopLevels(int profID){
        return dRepo.GetUserTroopLevels(profID);
    }
    public Map<Integer, Integer> GetPlayerBuildingLineup(int profID){
        return dRepo.GetPlayerBuildingLineup(profID);
    }
    public List<Building> GetPlayerBuildings(int profID){
        return dRepo.GetPlayerBuildings(profID);
    }
    public int GetResourceAmount(int profID, int buildID){
        return dRepo.GetResourceAmount(profID, buildID);
    }
    public Map<Integer, Integer> GetPlayerArmy(int profID){
        return dRepo.GetPlayerArmy(profID);
    }

    public boolean GetPaymentAccountExists(long cardNo){
        return dRepo.GetPaymentAccountExists(cardNo);
    }
    public boolean GetPaymentAccountExists(long cardNo, int expMo, int expYr, int pin){
        return dRepo.GetPaymentAccountExists(cardNo, expMo, expYr, pin);
    }
    public void AddNewCard(long cardNo, int expMo, int expYr, int pin){
        if (!dRepo.GetPaymentAccountExists(cardNo)) dRepo.AddNewCard(cardNo, expMo, expYr, pin);
    }
    public void DeleteCard(long cardNo, int expMo, int expYr, int pin){
        if (dRepo.GetPaymentAccountExists(cardNo, expMo, expYr, pin)) dRepo.DeleteCard(cardNo, expMo, expYr, pin);
    }
    public List<String> DisplayGameAccountsToAddToCard(long cardNo){
        return dRepo.DisplayGameAccountsToAddToCard(cardNo);
    }
    public List<String> DisplayGameAccountsToRemoveFromCard(long cardNo){
        return dRepo.DisplayGameAccountsToRemoveFromCard(cardNo);
    }
    public List<Integer> GetGameAccountIDsToAddToCard(long cardNo){
        return dRepo.GetGameAccountIDsToAddToCard(cardNo);
    }
    public List<Integer> GetGameAccountsIDsToRemoveFromCard(long cardNo){
        return dRepo.GetGameAccountsIDsToRemoveFromCard(cardNo);
    }
    public boolean GetAccountsAreConnected(long cardNo, int profID){
        return dRepo.GetAccountsAreConnected(cardNo, profID);
    }
    public void AddGameAccountToCard(long cardNo, int profID){
        if (!dRepo.GetAccountsAreConnected(cardNo, profID)) dRepo.AddGameAccountToCard(cardNo, profID);
    }
    public void RemoveGameAccountFromCard(long cardNo, int profID){
        if (dRepo.GetAccountsAreConnected(cardNo, profID)) dRepo.RemoveGameAccountFromCard(cardNo, profID);
    }
    public void MakeDeposit(int deposit, long cardNo, int expMo, int expYr, int pin){
        if (dRepo.GetPaymentAccountExists(cardNo, expMo, expYr, pin)) dRepo.MakeDeposit(deposit, cardNo, expMo, expYr, pin);
    }
    public void MakeWithdrawal(int withdrawal, long cardNo, int expMo, int expYr, int pin){
        if (dRepo.GetPaymentAccountExists(cardNo, expMo, expYr, pin) && dRepo.GetBalance(cardNo, expMo, expYr, pin) >= withdrawal) {
            dRepo.MakeWithdrawal(withdrawal, cardNo, expMo, expYr, pin);
        }
    }
    public int GetBalance(long cardNo, int expMo, int expYr, int pin){
        if (dRepo.GetPaymentAccountExists(cardNo, expMo, expYr, pin)) return dRepo.GetBalance(cardNo, expMo, expYr, pin);
        return -1;
    }

    public boolean CreateProfile(int profID, String name){
        return dRepo.CreateProfile(profID, name);
    }

    @Override
    public void DropRepo() {
        dRepo.DropRepo();
    }
}

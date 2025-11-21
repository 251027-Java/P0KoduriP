package Models.Singletons;

import Application.Game;
import Models.PaymentAccount;
import Service.DataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {
    private final static Profile profile = new Profile();
    private Profile() {}
    public static Profile getInstance() {return profile;}

    private static int id;

    private String name;
    private Map<Long, PaymentAccount> payOptions;

    private int trophies;
    private int gems;
    private boolean troopIsUpgrading;
    private int upgradingTroop;

    public static void LoadProfile(int profID){
        DataService serv = Game.getInstance().GetDataService();
        profile.setProfile(profID, serv.GetPlayerName(profID));
        profile.fillProfile(serv.GetTrophies(profID), serv.GetGems(profID),
                serv.GetTroopIsUpgrading(profID), serv.GetUpgradingTroopID(profID), serv.GetPaymentAccounts(profID));
    }
    private void setProfile(int profID, String profName){
        id = profID;
        name = profName;
    }
    private void fillProfile(int profTrophies, int profGems, boolean troopCurrentlyUpgrading, int upgradingTroopID, List<PaymentAccount> paymentAccounts){ //unknown value of upgradingTroopID if troop isn't upgrading
        trophies = profTrophies;
        gems = profGems;
        upgradingTroop = upgradingTroopID;
        troopIsUpgrading = troopCurrentlyUpgrading;

        payOptions = new HashMap<>();
        for (PaymentAccount p : paymentAccounts){
            payOptions.put(p.GetCardNumber(), p);
        }
    }
    public static int GetID(){
        return id;
    }

    public void AddPaymentOption(PaymentAccount paymentAccount){
        payOptions.put(paymentAccount.GetCardNumber(), paymentAccount);
    }
    public void RemovePaymentOption(PaymentAccount paymentAccount){
        payOptions.remove(paymentAccount);
    }
    public Map<Long, PaymentAccount> GetPaymentOptions(){
    return payOptions;
    }
    public String GetName(){
        return name;
    }

    public int GetTrophies(){
        return trophies;
    }
    public int GetGems(){
        return gems;
    }
    public void UpgradeTroop(int upgradingTroopID){
        troopIsUpgrading = true;
        upgradingTroop = upgradingTroopID;
    }
    public void FinishUpgradingTroop(){
        troopIsUpgrading = false;
    }
    public boolean GetTroopIsCurrentlyUpgrading(){
        return troopIsUpgrading;
    }
    public int GetUpgradingTroop(){ //unknown value if troop is actually not upgrading
        return upgradingTroop;
    }
    public long GetTroopUpgradeTimeRemainingSeconds(){
        return Game.getInstance().GetDataService().GetTroopUpgradeTimeRemainingSeconds(id);
    }

    public void ChangeTrophies(int changeTrophies){
        trophies += changeTrophies;
        if (trophies < 0) trophies = 0;
        Game.getInstance().GetDataService().SetTrophies(id, trophies);
    }
    public void ChangeGems(int changeGems){
        gems += changeGems;
        if (gems < 0) gems = 0;
        Game.getInstance().GetDataService().SetGems(id, gems);
    }
}

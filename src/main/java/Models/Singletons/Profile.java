package Models.Singletons;

import Models.PaymentAccount;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private final static Profile profile = new Profile();
    private Profile() {}
    public static Profile getInstance() {return profile;}

    private int id;
    private String name;
    private List<PaymentAccount> payOptions = new ArrayList<>();

    private int trophies;
    private int gems;
    private int gold;
    private int elixir;

    public void SetProfile(int profID, String profName){
        id = profID;
        name = profName;
        payOptions = new ArrayList<>();
    }
    public void FillProfile(int profTrophies, int profGems, int profGold, int profElixir){
        trophies = profTrophies;
        gems = profGems;
        gold = profGold;
        elixir = profElixir;
    }

    public void AddPaymentOption(PaymentAccount paymentAccount){
        payOptions.add(paymentAccount);
    }
    public void RemovePaymentOption(PaymentAccount paymentAccount){
        payOptions.remove(paymentAccount);
    }
    public List<PaymentAccount> GetPaymentOptions(){
        return new ArrayList<>(payOptions);
    }
    public int GetID(){
        return id;
    }
    public String GetName(){
        return name;
    }

    public int GetTrophies(){
        return trophies;
    }
    public int GetGames(){
        return gems;
    }
    public int GetGold(){
        return gold;
    }
    public int GetElixir(){
        return elixir;
    }

    public void ChangeTrophies(int changeTrophies){
        trophies += changeTrophies;
        if (trophies < 0) trophies = 0;
    }
    public void ChangeGems(int changeGems){
        gems += changeGems;
        if (gems < 0) gems = 0;
    }
    public void ChangeGold(int changeGold){
        gold += changeGold;
        if (gold < 0) gold = 0;
    }
    public void ChangeElixir(int changeElixir){
        elixir += changeElixir;
        if (elixir < 0) elixir = 0;
    }
}

package Models;

import Models.Singletons.GemShop;

import java.util.Map;

public class PaymentAccount {
    private final long cardno;
    private final int expmonth;
    private final int expyr;
    private final int pin;
    private int balance;

    public PaymentAccount(long cardNumber, int expMonth, int expYear, int securityPin, int acctBalance){
        cardno = cardNumber;
        expmonth = expMonth;
        expyr = expYear;
        pin = securityPin;
        balance = acctBalance;
    }

    public Map<Integer, Integer> GetPaymentOptions(){
        return GemShop.GetAvailableOptions(balance);
    }

    public void Deposit(int usd){
        balance += usd;
    }

    public int Withdraw(int usd){
        int withdraw = usd;
        if (withdraw > balance) withdraw = balance;

        balance -= withdraw;

        return withdraw;
    }

    public void GemPurchase(int usd){
        int gems = GemShop.GetGems(usd);
        Withdraw(usd);
    }
}

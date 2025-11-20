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

    public int GetBalance(){
        return balance;
    }

    public void GemPurchase(int usd){
        int gems = GemShop.GetGems(usd);
        if (gems > 0) Withdraw(usd);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Reference equality
        if (obj == null) return false; // Null check
        if (getClass() != obj.getClass()) return false; // Type check

        PaymentAccount o = (PaymentAccount) obj;

        return cardno==o.cardno && expmonth==o.expmonth && expyr==o.expyr;
    }
}

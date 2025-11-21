package Models;

import Application.Game;
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

    public void Deposit(int depositUSD){
        balance += depositUSD;
        Game.getInstance().GetDataService().MakeDeposit(depositUSD, cardno, expmonth, expyr, pin);
    }

    public int Withdraw(int withdrawUSD){
        if (withdrawUSD > balance) withdrawUSD = balance;

        balance -= withdrawUSD;

        Game.getInstance().GetDataService().MakeWithdrawal(withdrawUSD, cardno, expmonth, expyr, pin);
        return withdrawUSD;
    }

    public long GetCardNumber(){
        return cardno;
    }
    public int GetBalance(){
        return balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Reference equality
        if (obj == null) return false; // Null check
        if (getClass() != obj.getClass()) return false; // Type check

        PaymentAccount o = (PaymentAccount) obj;

        return cardno==o.cardno && expmonth==o.expmonth && expyr==o.expyr && pin==o.pin;
    }
}

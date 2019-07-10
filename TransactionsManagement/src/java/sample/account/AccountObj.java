/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.account;

import java.io.Serializable;

/**
 *
 * @author welcome
 */
public class AccountObj implements Serializable {
    private String accountID;
    private Float balance;

    public AccountObj() {
    }

    public AccountObj(String accountID, Float balance) {
        this.accountID = accountID;
        this.balance = balance;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
    public void updateBalance(Float amount){
        this.balance+=amount;
    }
}

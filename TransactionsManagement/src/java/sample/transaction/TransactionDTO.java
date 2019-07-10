/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.transaction;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author User
 */
public class TransactionDTO implements Serializable{
    private String transID;
    private String date;
    private String type;
    private float amount;
    private String reason;

    public TransactionDTO() {
    }
    
    public TransactionDTO(String transID,String date, int type, float amount, String reason) throws ParseException {
        this.transID=transID;
        this.date=date;
        setType(type);
        this.amount = amount;
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }
    

    public String getType() {
        return type;
    }

    public void setType(int type) {
        switch (type) {
            case 1:
                this.setType("Deposit");
                break;
            case 2:
                this.setType("Transfer");
                break;
            case 3:
                this.setType("Profit");
                break;
            case 0:
                this.setType("Withdrawn");
                break;
        }
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the transID
     */
    public String getTransID() {
        return transID;
    }

    /**
     * @param transID the transID to set
     */
    public void setTransID(String transID) {
        this.transID = transID;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}

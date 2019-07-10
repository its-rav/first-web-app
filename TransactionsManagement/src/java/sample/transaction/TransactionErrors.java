/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.transaction;

/**
 *
 * @author welcome
 */
public class TransactionErrors {
    private String amountError;// 1000<=transAmount, transAmount<=balance, float format, error empty account 
    private String accountError;//account not existed 
    private String emptyAmountError;//empty amount
    private String emptyAccountError;//empty account
    private String serverError;//server Error
    private String emptyDateError;
    private String dateFormatError;
    public TransactionErrors() {
    }
    
    

    public String getAmountError() {
        return amountError;
    }

    public String getAccountError() {
        return accountError;
    }

    public void setAmountError(String amountError) {
        this.amountError = amountError;
    }

    public void setAccountError(String accountError) {
        this.accountError = accountError;
    }

    /**
     * @return the emptyAmountError
     */
    public String getEmptyAmountError() {
        return emptyAmountError;
    }

    /**
     * @param emptyAmountError the emptyAmountError to set
     */
    public void setEmptyAmountError(String emptyAmountError) {
        this.emptyAmountError = emptyAmountError;
    }

    /**
     * @return the emptyAccountError
     */
    public String getEmptyAccountError() {
        return emptyAccountError;
    }

    /**
     * @param emptyAccountError the emptyAccountError to set
     */
    public void setEmptyAccountError(String emptyAccountError) {
        this.emptyAccountError = emptyAccountError;
    }

    /**
     * @return the serverError
     */
    public String getServerError() {
        return serverError;
    }

    /**
     * @param serverError the serverError to set
     */
    public void setServerError(String serverError) {
        this.serverError = serverError;
    }

    /**
     * @return the emptyDateError
     */
    public String getEmptyDateError() {
        return emptyDateError;
    }

    /**
     * @param emptyDateError the emptyDateError to set
     */
    public void setEmptyDateError(String emptyDateError) {
        this.emptyDateError = emptyDateError;
    }

    /**
     * @return the dateFormatError
     */
    public String getDateFormatError() {
        return dateFormatError;
    }

    /**
     * @param dateFormatError the dateFormatError to set
     */
    public void setDateFormatError(String dateFormatError) {
        this.dateFormatError = dateFormatError;
    }
                
    
    
}

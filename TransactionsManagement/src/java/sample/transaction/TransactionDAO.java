/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.transaction;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import sample.Utils.DBUtilities;

/**
 *
 * @author welcome
 */
public class TransactionDAO implements Serializable {

    List<TransactionDTO> listTransactions;

    public List<TransactionDTO> getListTransactions() {
        return listTransactions;
    }

    public TransactionDAO() {
    }

    public boolean checkAccount(String accountID) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DBUtilities.makeConnection();
            if (con != null) {
                String sql = "SELECT accountID "
                        + "FROM tbl_account "
                        + "WHERE accountID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, accountID);
                rs = pst.executeQuery();
                if (rs.next()) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }

        }
        return false;
    }

    public boolean transfer(String thisAccount, String receivedccountID, float transAmount) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DBUtilities.makeConnection();
            if (con != null) {
                con.setAutoCommit(false);
                String sql = "UPDATE tbl_account\n"
                        + "SET balance=balance-?\n"
                        + "WHERE accountID=?";
                pst = con.prepareStatement(sql);
                pst.setFloat(1, transAmount);
                pst.setString(2, thisAccount);
                if (pst.executeUpdate() > 0) {
                    sql = "UPDATE tbl_account\n"
                            + "SET balance=balance+?\n"
                            + "WHERE accountID=?";
                    pst = con.prepareStatement(sql);
                    pst.setFloat(1, transAmount);
                    pst.setString(2, receivedccountID);
                    String now = getCurrentDatetime();
                    if (pst.executeUpdate() > 0) {
                        sql = "INSERT INTO tbl_transaction(accountID,amount,reason,status,transDate,type) \n"
                                + "VALUES(?,?,?,?,?,?)";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, thisAccount);
                        pst.setFloat(2, transAmount);
                        pst.setString(3, thisAccount + " transfered to " + receivedccountID + " " + transAmount + " VND.");
                        pst.setBoolean(4, true);
                        pst.setString(5, now);
                        pst.setInt(6, 2);//transfer
                        if (pst.executeUpdate() > 0) {
                            sql = "INSERT INTO tbl_transaction(accountID,amount,reason,status,transDate,type) \n"
                                    + "VALUES(?,?,?,?,?,?)";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, receivedccountID);
                            pst.setFloat(2, transAmount);
                            pst.setString(3, receivedccountID + " received from " + thisAccount + " " + transAmount + " VND.");
                            pst.setBoolean(4, true);
                            pst.setString(5, now);
                            pst.setInt(6, 2);//transfer
                            if (pst.executeUpdate() > 0) {
                                con.commit();
                                return true;
                            }//insert transaction for received account for deposit
                        }//insert transaction for this account for transfer
                    }//+revceied account balance
                }//-this account balance
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    private String getCurrentDatetime() {
        java.util.Date today = new java.util.Date();

        DateFormat sqlFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sqlFormat.format(today);
    }

    public void listTransactions(String accountID, String fromDate, String toDate) throws NamingException, SQLException, ParseException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DBUtilities.makeConnection();
            if (con != null) {
                String sql = "";
                if (fromDate.trim().length() > 0 || toDate.trim().length() > 0) {
                    if (toDate.trim().length() == 0) {
                        sql = "SELECT transID,FORMAT(transDate, 'dd/MM/yyyy HH:mm:ss') AS transDate,type,amount,reason \n"
                                + "FROM tbl_transaction \n"
                                + "WHERE accountID=? AND status=1 AND transDate >=?";//without toDate
                        pst = con.prepareStatement(sql);
                        pst.setString(1, accountID);
                        pst.setString(2, fromDate);
                    } else if (fromDate.trim().length() == 0) {
                        sql = "SELECT transID, FORMAT(transDate, 'dd/MM/yyyy HH:mm:ss') AS transDate,type,amount,reason "
                                + "FROM tbl_transaction "
                                + "WHERE accountID=? AND status=1 AND transDate <=?";//without fromDate
                        pst = con.prepareStatement(sql);
                        pst.setString(1, accountID);
                        pst.setString(2, toDate);
                    } else {
                        sql = "SELECT transID, FORMAT(transDate, 'dd/MM/yyyy HH:mm:ss') AS transDate,type,amount,reason \n"
                                + "FROM tbl_transaction \n"
                                + "WHERE accountID=? AND status=1 AND transDate between ? and  ?";//both
                        pst = con.prepareStatement(sql);
                        pst.setString(1, accountID);
                        pst.setString(2, fromDate);
                        pst.setString(3, toDate);
                    }
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        String transID = rs.getString("transID");
                        String transDate = rs.getString("transDate");
                        int type = rs.getInt("type");
                        Float amount = rs.getFloat("amount");
                        String reason = rs.getString("reason");
                        TransactionDTO dto = new TransactionDTO(transID, transDate, type, amount, reason);
                        if (listTransactions == null) {
                            this.listTransactions = new ArrayList<>();
                        }//emd if listTransaction is null
                        listTransactions.add(dto);
                    }

                } else {
                    return;
                }
                pst = con.prepareStatement(sql);

            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean hideTransaction(String transID) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DBUtilities.makeConnection();
            if (con != null) {
                String sql = "UPDATE tbl_transaction\n"
                        + "SET status=0\n"
                        + "WHERE transID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, transID);
                int rs = pst.executeUpdate();
                if (rs>0) {
                    return true;
                }
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }

        }
        return false;
    }

}

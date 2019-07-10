/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.login;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import sample.Utils.DBUtilities;

/**
 *
 * @author welcome
 */
public class LoginDAO implements Serializable {

    public int checkLogin(String username, String PIN) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DBUtilities.makeConnection();
            if (con != null) {
                String sql = "SELECT isExpired "
                        + "FROM tbl_account "
                        + "WHERE accountID=? and pin=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, PIN);
                rs = pst.executeQuery();
                if (rs.next()) {
                    return rs.getInt("isExpired");
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
        return -1;
    }

    public Float getBalance(String accountID) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DBUtilities.makeConnection();
            if (con != null) {
                String sql = "SELECT balance "
                        + "FROM tbl_account "
                        + "WHERE accountID = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, accountID);
                rs = pst.executeQuery();
                if (rs.next()) {
                    return rs.getFloat("balance");
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
        return Float.parseFloat("-1");
    }

}

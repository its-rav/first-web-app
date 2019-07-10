/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.account.AccountObj;
import sample.transaction.TransactionDAO;
import sample.transaction.TransactionErrors;

/**
 *
 * @author welcome
 */
public class TransferServlet extends HttpServlet {
    final private String TRANSACTION_PAGE = "transactionpage.jsp";
    final private String ERROR_PAGE = "transfererrorpage.jsp";
    final private String  LOGIN_PAGE="login.html";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = LOGIN_PAGE;
        boolean flagError = false;
        TransactionErrors errors = new TransactionErrors();
        HttpSession session = request.getSession(false);

        try {
            if (session != null) {
                //from req
                String transAmountStr = request.getParameter("txtTransAmount");
                String receivedccountID = request.getParameter("txtAccountID");
                float transAmount = 0;

                //from session
                AccountObj user = (AccountObj) session.getAttribute("USER");
                Float currentBalance = user.getBalance();
                String thisAccount = user.getAccountID();

                if (transAmountStr.trim().length() > 0 && receivedccountID.trim().length() > 0) {
                    try {
                        transAmount = Float.parseFloat(transAmountStr);
                        if (transAmount < 1000) {
                            flagError = true;
                            //error transAmount>=1000
                            errors.setAmountError("Transfer amount must be greater than or equal 1000 VND.");
                        } else if (transAmount > currentBalance) {
                            //error transAmount > current Balance
                            flagError = true;
                            errors.setAmountError("Transfer amount must be less than or equal balance.");
                        }
                    } catch (NumberFormatException nfe) {
                        flagError = true;
                        //error number format
                        errors.setAmountError("Transfer amount must be a real number.");
                    }
                    TransactionDAO dao = new TransactionDAO();
                    boolean checkAccount = dao.checkAccount(receivedccountID);
                    if (checkAccount) {
                        boolean rs = dao.transfer(thisAccount, receivedccountID, transAmount);
                        if (rs) {
                            //set Balance
                            url=TRANSACTION_PAGE;
                            user.updateBalance(-transAmount);
                            session.setAttribute("USER", user);
                        }
                    } else {
                        flagError = true;
                        //error account not existed
                        errors.setAccountError("Account does not exist.");
                    }

                } else {
                    flagError = true;
                    if (transAmountStr.trim().length() == 0) {
                        errors.setEmptyAmountError("transfer amount field must not be empty.");
                    } //error empty amount
                    if (receivedccountID.trim().length() == 0) {
                        errors.setEmptyAccountError("Receiver account field must not be empty.");
                    }//error empty account 
                }

            }//end if session != null

        } catch (NamingException ex) {
            log("TransferServlet NamingException: " + ex.getMessage());
            //loi server
            flagError = true;
            errors.setServerError("Server overload");
        } catch (SQLException ex) {
            log("TransferServlet SQLException: " + ex.getMessage());
            //loi SQL
            flagError = true;
            errors.setServerError("Server overload");
            
        } finally {
            if (flagError) {
                request.setAttribute("TRANSFER_ERRORS", errors);
                url=ERROR_PAGE;
            }
            response.sendRedirect(url);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.account.AccountObj;
import sample.login.LoginDAO;
import sample.transaction.TransactionDAO;
import sample.transaction.TransactionDTO;
import sample.transaction.TransactionErrors;

/**
 *
 * @author welcome
 */
public class PrintTransactionServlet extends HttpServlet {

    final private String PRINT_TRANSACTIONS_PAGE = "printtransaction.jsp";
    final private String ERROR_PRINT_TRANSACTIONS_PAGE = "printtransaction.jsp";

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
        String fromDate = request.getParameter("txtFromDate");
        String toDate = request.getParameter("txtToDate");
        String url = PRINT_TRANSACTIONS_PAGE;
        TransactionErrors errors = new TransactionErrors();
        boolean flagError = false;
        HttpSession session = request.getSession(false);
        LoginDAO logindao=new LoginDAO();
        try {
            if (session != null) {
                AccountObj user = (AccountObj) session.getAttribute("USER");
                String accountID = user.getAccountID();
                //reupdate balance
                Float balance = logindao.getBalance(accountID);
                user.setBalance(balance);
                session.setAttribute("USER", user);
                //
                DateFormat clientInputFormat = new SimpleDateFormat("dd/MM/yyyy");
                clientInputFormat.setLenient(false);
                DateFormat sqlFormat = new SimpleDateFormat("yyyy/MM/dd");
                sqlFormat.setLenient(false);
                if (fromDate.trim().length() > 0 || toDate.trim().length() > 0) {
                    TransactionDAO dao = new TransactionDAO();
                    try {
                        if (toDate.trim().length() > 0) {
                            Date date = clientInputFormat.parse(toDate);
                            toDate = sqlFormat.format(date) + " 23:59:59";//+23:59:59 because default will be 00:00:00
                        }
                        if (fromDate.trim().length() > 0) {
                            Date date = clientInputFormat.parse(fromDate);
                            fromDate = sqlFormat.format(date);
                        }
                    } catch (ParseException ex) {
                        //loi format
                        flagError = true;
                        errors.setDateFormatError("Date must be in dd/mm/yyy format.");
                        return;
                    }
                    try {
                        dao.listTransactions(accountID, fromDate, toDate);
                    } catch (ParseException e) {
                        //loi format
                        flagError = true;
                        log("PrintTransactionServlet  ParseException: " + e.getMessage());
                        errors.setServerError("Server overload.");
                        return;
                    }
                    List<TransactionDTO> listTransactions = dao.getListTransactions();
                    request.setAttribute("LIST_TRANSACTIONS", listTransactions);
                    url = PRINT_TRANSACTIONS_PAGE;
                } else {
                    flagError = true;
                    errors.setEmptyDateError("Both From Date and To Date field cannot be empty.");
                }

            }
        } catch (NamingException ex) {
            log("PrintTransactionServlet  NamingException: " + ex.getMessage());
            flagError = true;
            errors.setServerError("Server overload");
        } catch (SQLException ex) {
            log("PrintTransactionServlet  SQLException: " + ex.getMessage());
            flagError = true;
            errors.setServerError("Server overload");
        } finally {
            if (flagError) {
                request.setAttribute("PRINT_TRANS_ERRORS", errors);
            }
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
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

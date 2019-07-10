/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.account.AccountObj;
import sample.login.LoginDAO;

/**
 *
 * @author welcome
 */
public class LoginServlet extends HttpServlet {

    final private String VALID_LOGIN_PAGE = "transactionpage.jsp";
    final private String INVALID_LOGIN_PAGE = "invalidLogin.html";

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
        String accountID = request.getParameter("txtUsername");
        String pin = request.getParameter("txtPIN");
        String url = INVALID_LOGIN_PAGE;
        try {
            if (accountID.trim().length() > 0 && pin.trim().length() > 0) {
                boolean isLocked = false;//is account locked,default is not locked
                int loginCount = 1;
                Cookie currentCookie = null;//current Cookie
                Cookie[] cookies = request.getCookies();//get cookies

                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(accountID)) {
                            loginCount = Integer.parseInt(cookie.getValue());
                            if (loginCount == 3) {
                                isLocked = true;//lock account 
                                cookie.setMaxAge(60 * 5);//for 5 mins
                            } //lock account
                            currentCookie = cookie;
                            break;
                        }//existed cookie
                    }//end if no existed cookie
                    //if  no existed cookie
//                    currentCookie = new Cookie(accountID, "" + 1);//login Attempt =1
//                    currentCookie.setMaxAge(60 * 5);//for 5 mins
                }//end if cookies != null
                if (!isLocked) {//if not locked
                    LoginDAO dao = new LoginDAO();
                    int result = dao.checkLogin(accountID, pin);
                    if (result >= 0) {
                        HttpSession session = request.getSession(true);
                        Float balance = dao.getBalance(accountID);
                        session.setAttribute("USER", new AccountObj(accountID, balance));
                        url = VALID_LOGIN_PAGE;
                        if (currentCookie != null) {
                            currentCookie.setMaxAge(0);//expired cookie
                        }//remove current cookie by set Age =0
                    } else {//login fail
                        if (currentCookie != null) {
                            loginCount++;
                            currentCookie.setValue("" + loginCount);
                        } else {//update currentCookie by increase loginCount
                            currentCookie = new Cookie(accountID, "" + loginCount);
                        }//new Cookie with current loginCount, 1 by default
                        currentCookie.setMaxAge(60 * 5);//5 mins
                    }
                }
                if (currentCookie != null) {
                    response.addCookie(currentCookie);//add to response if there is any cookie
                }
            }
        } catch (NamingException ex) {
            log("LoginServlet NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("LoginServlet SQLException: " + ex.getMessage());
        } finally {
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

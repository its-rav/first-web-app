<%-- 
    Document   : transfer
    Created on : Jul 6, 2019, 11:06:11 PM
    Author     : welcome
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Transfer Page</title>
    </head>
    <body>
        <c:set var="user" value="${sessionScope.USER}"/>
        <font color="blue">
        Welcome, ${user.accountID}
        </font>
        <br/><br/>
        
        <h2>Transfer Page</h2>
        <c:set var="balance" value="${user.balance}"/> 
        <table border="0">
            <thead>
                <tr>
                    <td colspan="3">Your balance is ${balance} VND.</td>
                </tr>
            </thead>
            <form action="transfer" method="POST">
                <tbody>
                    <tr>
                        <td colspan="2">Transfer Amount (1000 VND): </td>
                        <td><input type="text" name="txtTransAmount" value="${requestScope.txtTransAmount}" /></td>
                    </tr>
                    <tr>
                        <td>Account</td>
                        <td><input type="text" name="txtAccountID" value="${requestScope.txtAccountID}" /></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="Transfer" /></td>
                        <td></td>
                    </tr>
                </tbody>
            </form>

        </table>
        <br/><br/><a href="logout">Log out</a>
    </body>
</html>

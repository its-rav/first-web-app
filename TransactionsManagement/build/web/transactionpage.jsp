<%-- 
    Document   : transactionpage
    Created on : Jul 8, 2019, 10:21:19 PM
    Author     : welcome
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Transaction Page</title>
    </head>
    <body>
        <c:set var="user" value="${sessionScope.USER}"/>
        <font color="blue">
        Welcome, ${user.accountID}
        </font>
        <br/><br/>
        
        <h1>Transaction Page</h1>
        <form action="transaction" method="POST">
            Please choose transaction type below:
            <br />
            <input type="radio" name="rdTransType" value="Print Transaction" checked="checked"/>Print Transaction
            <br />
            <input type="radio" name="rdTransType" value="Transfer" />Transfer
            <br />
            <input type="submit" value="Execute" />
        </form>
        <br/><br/><a href="logout">Log out</a>
    </body>
</html>

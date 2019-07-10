<%-- 
    Document   : transfererrorpage
    Created on : Jul 6, 2019, 11:15:56 PM
    Author     : welcome
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
    </head>
    <body>
        <c:set var="user" value="${sessionScope.USER}"/>
        <font color="blue">
        Welcome, ${user.accountID}
        </font>
        <br/><br/>

        <h1>Error Page</h1>
        <c:set var="errors" value="${sessionScope.TRANSFER_ERRORS}"/>
        <text>Errors occur due to :</text>
        <c:if test="${not empty errors.accountError}">
            <br/>
            <font color="red">
            ${errors.accountError}
            </font>
        </c:if>
        <c:if test="${not empty errors.amountError}">
            <br/>
            <font color="red">
            ${errors.amountError}
            </font>
        </c:if>
        <c:if test="${not empty errors.emptyAccountError}">
            <br/>
            <font color="red">
            ${errors.emptyAccountError}
            </font>
        </c:if>
        <c:if test="${not empty errors.emptyAmountError}">
            <br/>
            <font color="red">
            ${errors.emptyAmountError}
            </font>
        </c:if>
        <c:if test="${not empty errors.serverError}">
            <br/>
            <font color="red">
            ${errors.serverError}
            </font>
        </c:if>
        <br/>
        <text>Continue to other transaction?</text>
        <br/>
        <form action="continueOrNot" method="POST">
            <input type="submit" value="Yes" name="btAction" />
            <input type="submit" value="No" name="btAction" />
        </form>
    </body>
</html>

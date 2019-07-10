<%-- 
    Document   : printtransaction
    Created on : Jul 6, 2019, 10:54:09 PM
    Author     : welcome
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Print Transaction</title>
    </head>
    <body>
        <c:set var="user" value="${sessionScope.USER}"/>
        <c:set var="balance" value="${user.balance}"/> 
        <font color="blue">
        Welcome, ${user.accountID}
        </font>
        <br/><br/>

        <form action="printTransaction" method="GET">
            <table>
                <tr>
                    <td colspan="2" align="left"><b>Print Transaction</b></td>
                    <td colspan="2" align="right">Your balance: </td>
                    <td colspan="2">${balance}</td>
                </tr>
                <tr>
                    <td>From Date</td>
                    <td><input type="date" name="txtFromDate" value="${param.txtFromDate}" /></td>
                    <td>To Date: </td>
                    <td ><input type="date" name="txtToDate" value="${param.txtToDate}"/></td>
                    <td colspan="2"><input type="submit" value="List" style="width: 100%" /></td>
                </tr>

            </table>
        </form>
        <c:set var="errors" value="${requestScope.PRINT_TRANS_ERRORS}"/>
        <c:if test="${not empty errors.emptyDateError}">
            <br/>
            <font color="red">
            ${errors.emptyDateError}
            </font>
            <br/>
        </c:if>
        <c:if test="${not empty errors.dateFormatError}">
            <br/>
            <font color="red">
            ${errors.dateFormatError}
            </font>
            <br/>
        </c:if>
        <c:if test="${not empty errors.serverError}">
            <br/>
            <font color="red">
            ${errors.serverError}
            </font>
            <br/>
        </c:if>

        <c:set var="result" value="${requestScope.LIST_TRANSACTIONS}"/>
        <c:if test="${not empty result}">
            Result:
            <hr>
            <hr>

            <table border="1">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Reason</th>
                        <th>Hide</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${result}" varStatus="counter" >
                    <form action="hide" method="POST">
                        <tr>
                            <td>
                                ${counter.count}
                                <input type="hidden" name="txtTransID" value="${item.transID}" />
                                <input type="hidden" name="txtFromDate" value="${param.txtFromDate}"/>
                                <input type="hidden" name="txtToDate" value="${param.txtToDate}"/>
                            </td>
                            <td>${item.date}</td>
                            <td>${item.type}</td>
                            <td>${item.amount}</td>
                            <td>${item.reason}</td>
                            <td><input type="submit" value="Hide" /></td>
                        </tr>
                    </form>

                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <br/><br/><a href="logout">Log out</a>
</body>
</html>

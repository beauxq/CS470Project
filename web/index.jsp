<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="postQuery" dataSource="jdbc/blogData">
    SELECT pID, pTitle FROM posts
</sql:query>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>

    <body>
        <h1>
            a blog website
        </h1>

        <form action="response.jsp">
            Select a post:
            <select name="title_id" size="4">
                <c:forEach var="row" items="${postQuery.rows}">
                    <option value="${row.pID}">${row.pTitle}</option>
                </c:forEach>
            </select>
            <input type="submit" value="submit" name="submit" />
        </form>

    </body>

</html>

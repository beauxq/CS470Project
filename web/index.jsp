<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="peopleQuery" dataSource="jdbc/blogData">
    SELECT ID, name FROM people
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
            a blog
        </h1>

        <form action="response.jsp">
            Select a name:
            <select name="name_id">
                <c:forEach var="row" items="${peopleQuery.rows}">
                    <option value="${row.ID}">${row.name}</option>
                </c:forEach>
            </select>
            <input type="submit" value="submit" name="submit" />
        </form>

    </body>

</html>

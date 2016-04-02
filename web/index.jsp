<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>


<sql:query var="postQuery" dataSource="jdbc/blogData">
    SELECT pID, pTitle, aName, pDate FROM
    (posts JOIN authors
    ON posts.aID = authors.aID)
    ORDER BY pDate DESC
    LIMIT 20
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
            Home
        </h1>
        
        <h2>
            Latest posts:<br />
        </h2>
        <c:forEach var="row" items="${postQuery.rows}">
            <a href="view_post.jsp?pID=${row.pID}">${row.pTitle}</a> 
            <br>By ${row.aName} on ${row.pDate}
            <br /><br />
        </c:forEach>

    </body>

</html>

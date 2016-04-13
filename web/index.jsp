<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<!-- recent posts query -->
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
        <h1>Home</h1>
        <a href="new_post.jsp">New Post</a><br /><br />
        <a href="search.jsp">Search Posts</a><br /><br />
    </head>

    <body>
        <c:forEach var="row" items="${postQuery.rows}">
            <a href="view_post.jsp?pID=${row.pID}">${row.pTitle}</a> 
            <br>By <a href="posts_by_author.jsp?aName=${row.aName}">${row.aName}</a> on ${row.pDate}
            <br /><br />
        </c:forEach>
    </body>
</html>

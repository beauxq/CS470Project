<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="postQuery" dataSource="jdbc/blogData">
    SELECT pId, pTitle, pDate, aName FROM (
    posts JOIN (
    SELECT aName, aId FROM authors
    WHERE aName = ? <sql:param value="${param.aName}"/>) AS inputAuthor
    ON posts.aID = inputAuthor.aID)
    ORDER BY pDate DESC
    LIMIT 20
</sql:query>

 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Posts By ${param.aName}</title>
        <h1>${param.aName}'s latest posts</h1>
        <a href="index.jsp">Home</a><br><br>
    </head>
    <body>
        <c:forEach var="row" items="${postQuery.rows}">
            <a href="view_post.jsp?pID=${row.pID}">${row.pTitle}</a> 
            <br>By <a href="posts_by_author.jsp?aName=${row.aName}">${row.aName}</a> on ${row.pDate}
            <br /><br />
        </c:forEach>
    </body>
</html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="postQuery" dataSource="jdbc/blogData">
    SELECT * FROM posts JOIN authors
    ON posts.aID = authors.aID
    WHERE posts.pID = ? <sql:param value="${param.pID}"/>
</sql:query>
    
<sql:query var="commentQuery" dataSource="jdbc/blogData">
    SELECT cID, cText, cDate, aName FROM (
        authors JOIN (
            SELECT * FROM comments
            WHERE comments.pID = ? <sql:param value="${param.pID}"/>
        ) AS postcomments
        ON authors.aID = postcomments.aID
    )
</sql:query>

<c:set var="details" value="${postQuery.rows[0]}"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${details.pTitle}</title>
    </head>

    <body>
        Title: ${details.pTitle}<br />
        Author: ${details.aName}<br />
        Date: ${details.pDate}<br />
        <br />${details.pText}<br />
        
        <br />Comments:<br />
        <c:forEach var="row" items="${commentQuery.rows}">
            <br />
            ${row.cText}
            <br />
            by ${row.aName} on ${row.cDate}
            <br />
        </c:forEach>
    </body>

</html>

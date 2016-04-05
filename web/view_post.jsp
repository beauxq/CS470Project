<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="postQuery" dataSource="jdbc/blogData">
    SELECT * FROM posts JOIN authors
    ON posts.aID = authors.aID
    WHERE posts.pID = ? <sql:param value="${param.pID}"/>
</sql:query>
    
<sql:query var="commentsQuery" dataSource="jdbc/blogData">
    SELECT cID, cText, cDate, aName FROM (
        authors JOIN (
            SELECT * FROM comments
            WHERE comments.pID = ? <sql:param value="${param.pID}"/>
        ) AS postcomments
        ON authors.aID = postcomments.aID
    )
    ORDER BY cDate ASC
</sql:query>

<sql:query var="commentCountQuery" dataSource="jdbc/blogData">
    SELECT COUNT(cID) AS numComments FROM
        authors JOIN (
            SELECT * FROM comments
            WHERE comments.pID = ? <sql:param value="${param.pID}"/>
        ) AS postcomments
        ON authors.aID = postcomments.aID
</sql:query>
        
<sql:query var="tagsQuery" dataSource="jdbc/blogData">
    SELECT tText FROM posttags JOIN tags
    ON posttags.tID = tags.tID
    WHERE posttags.pID = ? <sql:param value="${param.pID}"/>
</sql:query>

<c:set var="postDetails" value="${postQuery.rows[0]}"/>
<c:set var="comments" value="${commentsQuery.rows}"/>
<c:set var="commentsCount" value="${commentCountQuery.rows[0]}"/>
<c:set var="tags" value="${tagsQuery.rows}"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${postDetails.pTitle}</title>
    </head>

    <body>
        <a href="index.jsp">Home</a><br><br>
        Title: ${postDetails.pTitle}<br />
        Author: <a href="index.jsp?aName=${postDetails.aName}">${postDetails.aName}</a><br />
        Date: ${postDetails.pDate}<br />
        <br />${postDetails.pText}<br />
        <br />
        
        Tags:<c:forEach var="row" items="${tags}"> ${row.tText}</c:forEach><br />
        <br />
        
        ${commentsCount.numComments} comments on ${postDetails.pTitle}<br />
        <c:forEach var="row" items="${comments}">
            <br />
            ${row.cText}
            <br />
            By ${row.aName} on ${row.cDate}
            <br />
        </c:forEach><br />
            
        <form method="get" action="add_comment">
            <input type="hidden" name="pID" value="${param.pID}">
            Your comment:<br>
            <input type="text" name="cText" required><br>
            Your name:<br>
            <input type="text" name="aName" required>
            <input type="submit" value="Add Comment">
        </form>
                
    </body>

</html>

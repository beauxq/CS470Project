<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<c:choose>
    <c:when test="${not empty param.title}">
        <c:choose>
            <c:when test="${not empty param.content}">
                <c:choose>
                    <c:when test="${not empty param.tags}">
                        <c:choose>
                            <c:when test="${not empty param.author}">
                                <!-- title content tags author query -->
                                <sql:query var="postQuery" dataSource="jdbc/blogData">
                                    SELECT DISTINCT posts.pID, pTitle, aName, pDate FROM
                                    (posts 
                                    JOIN authors ON posts.aID = authors.aID
                                    JOIN posttags ON posts.pID = postTags.pID
                                    JOIN tags ON postTags.tID = tags.tID)
                                    WHERE pTitle LIKE '%${param.search}%'
                                    OR pText LIKE '%${param.search}%'
                                    OR tText LIKE '%${param.search}%'   
                                    OR aName LIKE '%${param.search}%'
                                    ORDER BY pDate DESC
                                    LIMIT 20
                                </sql:query>
                            </c:when>
                            <c:otherwise>
                                <!-- title content tags query -->
                                <sql:query var="postQuery" dataSource="jdbc/blogData">
                                    SELECT DISTINCT posts.pID, pTitle, aName, pDate FROM
                                    (posts 
                                    JOIN authors ON posts.aID = authors.aID
                                    JOIN posttags ON posts.pID = postTags.pID
                                    JOIN tags ON postTags.tID = tags.tID)
                                    WHERE pTitle LIKE '%${param.search}%'
                                    OR pText LIKE '%${param.search}%'
                                    OR tText LIKE '%${param.search}%'
                                    ORDER BY pDate DESC
                                    LIMIT 20
                                </sql:query>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <!-- title content query  -->
                        <sql:query var="postQuery" dataSource="jdbc/blogData">
                            SELECT DISTINCT pID, pTitle, aName, pDate FROM
                            (posts JOIN authors
                            ON posts.aID = authors.aID)
                            WHERE pTitle LIKE '%${param.search}%'
                            OR pText LIKE '%${param.search}%'
                            ORDER BY pDate DESC
                            LIMIT 20
                        </sql:query>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <!-- title query -->
                <sql:query var="postQuery" dataSource="jdbc/blogData">
                    SELECT DISTINCT pID, pTitle, aName, pDate FROM
                    (posts JOIN authors
                    ON posts.aID = authors.aID)
                    WHERE pTitle LIKE '%${param.search}%'
                    ORDER BY pDate DESC
                    LIMIT 20
                </sql:query>
            </c:otherwise>
        </c:choose>
    </c:when>
</c:choose>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Results</title>
    <h1>Search Results for <i>${param.search}</i></h1>
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

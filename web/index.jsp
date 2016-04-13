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
    <c:otherwise>
        <sql:query var="postQuery" dataSource="jdbc/blogData">
            SELECT pID, pTitle, aName, pDate FROM
            (posts JOIN authors
            ON posts.aID = authors.aID)
            ORDER BY pDate DESC
            LIMIT 20
        </sql:query>
    </c:otherwise>
</c:choose>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <h1>Home</h1>
        <a href="new_post.jsp">New Post</a><br /><br />
    </head>

    <body>
            <form action="index.jsp">
            <input type="text" name="search">
            <input type="checkbox" name="title" id="title" onclick="titleClick()">Title  
            <input type="checkbox" name="content" id="content" onclick="contentClick()" disabled>Content  
            <input type="checkbox" name="tags" id="tags" onclick="tagsClick()" disabled>Tags  
            <input type="checkbox" name="author" id="author" disabled>Authors
            <input type="submit" value="Search">
        </form>

        <br/><br/>
        
        <c:forEach var="row" items="${postQuery.rows}">
            <a href="view_post.jsp?pID=${row.pID}">${row.pTitle}</a> 
            <br>By <a href="posts_by_author.jsp?aName=${row.aName}">${row.aName}</a> on ${row.pDate}
            <br /><br />
        </c:forEach>
            
        <script src="index.js"></script>
    </body>

</html>

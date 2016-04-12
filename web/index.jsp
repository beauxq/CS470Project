<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>


<c:choose>
    <c:when test="${not empty param.aName}">
        <sql:query var="postQuery" dataSource="jdbc/blogData">
            SELECT pId, pTitle, pDate, aName FROM (
            posts JOIN (
            SELECT aName, aId FROM authors
            WHERE aName = ? <sql:param value="${param.aName}"/>) AS inputAuthor
            ON posts.aID = inputAuthor.aID)
            ORDER BY pDate DESC
            LIMIT 20
        </sql:query>
    </c:when>
    <c:when test="${not empty param.title}">
        <c:choose>
            <c:when test="${not empty param.content}">
                <c:choose>
                    <c:when test="${not empty param.tags}">
                        <c:choose>
                            <c:when test="${not empty param.author}">
                                <!-- TODO: all 4 query here -->
                                <sql:query var="postQuery" dataSource="jdbc/blogData">
                                    SELECT pID, pTitle, aName, pDate FROM
                                    (posts JOIN authors
                                    ON posts.aID = authors.aID)                                  
                                    ORDER BY pDate DESC
                                    LIMIT 20
                                </sql:query>
                            </c:when>
                            <c:otherwise>
                                <!-- TODO: title content tags query here -->
                                <sql:query var="postQuery" dataSource="jdbc/blogData">
                                    SELECT pID, pTitle, aName, pDate FROM
                                    (posts JOIN authors
                                    ON posts.aID = authors.aID)
                                    ORDER BY pDate DESC
                                    LIMIT 20
                                </sql:query>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <!-- TODO: title content query here -->
                        <sql:query var="postQuery" dataSource="jdbc/blogData">
                            SELECT pID, pTitle, aName, pDate FROM
                            (posts JOIN authors
                            ON posts.aID = authors.aID)
                            ORDER BY pDate DESC
                            LIMIT 20
                        </sql:query>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <!-- TODO: title query here -->
                <sql:query var="postQuery" dataSource="jdbc/blogData">
                    SELECT pID, pTitle, aName, pDate FROM
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
        <c:choose>
            <c:when test="${not empty param.aName}">
                <title>${param.aName}</title>
            </c:when>
            <c:otherwise>
                <title>Home</title>
            </c:otherwise>
        </c:choose>
    </head>

    <body>
        <c:choose>
            <c:when test="${not empty param.aName}">
                <a href="index.jsp">Home</a><br><br>
                <h1>${param.aName}'s latest posts</h1>
            </c:when>
            <c:otherwise>
                <h1>Home</h1>
            </c:otherwise>
        </c:choose>
        
        <a href="new_post.jsp">New Post</a>
        <br /><br />
        <form action="index.jsp">
            <input type="text" name="search">
            <input type="checkbox" name="title" id="title" onclick="titleClick()">Title  
            <input type="checkbox" name="content" id="content" onclick="contentClick()" disabled>Content  
            <input type="checkbox" name="tags" id="tags" onclick="tagsClick()" disabled>Tags  
            <input type="checkbox" name="author" id="author" disabled>Authors
            <input type="submit" value="Search">
        </form>
        <!--
        <form action="index.jsp">
            <input type="text" name="searchTitleContent">
            <input type="submit" value="Search Titles and Content">
        </form>
        <form action="index.jsp">
            <input type="text" name="searchTitleTagsContent">
            <input type="submit" value="Search Titles, Tags, and Content">
        </form>
        <form action="index.jsp">
            <input type="text" name="searchTitleTagsAuthorContent">
            <input type="submit" value="Search Titles, Tags, Authors, and Content">
        </form>
        -->
        <br/><br/>
        
        <c:forEach var="row" items="${postQuery.rows}">
            <a href="view_post.jsp?pID=${row.pID}">${row.pTitle}</a> 
            <br>By <a href="index.jsp?aName=${row.aName}">${row.aName}</a> on ${row.pDate}
            <br /><br />
        </c:forEach>
            
        <script src="index.js"></script>
    </body>

</html>

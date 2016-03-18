<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="authorQuery" dataSource="jdbc/blogData">
    SELECT * FROM posts JOIN authors
    ON posts.aID = authors.aID
    WHERE posts.pID = ? <sql:param value="${param.title_id}"/>
</sql:query>

<c:set var="details" value="${authorQuery.rows[0]}"/>

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
        <br />
        ${details.pText}
    </body>

</html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:query var="colorQuery" dataSource="jdbc/blogData">
    SELECT * FROM people, color
    WHERE people.FavoriteColorID = color.ID
    AND people.ID = ? <sql:param value="${param.name_id}"/>
</sql:query>

<c:set var="colorDetails" value="${colorQuery.rows[0]}"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${colorDetails.Name}</title>
    </head>

    <body>
        ${colorDetails.Name}'s Favorite Color: ${colorDetails.Color}
    </body>

</html>

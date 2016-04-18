<%String results = request.getParameter("results"); %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Testing</title>
        <h1>Performance Testing</h1>
        <a href="index.jsp">Home</a><br><br>
    </head>
    <body>
        <form method="get" action="performance_test">
            <input type="radio" name="db" value="MySQL")> MySQL
            <input type="radio" name="db" value="MongoDB"> MongoDB<br>
            <input type="submit" value="Run"><br><br>
            <%if (results != null){%> <%=results%> <%}%>
        </form>
    </body>
</html>

<%String results = request.getParameter("results"); %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Style/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Testing</title>
        <h1>Performance Testing</h1>
        <br>
        <a href="index.jsp" class="button">Home</a>
        <a href="search.jsp" class="button">Search Posts</a>
        <a href="recent_posts.jsp" class="button">Recent Posts</a>
        <a href="new_post.jsp" class="button">New Post</a>
        <a href="performance.jsp" class="button">Performance Testing</a>
    </head>
    <body class="bgc">
        <div class="info">
            <form method="get" action="performance_test">
                <input type="radio" name="db" value="MySQL")> MySQL
                <input type="radio" name="db" value="MongoDB"> MongoDB
                <input type="submit" value="Run" class="button_small">
            </form>
        </div>
        <%if (results != null){%>
            <div class="post">
                <%=results%> 
            </div>
        <%}%>
    </body>
</html>

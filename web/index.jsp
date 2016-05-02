<%@page import="DAL.DAL"%>

<% String usingSQL = (DAL.UsingSQLDatabase() ? "checked" : ""); %>
<% String usingMongo = (DAL.UsingSQLDatabase() ? "" : "checked"); %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Style/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <h1>Home</h1>
        <div class="button_container">
            <a href="index.jsp" class="button">Home</a>
            <a href="search.jsp" class="button">Search Posts</a>
            <a href="recent_posts.jsp" class="button">Recent Posts</a>
            <a href="new_post.jsp" class="button">New Post</a>
            <a href="performance.jsp" class="button">Performance Testing</a>
        </div>
    </head>

    <body class="bgc">
        <div class="info">
            Change database:<br>
            <form method="get" action="choose_dal">
                <input type="radio" name="db" value="MySQL" <%=usingSQL%> > MySQL
                <input type="radio" name="db" value="MongoDB" <%=usingMongo%> > MongoDB
                <input type="submit" value="Update" class="button_small">
            </form>
        </div>
    </body>
</html>

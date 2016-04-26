<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Style/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New Post</title>
        <h1 class="h1">New Post</h1>
        <br>
        <a href="index.jsp" class="button">Home</a>
        <a href="search.jsp" class="button">Search Posts</a>
        <a href="recent_posts.jsp" class="button">Recent Posts</a>
        <a href="new_post.jsp" class="button">New Post</a>
        <a href="performance.jsp" class="button">Performance Testing</a>
    </head>
    <body class="bgc">
        <div class="info">
            <form method="get" action="add_post">
                Your name:<br>
                <input type="text" name="aName" required><br>
                Post title:<br>
                <input type="text" name="pTitle" required><br>
                Post content:<br>
                <textarea name="pText" rows="10" cols="109" required></textarea><br>
                Tags:<br>
                <input type="text" name="tags" required><br>
                <input type="submit" value="Add Post" class="button_small">
            </form>
        </div>
    </body>
</html>

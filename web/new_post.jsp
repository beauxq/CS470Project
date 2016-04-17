<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style.css" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New Post</title>
        <h1 class="heading">New Post</h1>
        <a href="index.jsp">Home</a><br><br>
    </head>
    <body class="bgc">
        <form method="get" action="add_post">
            Your name:<br>
            <input type="text" name="aName" required><br>
            Post title:<br>
            <input type="text" name="pTitle" required><br>
            Post content:<br>
            <textarea name="pText" rows="10" cols="30" required></textarea><br>
            Tags:<br>
            <input type="text" name="tags" required><br>
            <input type="submit" value="Add Post">
        </form>
    </body>
</html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Posts</title>
        <h1>Search Posts</h1>
        <a href="index.jsp">Home</a><br><br>
    </head>

    <body>
        <form action="search_results.jsp">
            <input type="text" name="search">
            <input type="checkbox" name="title" id="title" onclick="titleClick()">Title  
            <input type="checkbox" name="content" id="content" onclick="contentClick()" disabled>Content  
            <input type="checkbox" name="tags" id="tags" onclick="tagsClick()" disabled>Tags  
            <input type="checkbox" name="author" id="author" disabled>Authors
            <input type="submit" id="searchbutton" value="Search" disabled>
        </form>
        <br/><br/> 
        <script src="index.js"></script>
    </body>
</html>

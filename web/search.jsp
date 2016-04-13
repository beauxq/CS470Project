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
    </body>
    
    <script>
        var title = document.getElementById("title");
        var content = document.getElementById("content");
        var tags = document.getElementById("tags");
        var author = document.getElementById("author");
        var searchbutton = document.getElementById("searchbutton");

        function titleClick()
        {
            if (! title.checked)
            {
                content.checked = false;
                contentClick();
            }
            content.disabled = !(title.checked);
            searchbutton.disabled = content.disabled;
        }

        function contentClick()
        {
            if (! content.checked)
            {
                tags.checked = false;
                tagsClick();
            }
            tags.disabled = !(content.checked);
        }
        
        function tagsClick()
        {
            if (! tags.checked)
            {
                author.checked = false;
            }
            author.disabled = !(tags.checked);
        }
    </script>
</html>

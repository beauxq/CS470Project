<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style.css" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Posts</title>
        <h1 class="heading">Search Posts</h1>
        <a href="index.jsp" class="button1">Home</a><br><br>
    </head>

    <body class="bgc">
        <form action="search_results.jsp">
            <input type="text" name="search">
            <input type="checkbox" name="title" id="title" onclick="titleClick()">Title  
            <input type="checkbox" name="tags" id="tags" onclick="tagsClick()" disabled>Tags  
            <input type="checkbox" name="content" id="content" onclick="contentClick()" disabled>Content  
            <input type="checkbox" name="author" id="author" disabled>Authors
            <input type="submit" id="searchbutton" value="Search" disabled class="button2">
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
                tags.checked = false;
                tagsClick();
            }
            tags.disabled = !(title.checked);
            searchbutton.disabled = tags.disabled;
        }


        
        function tagsClick()
        {
            if (! tags.checked)
            {
                content.checked = false;
                contentClick();
            }
            content.disabled = !(tags.checked);
        }
        
        function contentClick()
        {
            if (! content.checked)
            {
                author.checked = false;
            }
            author.disabled = !(content.checked);
        }
    </script>
</html>

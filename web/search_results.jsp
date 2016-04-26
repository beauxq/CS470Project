<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="DataObjects.Post"%>
<%@page import="DAL.DAL"%>

<% DAL dal = DAL.GetDAL(); %>
<% String title = request.getParameter("title") ; %>
<% String tags = request.getParameter("tags") ; %>
<% String content = request.getParameter("content") ; %>
<% String author = request.getParameter("author") ; %>
<% String searchTerm = request.getParameter("search") ; %>
<% List<Post> posts = dal.Search(title, tags, content, author, searchTerm); %>
<% String numResults = posts.size() + (posts.size() == 1 ? " result" : " results");%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Style/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Results</title>
        <h1 class="h1">Search Results for <i><%=searchTerm%></i></h1>
        <br>
        <a href="index.jsp" class="button">Home</a>
        <a href="search.jsp" class="button">Search Posts</a>
        <a href="recent_posts.jsp" class="button">Recent Posts</a>
        <a href="new_post.jsp" class="button">New Post</a>
        <a href="performance.jsp" class="button">Performance Testing</a>
    </head>
    <body class="bgc">
        <div class="info">
            <%=numResults%> for <i><%=searchTerm%></i>
        </div>
        <% for (Post p : posts)
        {
            String pLink = "view_post.jsp?pID=" + p.pID;
            String aLink = "posts_by_author.jsp?aName=" + p.aName; 
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(p.pDate);
            String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(date);%>
            <div class="post">
                <a href="<%=pLink%>"><%=p.pTitle%></a><br>
                By <a href="<%=aLink%>"><%=p.aName%></a> on <%=formattedDate%>
            </div>
        <%}%>
    </body>
</html>

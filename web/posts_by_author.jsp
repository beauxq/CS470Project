<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="DataObjects.Post"%>
<%@page import="DAL.DAL"%>

<% DAL dal = DAL.GetDAL(); %>
<% String aName = request.getParameter("aName") ; %>
<% List<Post> posts = dal.GetPostsByAuthor(aName); %>
<% String numPosts = posts.size() + (posts.size() == 1 ? " post" : " posts");%>

 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Style/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Posts By <%=aName%></title>
        <h1><%=aName%>'s latest posts</h1>
        <br>
        <a href="index.jsp" class="button">Home</a>
        <a href="search.jsp" class="button">Search Posts</a>
        <a href="recent_posts.jsp" class="button">Recent Posts</a>
        <a href="new_post.jsp" class="button">New Post</a>
        <a href="performance.jsp" class="button">Performance Testing</a>
    </head>
    <body class="bgc">
        <br>
        <i><%=numPosts%> by <%=aName%></i><br /><br />
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

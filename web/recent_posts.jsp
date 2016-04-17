<%@page import="java.util.List"%>
<%@page import="DataObjects.Post"%>
<%@page import="DAL.DAL"%>

<% DAL dal = DAL.GetDAL(); %>
<% List<Post> posts = dal.GetRecentPosts(); %>
    
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style.css" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Recent Posts</title>
        <h1 class="heading">Recent Posts</h1>
        <a href="index.jsp" class="button1">Home</a><br><br>
    </head>

    <body class="bgc">
        <% for (Post p : posts)
        {
            String pLink = "view_post.jsp?pID=" + p.pID;
            String aLink = "posts_by_author.jsp?aName=" + p.aName; %>       
            <a href="<%=pLink%>"><%=p.pTitle%></a><br>
            By <a href="<%=aLink%>"><%=p.aName%></a> on <%=p.pDate%><br><br>

        <%}%>
    </body>
</html>

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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Posts By <%=aName%></title>
        <h1><%=aName%>'s latest posts</h1>
        <a href="index.jsp">Home</a><br><br>
    </head>
    <body>
        <i><%=numPosts%> by <%=aName%></i><br /><br />
        <% for (Post p : posts)
        {
            String pLink = "view_post.jsp?pID=" + p.pID;
            String aLink = "posts_by_author.jsp?aName=" + p.aName; %>
            <a href="<%=pLink%>"><%=p.pTitle%></a><br>
            By <a href="<%=aLink%>"><%=p.aName%></a> on <%=p.pDate%><br><br>
        <%}%>
    </body>
</html>

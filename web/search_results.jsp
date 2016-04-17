<%@page import="java.util.List"%>
<%@page import="DataObjects.Post"%>
<%@page import="DAL.DAL"%>

<% DAL dal = DAL.GetDAL(); %>
<% String title = request.getParameter("title") ; %>
<% String content = request.getParameter("content") ; %>
<% String tags = request.getParameter("tags") ; %>
<% String author = request.getParameter("author") ; %>
<% String searchTerm = request.getParameter("search") ; %>
<% List<Post> posts = dal.Search(title, content, tags, author, searchTerm); %>
<% String numResults = posts.size() + (posts.size() == 1 ? " result" : " results");%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Results</title>
        <h1>Search Results for <i><%=searchTerm%></i></h1>
        <a href="index.jsp">Home</a><br><br>
    </head>
    <body class="bgc">
        <i><%=numResults%> for <%=searchTerm%></i><br /><br />
        <% for (Post p : posts)
        {
            String pLink = "view_post.jsp?pID=" + p.pID;
            String aLink = "posts_by_author.jsp?aName=" + p.aName; %>
        
            <a href="<%=pLink%>"><%=p.pTitle%></a><br>
            By <a href="<%=aLink%>"><%=p.aName%></a> on <%=p.pDate%><br><br>

        <%}%>
    </body>
</html>

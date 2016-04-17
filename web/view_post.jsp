<%@page import="DataObjects.Comment"%>
<%@page import="java.util.List"%>
<%@page import="DataObjects.Post"%>
<%@page import="DAL.DAL"%>

<% DAL dal = DAL.GetDAL(); %>
<% String pID = request.getParameter("pID") ; %>
<% Post post = dal.GetPost(pID); %>
<% List<Comment> comments = dal.GetComments(pID); %>
<% String numComments = comments.size() + (comments.size() == 1 ? " comment" : " comments");%>
    
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style.css" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=post.pTitle%></title>
        <h1 class="heading"><%=post.pTitle%></h1>
        <a href="index.jsp">Home</a><br><br>
    </head>
    <body class="bgc">        
        By: <a href="posts_by_author.jsp?aName=<%=post.aName%>"><%=post.aName%></a><br />
        On: <%=post.pDate%><br /><br />
        <%=post.pText%><br /><br />
        
        Tags: <%for (String tag : post.tags){%><%=tag%> <%}%><br /><br />

        <i><%=numComments%> on <%=post.pTitle%></i><br />
        <%for (Comment c : comments){%>
            <br /><%=c.cText%><br />
            By <%=c.aName%> on <%=c.cDate%><br />
        <%}%><br/>
            
        <form method="get" action="add_comment">
            <input type="hidden" name="pID" value="<%=post.pID%>">
            Your comment:<br>
            <input type="text" name="cText" required><br>
            Your name:<br>
            <input type="text" name="aName" required><br>
            <input type="submit" value="Add Comment">
        </form>       
    </body>
</html>

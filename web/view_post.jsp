<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
        <link rel="stylesheet" type="text/css" href="Style/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=post.pTitle%></title>
        <h1><%=post.pTitle%></h1>
        <br>
        <a href="index.jsp" class="button">Home</a>
        <a href="search.jsp" class="button">Search Posts</a>
        <a href="recent_posts.jsp" class="button">Recent Posts</a>
        <a href="new_post.jsp" class="button">New Post</a>
        <a href="performance.jsp" class="button">Performance Testing</a>
    </head>
    <body class="bgc">    
        <div class="post">
            <%Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(post.pDate);
            String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(date);%>
            By: <a href="posts_by_author.jsp?aName=<%=post.aName%>"><%=post.aName%></a><br />
            On: <%=formattedDate%><br /><br />
            <%=post.pText%><br /><br />

            Tags: <%for (String tag : post.tags){%><%=tag%> <%}%><br /><br />
        </div>
        <%=numComments%> on <i><%=post.pTitle%></i><br />
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

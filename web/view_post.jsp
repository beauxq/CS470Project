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
        <div class="button_container">
            <a href="index.jsp" class="button">Home</a>
            <a href="search.jsp" class="button">Search Posts</a>
            <a href="recent_posts.jsp" class="button">Recent Posts</a>
            <a href="new_post.jsp" class="button">New Post</a>
            <a href="performance.jsp" class="button">Performance Testing</a>
        </div>
    </head>
    <body class="bgc">    
        <div class="post">
            <%=post.pText%>
        </div>
        <div class="info">
            <%Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(post.pDate);
            String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(date);%>
            By <a href="posts_by_author.jsp?aName=<%=post.aName%>"><%=post.aName%></a>
            (<i><%=formattedDate%></i>)
        </div>
        <div class="info">
            <b>Tags:</b> <%for (String tag : post.tags){%><%=tag%> <%}%>
        </div>
        <h2>
            <%=numComments%> on <i><%=post.pTitle%></i>
        </h2>
        <%for (Comment c : comments){%>
            <div class="post">
                <%Date cDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(c.cDate);
                String formattedCDate = new SimpleDateFormat("MMMM dd, yyyy").format(cDate);%>
                <b><%=c.aName%>:</b> <i>(<%=formattedCDate%>)</i><br />
                <%=c.cText%>
            </div>
        <%}%>
        <div class="info">
            <form method="get" action="add_comment">
                <input type="hidden" name="pID" value="<%=post.pID%>">
                Add a comment:<br>
                <textarea name="cText" rows="10" cols="109" required></textarea><br><br>
                Your name:<br>
                <input type="text" name="aName" required>
                <input type="submit" value="Add Comment" class="button_small">
            </form>  
        </div>
    </body>
</html>

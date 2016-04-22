package servlets;

import DAL.DAL;
import DataObjects.Post;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddPost extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        DAL dal = DAL.GetDAL();
        Post post = new Post();
        post.pTitle = request.getParameter("pTitle");
        post.pText = request.getParameter("pText");
        post.pDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        post.aName = request.getParameter("aName");
        post.tags = new ArrayList();
        post.tags.addAll(Arrays.asList(request.getParameter("tags").split(" ")));
        dal.AddPost(post);
        response.sendRedirect("recent_posts.jsp");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
}

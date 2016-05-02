package servlets;

import DAL.DAL;
import DataObjects.Comment;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddComment extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException
    {
        DAL dal = DAL.GetDAL();
        Comment comment = new Comment();
        comment.cText = request.getParameter("cText");
        comment.cDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        comment.aName = request.getParameter("aName");
        String pID = request.getParameter("pID");
        dal.AddComment(pID, comment);
        response.sendRedirect("view_post.jsp?pID=" + request.getParameter("pID"));
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
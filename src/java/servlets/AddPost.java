package servlets;

import DAL.DAL;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
        String pTitle = request.getParameter("pTitle");
        String pText = request.getParameter("pText");
        String pDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String aName = request.getParameter("aName");
        String[] tags = request.getParameter("tags").split(" ");

        DAL dal = DAL.GetDAL();
        dal.AddPost(pTitle, pText, pDate, aName, tags);
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

package servlets;

import DAL.DAL;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddComment extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            String pID = request.getParameter("pID");
            String cText = request.getParameter("cText");
            String cDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String aName = request.getParameter("aName");
            
            DAL dal = DAL.GetDAL();
            dal.AddComment(pID, cText, cDate, aName);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.toString());
        }
        response.sendRedirect("view_post.jsp?pID=" + request.getParameter("pID"));
    }

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
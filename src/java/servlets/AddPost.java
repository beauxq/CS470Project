package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddPost extends HttpServlet
{
    static String connectionString = "jdbc:mysql://72.129.239.46:3306/470blog";
    static String userID = "cs470";
    static String password = "cs470lnw";
    static String databaseName = "470blog";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            Statement stmt;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(connectionString, userID, password);
            stmt = con.createStatement();
            stmt.execute("USE " + databaseName);
            
            int pID = Util.nextID(stmt, "pid", "posts");
            String pTitle = request.getParameter("pTitle");
            String pText = request.getParameter("pText");
            String pDate = Util.getCurrentDate();            
            String aName = request.getParameter("aName");
            int aID = Util.getAuthorID(stmt, aName);
            String[] tags = request.getParameter("tags").split(" ");
            
            stmt.execute("INSERT INTO posts VALUES (" + pID + ", '" + pTitle + "', '" +
                    pText + "', '" + pDate + "', " + aID + ")");
            
            for (String tag : tags)
            {
                int tID = Util.getTagID(stmt, tag);
                stmt.execute("INSERT INTO posttags VALUES (" + pID + ", " + tID + ")");
            }
            
            response.sendRedirect("view_post.jsp?pID=" + pID);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}

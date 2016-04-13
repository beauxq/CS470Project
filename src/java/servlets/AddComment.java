package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddComment extends HttpServlet
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

            int cID = Util.nextID(stmt, "cid", "comments");
            String cText = request.getParameter("cText");
            String pID = request.getParameter("pID");
            String cDate = Util.getCurrentDate();
            String aName = request.getParameter("aName");
            int aID = Util.getAuthorID(stmt, aName);
 
            stmt.execute("INSERT INTO comments VALUES (" + cID + ", '" + cText + 
                    "', " + pID + ", '" + cDate + "', " + aID + ")");
            
            stmt.close();
            con.close();
        }
        catch (Exception ex)
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

    @Override
    public String getServletInfo()
    {
        return "Adds comment to database for given post";
    }
}
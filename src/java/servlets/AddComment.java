package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddComment extends HttpServlet
{
    static String connectionString = "jdbc:mysql://72.129.239.46:3306/470blog?zeroDateTimeBehavior=convertToNull";
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

            int cID = nextID(stmt, "cid", "comments");
            String cText = request.getParameter("cText");
            String pID = request.getParameter("pID");
            String cDate = getCurrentDate();
            String aName = request.getParameter("aName");
            int aID = getAuthorID(stmt, aName);
 
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
    
    private static int nextID(Statement stmt, String id, String tablename) throws SQLException
    {
            ResultSet rs;
            String selectMaxID = "select max(" + id + ") from " + tablename;
            rs = stmt.executeQuery(selectMaxID); // get largest id so far

            rs.next();
            return rs.getInt(1) + 1;
    }
    
    private static int getAuthorID(Statement stmt, String aName) throws SQLException
    {
        int aID = -1;
        ResultSet rs = stmt.executeQuery("Select aID from authors where aName = '" + aName + "'");
        if (rs.next()) // author already in db
        {
            aID = rs.getInt(1);
        }
        else // add author to db
        {
            aID = nextID(stmt, "aid", "authors");
            stmt.execute("Insert into authors values (" + aID + ", '" + aName + "', 'password')");
        }
        return aID;
    }
    
    private static String getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date d = new Date();
    	String currentDate = (dateFormat.format(d));
        return currentDate;
    }
}
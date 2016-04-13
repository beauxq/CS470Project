package DAL;

import DataObjects.Post;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection
{
    private static String ipAddress = "72.129.239.46";
    private static String port = "3306";
    private static String databaseName = "470blog";
    private static String userID = "cs470";
    private static String password = "cs470lnw";
    private static String connectionString =
            "jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName;

    private static SQLConnection singleton = null;
    private static Statement stmt;
    private static Connection con;

    private SQLConnection()
    {
        try
        {
            System.out.println("Opening mySQL connection...");

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(connectionString, userID, password);
            stmt = con.createStatement();
            stmt.execute("USE " + databaseName);

            System.out.println("Connected to mySQL");
        }
        catch (Exception ex)
        {
            System.out.println("Failure connecting to mySQL");
            System.out.println(ex.toString());
        }
    }

    public static SQLConnection GetSQLConnection()
    {
        if (singleton == null)
        {
            singleton = new SQLConnection();
        }
        return singleton;
    }

    public void Close()
    {
        try
        {
            System.out.println("Closing mySQL connection...");
            stmt.close();
            con.close();
            System.out.println("Closed mySQL connection");
            singleton = null;
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing mySQL connection");
            System.out.println(ex.toString());
        }
    }
    
    public List<Post> GetRecentPosts() throws SQLException
    {
    	String sqlCmd = 
                "SELECT pID, pTitle, aName, pDate "
                + "FROM (posts "
                + "JOIN authors ON posts.aID = authors.aID) "
                + "ORDER BY pDate DESC "
                + "LIMIT 20";
        return getPostsWithoutContent(sqlCmd);
    }
    
    public List<Post> GetPostsByAuthor(String aName) throws SQLException
    {
        String sqlCmd =
                "SELECT pId, pTitle, pDate, aName FROM "
                + "(posts "
                + "JOIN "
                    + "(SELECT aName, aId FROM authors "
                    + "WHERE aName = '" + aName + "') "
                    + "AS inputAuthor "
                + "ON posts.aID = inputAuthor.aID) "
                + "ORDER BY pDate DESC "
                + "LIMIT 20";
        return getPostsWithoutContent(sqlCmd);
    }
    
    public void AddPost(String pTitle, String pText, String pDate, 
            String aName, String[] tags) throws SQLException
    {
        int pID = nextID("pid", "posts");
        int aID = getAuthorID(aName);
        stmt.execute("INSERT INTO posts VALUES (" + pID + ", '" + pTitle 
                + "', '" + pText + "', '" + pDate + "', " + aID + ")");

        for (String tag : tags)
        {
            int tID = getTagID(tag);
            stmt.execute("INSERT INTO posttags VALUES (" 
                    + pID + ", " + tID + ")");
        }
    }
    
    public void AddComment(String pID, String cText, String cDate, String aName) 
            throws SQLException
    {
            int cID = nextID("cid", "comments");
            int aID = getAuthorID(aName);
 
            stmt.execute("INSERT INTO comments VALUES (" + cID + ", '" + cText + 
                    "', " + pID + ", '" + cDate + "', " + aID + ")");
    }  
    
    private static List<Post> getPostsWithoutContent(String sqlCmd) throws SQLException
    {
        List<Post> posts = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(sqlCmd);
        boolean more = rs.next();
        while (more)
        {
            Post p = new Post();
            p.pID = rs.getString(1);
            p.pTitle = rs.getString(2);
            p.aName = rs.getString(3);
            p.pDate = rs.getString(4);
            posts.add(p);
            more = rs.next();
        }
        return posts;
    }

    private static int nextID(String id, String tablename) throws SQLException
    {
        ResultSet rs;

        // get largest id so far
        String sqlCmd = "select max(" + id + ") from " + tablename;
        rs = stmt.executeQuery(sqlCmd);

        rs.next();
        return rs.getInt(1) + 1;
    }
    
    private static int getAuthorID(String aName) throws SQLException
    {
        int aID = -1;
        ResultSet rs = stmt.executeQuery
                ("Select aID from authors where aName = '" + aName + "'");
        if (rs.next()) // author already in db
        {
            aID = rs.getInt(1);
        }
        else // add author to db
        {
            aID = nextID("aid", "authors");
            stmt.execute("Insert into authors values (" + aID + ", '" 
                    + aName + "', 'password')");
        }
        return aID;
    }
    
    private static int getTagID(String tText) throws SQLException
    {
        int tID = -1;
        ResultSet rs = stmt.executeQuery("Select tID from tags where tText = '" + tText + "'");
        if (rs.next()) // tag already in db
        {
            tID = rs.getInt(1);
        }
        else // add tag to db
        {
            tID = nextID("tid", "tags");
            stmt.execute("Insert into tags values (" + tID + ", '" + tText + "')");
        }
        return tID;
    }
}
package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection implements IConnection
{
    private static String ipAddress = "72.129.239.46";
    private static String port = "3306";
    private static String databaseName = "470blog";
    private static String userID = "cs470";
    private static String password = "cs470lnw";
    private static String connectionString =
            "jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName;

    private static Statement stmt;
    private static Connection con;

    SQLConnection()
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

    public void Close()
    {
        try
        {
            System.out.println("Closing mySQL connection...");
            stmt.close();
            con.close();
            System.out.println("Closed mySQL connection");
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing mySQL connection");
            System.out.println(ex.toString());
        }
    }
    
    public Post GetPost(String pID)
    {
        Post post = new Post();
        try
        {
            String sqlCmd = 
                    "SELECT posts.pID, pTitle, aName, pDate, pText, tText "
                    + "FROM posts "
                    + "JOIN authors ON posts.aID = authors.aID "
                    + "JOIN posttags ON posts.pID = posttags.pID "
                    + "JOIN tags ON tags.tid = posttags.tID "
                    + "WHERE posts.pID = " + pID;
            post = getPost(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return post;
    }
    
    public List<Comment> GetComments(String pID)
    {
        List<Comment> comments = new ArrayList<>();
        try
        {
            String sqlCmd = 
                    "SELECT cID, aName, cDate, cText "
                    + "FROM comments "
                    + "JOIN authors on comments.aID = authors.aID "
                    + "WHERE comments.pID = " + pID + " "
                    + "ORDER BY cDate ASC";
            comments = getCommentList(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return comments;
    }
    
    public List<Post> GetRecentPosts()
    {
        List<Post> posts = new ArrayList();
        try
        {
            String sqlCmd =
                    "SELECT pID, pTitle, aName, pDate "
                    + "FROM (posts "
                    + "JOIN authors ON posts.aID = authors.aID) "
                    + "ORDER BY pDate DESC "
                    + "LIMIT 20";
            posts = getPostList(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        List<Post> posts = new ArrayList();
        try
        {
            String sqlCmd =
                    "SELECT pId, pTitle, aName, pDate FROM "
                    + "(posts "
                    + "JOIN "
                        + "(SELECT aName, aId FROM authors "
                        + "WHERE aName = '" + aName + "') "
                        + "AS inputAuthor "
                    + "ON posts.aID = inputAuthor.aID) "
                    + "ORDER BY pDate DESC "
                    + "LIMIT 20";
            posts = getPostList(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
    }
    
    public List<Post> Search(String byTitle, String byContent, 
            String byTags, String byAuthor, String searchTerm)
    {
        List<Post> posts = new ArrayList();
        String sqlCmd = "";

        if (byAuthor != null)
        {
            sqlCmd = searchByTitleContentTagsAuthor(searchTerm);
        }
        else if (byTags != null)
        {
            sqlCmd = searchByTitleContentTags(searchTerm);
        }
        else if (byContent != null)
        {
            sqlCmd = searchByTitleContent(searchTerm);
        }
        else
        {
            sqlCmd = searchByTitle(searchTerm);
        }
        try
        {
            posts = getPostList(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
    }
    
    public void AddPost(String pTitle, String pText, String pDate, 
            String aName, String[] tags)
    {
        try
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
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public void AddComment(String pID, String cText, String cDate, String aName) 
    {
        try
        {
            int cID = nextID("cid", "comments");
            int aID = getAuthorID(aName);
 
            stmt.execute("INSERT INTO comments VALUES (" + cID + ", '" + cText + 
                    "', " + pID + ", '" + cDate + "', " + aID + ")");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }  
    
    private static Post getPost(String sqlCmd) throws SQLException
    {
        System.out.println("GETPOST");
        ResultSet rs = stmt.executeQuery(sqlCmd);
        boolean more = rs.next();
        Post post = new Post();
        post.pID = rs.getString(1);
        post.pTitle = rs.getString(2);
        post.aName = rs.getString(3);
        post.pDate = rs.getString(4);
        post.pText = rs.getString(5);
        while (more)
        {
            post.tags.add(rs.getString(6));
            more = rs.next();
        }
        System.out.println("LEAVE GETPOST");
        return post;
    }
    
    private static List<Comment> getCommentList(String sqlCmd) throws SQLException
    {
        System.out.println("GETCOMMENTS");
        List<Comment> comments = new ArrayList<>();
        System.out.println("SQL");
        ResultSet rs = stmt.executeQuery(sqlCmd);
        System.out.println("LEAVE SQL");
        boolean more = rs.next();
        while (more)
        {
            System.out.println("iteration c");
            Comment c = new Comment();
            c.cID = rs.getString(1);
            c.aName = rs.getString(2);
            c.cDate = rs.getString(3);
            c.cText = rs.getString(4);
            comments.add(c);
            more = rs.next();
        }
        System.out.println("LEAVE GETCOMMENTS");
        return comments;
    }
    
    private static List<Post> getPostList(String sqlCmd) throws SQLException
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
    
    private static String searchByTitleContentTagsAuthor(String searchTerm)
    {
        return "SELECT DISTINCT posts.pID, pTitle, aName, pDate FROM " +
            "(posts " +
            "JOIN authors ON posts.aID = authors.aID " +
            "JOIN posttags ON posts.pID = postTags.pID " +
            "JOIN tags ON postTags.tID = tags.tID) " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "OR pText LIKE '%" + searchTerm + "%' " +
            "OR tText LIKE '%" + searchTerm + "%' " +
            "OR aName LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC " +
            "LIMIT 20";
    }
    
    private static String searchByTitleContentTags(String searchTerm)
    {
        return "SELECT DISTINCT posts.pID, pTitle, aName, pDate FROM " +
            "(posts " +
            "JOIN authors ON posts.aID = authors.aID " +
            "JOIN posttags ON posts.pID = postTags.pID " +
            "JOIN tags ON postTags.tID = tags.tID) " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "OR pText LIKE '%" + searchTerm + "%' " +
            "OR tText LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC " +
            "LIMIT 20";
    }
    
    private static String searchByTitleContent(String searchTerm)
    {
        return "SELECT DISTINCT pID, pTitle, aName, pDate FROM " +
            "(posts " +
            "JOIN authors ON posts.aID = authors.aID) " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "OR pText LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC " +
            "LIMIT 20";
    }
    
    private static String searchByTitle(String searchTerm)
    {
        return "SELECT DISTINCT pID, pTitle, aName, pDate FROM " +
            "(posts " +
            "JOIN authors ON posts.aID = authors.aID) " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC " +
            "LIMIT 20";
    }
}
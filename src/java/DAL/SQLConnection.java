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
    private static final String ipAddress = "localhost";
    private static final String port = "3306";
    private static final String databaseName = "470blog";
    private static final String userID = "cs470";
    private static final String password = "cs470lnw";
    private static final String connectionString =
            "jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName;
    private static final int maxPosts = 1000000;

    private static Statement stmt;
    private static Connection con;
    
    private static SQLConnection singleton = null;

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
        catch (ClassNotFoundException | SQLException ex)
        {
            System.out.println("Failure connecting to mySQL");
            System.out.println(ex.toString());
        }
    }
    
    public static SQLConnection GetConnection()
    {
        if (singleton == null)
        {
            singleton = new SQLConnection();
        }
        return singleton;
    }

    @Override
    public void Close()
    {
        try
        {
            System.out.println("Closing mySQL connection...");
            stmt.close();
            con.close();
            singleton = null;
            System.out.println("Closed mySQL connection");
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing mySQL connection");
            System.out.println(ex.toString());
        }
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public List<Post> GetRecentPosts(int count)
    {
        List<Post> posts = new ArrayList();
        try
        {
            String sqlCmd =
                    "SELECT pID, pTitle, aName, pDate "
                    + "FROM (posts "
                    + "JOIN authors ON posts.aID = authors.aID) "
                    + "ORDER BY pDate DESC "
                    + "LIMIT " + count;
            posts = getPostList(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
    }
    
    @Override
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
                    + "ORDER BY pDate DESC";
            posts = getPostList(sqlCmd);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
    }
    
    @Override
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm)
    {
        return Search(byTitle, byTags, byContent, byAuthor, searchTerm, maxPosts);
    }
    
    @Override
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm, int numPosts)
    {
        List<Post> posts = new ArrayList();
        String sqlCmd;

        if (byAuthor != null)
        {
            sqlCmd = searchByTitleTagsContentAuthor(searchTerm, numPosts);
        }
        else if (byContent != null)
        {
            sqlCmd = searchByTitleTagsContent(searchTerm, numPosts);
        }
        else if (byTags != null)
        {
            sqlCmd = searchByTitleTags(searchTerm, numPosts);
        }
        else
        {
            sqlCmd = searchByTitle(searchTerm, numPosts);
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
    
    @Override
    public void AddPost(Post post)
    {
        try
        {
            int pID = nextID("pid", "posts");
            int aID = getAuthorID(post.aName);
            stmt.execute("INSERT INTO posts VALUES (" + pID + ", '" + post.pTitle 
                    + "', '" + post.pText + "', '" + post.pDate + "', " + aID + ")");

            for (String tag : post.tags)
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
    
    @Override
    public void AddPosts(List<Post> posts)
    {
        try
        {
            int nextPID = nextID("pid", "posts");
            
            String sqlCmd_posts = "INSERT INTO posts VALUES ";
            String sqlCmd_tags = "INSERT INTO posttags VALUES ";
            
            String commaSep = "";
            for (Post post : posts)
            {
                int aID = getAuthorID(post.aName);    
                sqlCmd_posts += commaSep;

                sqlCmd_posts += "(" + nextPID + ", '" + post.pTitle + "', '" +
                        post.pText + "', '" + post.pDate + "', " + aID + ")";

                for (String tag : post.tags)
                {
                    int tID = getTagID(tag);
                    sqlCmd_tags += commaSep;
                    commaSep = ", ";
                    sqlCmd_tags += "(" + nextPID + ", " + tID + ")";
                }
                commaSep = ", ";                
                nextPID++;
            }
            
            stmt.execute(sqlCmd_posts);
            stmt.execute(sqlCmd_tags);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
       }
    }
    
    @Override
    public void AddComment(String pID, Comment comment) 
    {
        try
        {
            int cID = nextID("cid", "comments");
            int aID = getAuthorID(comment.aName);
 
            stmt.execute("INSERT INTO comments VALUES (" + cID + ", '" + comment.cText + 
                    "', " + pID + ", '" + comment.cDate + "', " + aID + ")");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void EmptyDatabase()
    {
        try
        {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("TRUNCATE TABLE comments");
            stmt.execute("TRUNCATE TABLE posttags");
            stmt.execute("TRUNCATE TABLE posts");
            stmt.execute("TRUNCATE TABLE tags");
            stmt.execute("TRUNCATE TABLE authors");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            stmt.execute("INSERT INTO authors VALUES (1, 'Atreya', 'password')");
            stmt.execute("INSERT INTO authors VALUES (2, 'Doug', 'password')");
            stmt.execute("INSERT INTO authors VALUES (3, 'Eric', 'password')");
            stmt.execute("INSERT INTO authors VALUES (4, 'Sundar', 'password')");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private static Post getPost(String sqlCmd) throws SQLException
    {
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
        return post;
    }
    
    private static List<Comment> getCommentList(String sqlCmd) throws SQLException
    {
        List<Comment> comments = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(sqlCmd);
        while (rs.next())
        {
            Comment c = new Comment();
            c.cID = rs.getString(1);
            c.aName = rs.getString(2);
            c.cDate = rs.getString(3);
            c.cText = rs.getString(4);
            comments.add(c);
        }
        return comments;
    }
    
    private static List<Post> getPostList(String sqlCmd) throws SQLException
    {
        List<Post> posts = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(sqlCmd);
        while (rs.next())
        {
            Post p = new Post();
            p.pID = rs.getString(1);
            p.pTitle = rs.getString(2);
            p.aName = rs.getString(3);
            p.pDate = rs.getString(4);
            posts.add(p);
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
        int aID;
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
        int tID;
        ResultSet rs = stmt.executeQuery(
                "Select tID from tags where tText = '" + tText + "'");
        if (rs.next()) // tag already in db
        {
            tID = rs.getInt(1);
        }
        else // add tag to db
        {
            tID = nextID("tid", "tags");
            stmt.execute(
                    "Insert into tags values (" + tID + ", '" + tText + "')");
        }
        return tID;
    }
    
    private static String searchByTitleTagsContentAuthor(String searchTerm, 
            int numPosts)
    {
        return "SELECT DISTINCT pID, pTitle, aName, pDate FROM " +
                "(SELECT posts.pID, pTitle, aName, pDate, pText, tText FROM " +
                    "(posts " +
                    "JOIN authors ON posts.aID = authors.aID " +
                    "JOIN posttags ON posts.pID = postTags.pID " +
                    "JOIN tags ON postTags.tID = tags.tID) " +
                "LIMIT " + numPosts + ") AS subtable " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "OR pText LIKE '%" + searchTerm + "%' " +
            "OR tText LIKE '%" + searchTerm + "%' " +
            "OR aName LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC";
    }
    
    private static String searchByTitleTagsContent(String searchTerm, 
            int numPosts)
    {
        return "SELECT DISTINCT pID, pTitle, aName, pDate FROM " +
                "(SELECT posts.pID, pTitle, aName, pDate, pText, tText FROM " +
                    "(posts " +
                    "JOIN authors ON posts.aID = authors.aID " +
                    "JOIN posttags ON posts.pID = postTags.pID " +
                    "JOIN tags ON postTags.tID = tags.tID) " +
                "LIMIT " + numPosts + ") AS subtable " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "OR pText LIKE '%" + searchTerm + "%' " +
            "OR tText LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC";
    }
    
    private static String searchByTitleTags(String searchTerm, int numPosts)
    {
        return "SELECT DISTINCT pID, pTitle, aName, pDate FROM " +
                "(SELECT posts.pID, pTitle, aName, pDate, tText FROM " +
                    "(posts " +
                    "JOIN authors ON posts.aID = authors.aID " +
                    "JOIN posttags ON posts.pID = postTags.pID " +
                    "JOIN tags ON postTags.tID = tags.tID) " +
                "LIMIT " + numPosts + ") AS subtable " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "OR tText LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC";
    }
    
    private static String searchByTitle(String searchTerm, int numPosts)
    {
        return "SELECT DISTINCT pID, pTitle, aName, pDate FROM " +
                "(SELECT pID, pTitle, aName, pDate FROM " +
                    "(posts " +
                    "JOIN authors ON posts.aID = authors.aID) " +
                "LIMIT " + numPosts + ") AS subtable " +
            "WHERE pTitle LIKE '%" + searchTerm + "%' " +
            "ORDER BY pDate DESC";
    }
}

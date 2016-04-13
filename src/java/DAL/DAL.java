package DAL;

import DataObjects.Post;
import java.sql.SQLException;
import java.util.List;

public class DAL
{
    private static Boolean usingSQL = true;
    private static Object connection;
    private static DAL singleton = null;

    private DAL()
    {
        if (usingSQL)
        {
            connection = SQLConnection.GetSQLConnection();
        }
        else
        {
            connection = MongoConnection.GetMongoConnection();
        }
    }

    public static DAL GetDAL()
    {
        if (singleton == null)
        {
            singleton = new DAL();
        }
        return singleton;
    }

    public void CloseConnection()
    {
        if (usingSQL)
        {
            ((SQLConnection) connection).Close();
        }
        else
        {
            ((MongoConnection) connection).Close();
        }
    }
    
    public List<Post> GetRecentPosts() throws SQLException
    {
        if (usingSQL)
        {
            return ((SQLConnection) connection).GetRecentPosts();
        }
        else
        {
            return ((MongoConnection) connection).GetRecentPosts();
        }
    }
    
    public List<Post> GetPostsByAuthor(String aName) throws SQLException
    {
        if (usingSQL)
        {
            return ((SQLConnection) connection).GetPostsByAuthor(aName);
        }
        else
        {
            return ((MongoConnection) connection).GetPostsByAuthor(aName);
        }
    }
    
    public void AddPost(String pTitle, String pText, String pDate, 
            String aName, String[] tags) throws SQLException
    {
        if (usingSQL)
        {
            ((SQLConnection) connection).AddPost
                    (pTitle, pText, pDate, aName, tags);
        }
        else
        {
            ((MongoConnection) connection).AddPost
                    (pTitle, pText, pDate, aName, tags);
        }
    }
    
    public void AddComment(String pID, String cText, String cDate, String aName) 
            throws SQLException
    {
        if (usingSQL)
        {
            ((SQLConnection) connection).AddComment(pID, cText, cDate, aName);
        }
        else
        {
            ((MongoConnection) connection).AddComment(pID, cText, cDate, aName);
        }
    }
}


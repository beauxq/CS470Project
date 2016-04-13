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
}


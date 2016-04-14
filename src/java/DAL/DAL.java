package DAL;

import DataObjects.Post;
import java.util.List;

public class DAL
{
    private static Boolean usingSQL = true;
    private static IConnection connection;
    private static DAL singleton = null;

    private DAL()
    {
        if (usingSQL)
        {
            connection = new SQLConnection();
        }
        else
        {
            connection = new MongoConnection();
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
        connection.Close();
    }
    
    public List<Post> GetRecentPosts()
    {
        return connection.GetRecentPosts();
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        return connection.GetPostsByAuthor(aName);
    }
    
    public List<Post> Search(String byTitle, String byContent, 
            String byTags, String byAuthor, String searchTerm)
    {
        return connection.Search(byTitle, byContent, byTags, byAuthor, searchTerm);
    }
    
    public void AddPost(String pTitle, String pText, String pDate, 
            String aName, String[] tags)
    {
        connection.AddPost(pTitle, pText, pDate, aName, tags);
    }
    
    public void AddComment(String pID, String cText, String cDate, String aName) 
    {
        connection.AddComment(pID, cText, cDate, aName);
    }
}

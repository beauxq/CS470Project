package DAL;

import DataObjects.Comment;
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
    
    public static DAL GetDAL(Boolean useSQL)
    {
        if (singleton != null && usingSQL != useSQL)
        {
            connection.Close();
        }
        usingSQL = useSQL;
        return GetDAL();
    }

    public void CloseConnection()
    {
        connection.Close();
    }
    
    public Post GetPost(String pID)
    {
        return connection.GetPost(pID);
    }
    
    public List<Comment> GetComments(String pID)
    {
        return connection.GetComments(pID);
    }
    
    public List<Post> GetRecentPosts()
    {
        return connection.GetRecentPosts();
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        return connection.GetPostsByAuthor(aName);
    }
    
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm)
    {
        return connection.Search(byTitle, byTags, byContent, byAuthor, searchTerm);
    }
    
    public void AddPost(Post post)
    {
        connection.AddPost(post);
    }
    
    public void AddPosts(List<Post> posts)
    {
        connection.AddPosts(posts);
    }
    
    public void AddComment(String pID, Comment comment) 
    {
        connection.AddComment(pID, comment);
    }
    
    public void EmptyDatabase()
    {
        connection.EmptyDatabase();
    }
}

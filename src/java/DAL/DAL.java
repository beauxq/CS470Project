package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import java.util.List;

public class DAL
{
    private static Boolean usingSQL = true;
    private static final int maxPosts = 1000000;
    private static IConnection connection;
    private static DAL singleton = null;
    
    private DAL()
    {
        if (usingSQL)
        {
            connection = SQLConnection.GetConnection();
        }
        else
        {
            connection = MongoConnection.GetConnection();
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
        SetDefaultDatabase(useSQL);
        return GetDAL();
    }
    
    public static Boolean UsingSQLDatabase()
    {
        return usingSQL;
    }
    
    public static void SetDefaultDatabase(Boolean useSQL)
    {
        if (singleton != null && !usingSQL.equals(useSQL))
        {
            connection.Close();
            singleton = null;
        }
        usingSQL = useSQL;  
    }

    public void CloseConnection()
    {
        connection.Close();
        singleton = null;
    }
    
    public Post GetPost(String pID)
    {
        return connection.GetPost(pID);
    }
    
    public List<Comment> GetComments(String pID)
    {
        return connection.GetComments(pID);
    }
    
    public List<Post> GetRecentPosts(int count)
    {
        return connection.GetRecentPosts(count);
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        return connection.GetPostsByAuthor(aName);
    }
    
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm)
    {
        return connection.Search(byTitle, 
                byTags, byContent, byAuthor, searchTerm, maxPosts);
    }
    
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm, int numPosts)
    {
        return connection.Search(byTitle, 
                byTags, byContent, byAuthor, searchTerm, numPosts);
    }
    
    public void AddPost(Post post)
    {
        connection.AddPost(post);
    }
    
    public void AddPosts(List<Post> posts)
    {
        //limit batch size to 1000 to avoid oversized packets
        while (posts.size() > 1000)
        {
            AddPosts(posts.subList(0, 1000));
            posts = posts.subList(1000, posts.size());
        }
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

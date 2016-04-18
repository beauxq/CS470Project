package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;

public class MongoConnection implements IConnection
{
    private static String ipAddress = "136.63.18.225";
    private static String port = "27444";
    private static String userID = "cs470";
    private static String password = "cs470lnw";
    private static String databaseName = "cs470blog";
    private static String URI =
            "mongodb://" + userID +":" + password 
            + "@" + ipAddress + ":" + port + "/" + databaseName;

    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Document> posts;

    MongoConnection()
    {
        try
        {
            System.out.println("Opening MongoDB connection...");

            mongoClient = new MongoClient(new MongoClientURI(URI));
            db = mongoClient.getDatabase(databaseName);
            posts = db.getCollection("posts");

            System.out.println("Connected to MongoDB");
        }
        catch (Exception ex)
        {
            System.out.println("Failure connecting to MongoDB");
            System.out.println(ex.toString());
        }
    }

    public void Close()
    {
        try
        {
            System.out.println("Closing MongoDB connection...");

            posts = null;
            db = null;
            mongoClient.close();

            System.out.println("Closed MongoDB connection");
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing MongoDB connection");
            System.out.println(ex.toString());
        }
    }
    
    public Post GetPost(String pID)
    {
        return null;
    }
    
    public List<Comment> GetComments(String pID)
    {
        return null;
    }
    
    public List<Post> GetRecentPosts()
    {
     return null;   
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        return null;   
    }
    
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm)
    {
     return null;   
    }
    
    public void AddPost(Post post)
    {
        
    }
    
    public void AddPosts(List<Post> posts)
    {
        
    }
    
    public void AddComment(String pID, Comment comment) 
    {
        
    }
    
    public void EmptyDatabase()
    {
        
    }
}

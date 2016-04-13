package DAL;

import DataObjects.Post;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;

public class MongoConnection
{
    private static String ipAddress = "72.129.239.46";
    private static String port = "27444";
    private static String userID = "cs470";
    private static String password = "cs470lnw";
    private static String databaseName = "cs470blog";
    private static String URI =
            "mongodb://" + userID +":" + password 
            + "@" + ipAddress + ":" + port + "/" + databaseName;

    private static MongoConnection singleton = null;
    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Document> posts;

    private MongoConnection()
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

    public static MongoConnection GetMongoConnection()
    {
        if (singleton == null)
        {
            singleton = new MongoConnection();
        }
        return singleton;
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
            singleton = null;
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing MongoDB connection");
            System.out.println(ex.toString());
        }
    }
    
    public List<Post> GetRecentPosts()
    {
     return null;   
    }
}
